package com.example.debatazo.usuario;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.debatazo.R;
import com.example.debatazo.configuracion.view.Configuracion;
import com.example.debatazo.usuario.datospersonal.ActividadDatosPersonal;
import com.example.debatazo.usuario.iniciarsesion.ui.login.IniciaSesion;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModel;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModelFactory;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private LinearLayout info;
    private View rootView;
    private ImageView configuracion;
    private ImageView perfil;
    private ImageView modoTema;
    private LoginViewModel loginViewModel;
    private TextView nombreUsuario, nPublicarD, nPublicarDRespondido, nPublicarDGustado;
    private TextView idUsuario;
    public static final String TYPE_KEY = "TYPE_KEY";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragmento_perfil, container, false);
        }

        vincularVistas();

        // Crear una instancia del ViewModel utilizando un ViewModelProvider y una Factory personalizada
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        loginViewModel.autoLogin(getContext());

        mostrarDatos();

        info.setOnClickListener(view -> {
            if (!loginViewModel.getLoginRepository().isLoggedIn()){
                Intent i = new Intent(getContext(), IniciaSesion.class);
                startActivity(i);
            }
        });

        perfil.setOnClickListener(view -> {

            Intent i;

            if (loginViewModel.getLoginRepository().isLoggedIn()) {
                i = new Intent(getContext(), ActividadDatosPersonal.class);
            } else {
                i = new Intent(getContext(), IniciaSesion.class);
            }
            startActivity(i);
        });
        configuracion.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), Configuracion.class);
            startActivity(i);
        });

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                modoTema.setImageResource(R.drawable.tema_diurno);
                modoTema.setTag("diurno");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            // Night mode is not active, we're in day time
            case Configuration.UI_MODE_NIGHT_YES:
                modoTema.setImageResource(R.drawable.tema_nocturno);
                modoTema.setTag("nocturno");
                break;
            // Night mode is active, we're at night!
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                modoTema.setImageResource(R.drawable.tema_dia_noche);
                modoTema.setTag("auto");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            // We don't know what mode we're in, assume notnight
        }

        modoTema.setOnClickListener(view -> {
            if (modoTema.getTag().equals("diurno")) {
                modoTema.setImageResource(R.drawable.tema_nocturno);
                modoTema.setTag("nocturno");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (modoTema.getTag().equals("nocturno")){
                modoTema.setImageResource(R.drawable.tema_dia_noche);
                modoTema.setTag("auto");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }else {
                modoTema.setImageResource(R.drawable.tema_diurno);
                modoTema.setTag("diurno");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
        return rootView;

    }
    private void vincularVistas() {
        info = rootView.findViewById(R.id.fPerfil_datosUsuario);
        perfil = rootView.findViewById(R.id.fPerfil_imageV_perfil);
        configuracion = rootView.findViewById(R.id.fPerfil_imageV_configuracion);
        modoTema = rootView.findViewById(R.id.fPerfil_imageV_tema);
        nombreUsuario = rootView.findViewById(R.id.fragmentoP_textV_nombreUsuario);
        idUsuario = rootView.findViewById(R.id.fragmentoP_textV_idUsuario);
        nPublicarD = rootView.findViewById(R.id.fPerfil_textV_numeroPublicarDebate);
        nPublicarDRespondido = rootView.findViewById(R.id.fPerfil_textV_numeroPublicarDebateRespondido);
        nPublicarDGustado = rootView.findViewById(R.id.fPerfil_textV_numeroPublicarDebateGustado);
    }

    public void mostrarDatos() {
        if(loginViewModel != null){
            loginViewModel.getLoginRepository().getLoggedInUserLiveData().observe(getViewLifecycleOwner(),loggedInUser -> {
                if(loggedInUser != null){
                    nombreUsuario.setText(loggedInUser.getUser_name());
                    idUsuario.setText(String.format("ID:%s", loggedInUser.getId()));
                    Picasso.get().load(loggedInUser.getProfile_img()).into(perfil);
                    nPublicarD.setText(String.valueOf(loggedInUser.getDebate_create()));
                    nPublicarDRespondido.setText(String.valueOf(loggedInUser.getComment_debate()));
                    nPublicarDGustado.setText(String.valueOf(loggedInUser.getDebate_like()));
                    info.setClickable(false);
                }else {
                    resetearDatos();
                }
            });
        }
    }

    private void resetearDatos() {
        nombreUsuario.setText(R.string.conectar_ahora);
        idUsuario.setText(R.string.mensaje_campo_id_usuario);
        perfil.setImageResource(R.drawable.usuario);
        info.setClickable(true);
    }
}
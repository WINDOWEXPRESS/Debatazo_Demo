package com.example.debatazo.usuario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.debatazo.R;
import com.example.debatazo.configuracion.Configuracion;
import com.example.debatazo.savesharedpreference.SaveSharedPreference;
import com.example.debatazo.token.Token;
import com.example.debatazo.usuario.datospersonal.ActividadDatosPersonal;
import com.example.debatazo.usuario.iniciarsesion.data.LoginDataSource;
import com.example.debatazo.usuario.iniciarsesion.data.LoginRepository;
import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;
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
    private TextView nombreUsuario;
    private TextView idUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragmento_perfil, container, false);
        }

        vincularVistas();

        // Crear una instancia del ViewModel utilizando un ViewModelProvider y una Factory personalizada
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        //Si existen token se auto loguea la cuenta.
        if(Token.hasInstance()){
            if (isAdded() && getActivity() != null) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SaveSharedPreference.PREFS_NOMBRE, Context.MODE_PRIVATE);
                loginViewModel.login(
                        sharedPreferences.getString(SaveSharedPreference.EMAIL,""),
                        sharedPreferences.getString(SaveSharedPreference.CONTRASENIA,""),
                        getContext()
                );
            } else {
                throw new IllegalStateException("Fragment is not attached to an activity or activity is destroyed.");
            }
        }

        ActivityResultLauncher<Intent> lanzador = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                mostrarDatos();
            }
        });

        ActivityResultLauncher<Intent> lanzadorLogOut = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                resetearDatos();
            }
        });


        info.setOnClickListener(view -> {
            if (!loginViewModel.getLoginRepository().isLoggedIn()){
                Intent i = new Intent(getContext(), IniciaSesion.class);
                lanzador.launch(i);
            }
        });

        perfil.setOnClickListener(view -> {

            Intent i;

            if (loginViewModel.getLoginRepository().isLoggedIn()) {
                i = new Intent(getContext(), ActividadDatosPersonal.class);
            } else {
                i = new Intent(getContext(), IniciaSesion.class);
            }
            lanzador.launch(i);
        });
        configuracion.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), Configuracion.class);
            lanzadorLogOut.launch(i);
        });

        modoTema.setOnClickListener(view -> {
            if (modoTema.getTag().equals("diurno")) {
                modoTema.setImageResource(R.drawable.tema_nocturno);
                modoTema.setTag("nocturno");
            } else {
                modoTema.setImageResource(R.drawable.tema_diurno);
                modoTema.setTag("diurno");
            }
        });
        return rootView;

    }
    private void vincularVistas() {
        info = rootView.findViewById(R.id.fragmentoP_datosUsuario);
        perfil = rootView.findViewById(R.id.fragmentoP_imageV_perfil);
        configuracion = rootView.findViewById(R.id.fragmentoP_imageV_configuracion);
        modoTema = rootView.findViewById(R.id.fragmentoP_imageV_tema);
        nombreUsuario = rootView.findViewById(R.id.fragmentoP_textV_nombreUsuario);
        idUsuario = rootView.findViewById(R.id.fragmentoP_textV_idUsuario);

    }

    public void mostrarDatos() {
        LoggedInUser user = loginViewModel.getLoginRepository().getUser();
        nombreUsuario.setText(user.getUser_name());
        idUsuario.setText("ID:" + user.getId());
        Picasso.get().load(user.getProfile_img()).into(perfil);
        info.setClickable(false);
    }

    private void resetearDatos() {
        nombreUsuario.setText(R.string.conectar_ahora);
        idUsuario.setText(R.string.mensaje_campo_id_usuario);
        perfil.setImageResource(R.drawable.usuario);
        info.setClickable(true);
    }
}
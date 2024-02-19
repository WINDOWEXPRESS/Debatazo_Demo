package com.example.debatazo.usuario;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.debatazo.R;
import com.example.debatazo.configuracion.Configuracion;
import com.example.debatazo.usuario.datospersonal.ActividadDatosPersonal;
import com.example.debatazo.usuario.iniciarsesion.ui.login.IniciaSesion;

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

    private TextView texto;
    private View rootView;
    private ImageView configuracion ;
    private ImageView perfil ;
    private ImageView modoTema ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragmento_perfil, container, false);
        }

        texto = rootView.findViewById(R.id.fragmentoP_textV_idUsuario);
        texto.setOnClickListener(view -> {
            Intent i = new Intent(getContext(),IniciaSesion.class);
            startActivity(i);
        });
        configuracion = rootView.findViewById(R.id.fragmentoP_imageV_configuracion);
        configuracion.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), Configuracion.class);
            startActivity(i);
        });
        perfil = rootView.findViewById(R.id.fragmentoP_imageV_perfil);
        perfil.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), ActividadDatosPersonal.class);
            startActivity(i);
        });

        modoTema = rootView.findViewById(R.id.fragmentoP_imageV_tema);
        modoTema.setOnClickListener(view -> {
            if (modoTema.getTag().equals("diurno")){
                modoTema.setImageResource(R.drawable.tema_nocturno);
                modoTema.setTag("nocturno");
            }else {
                modoTema.setImageResource(R.drawable.tema_diurno);
                modoTema.setTag("diurno");
            }
        });
        return rootView;
    }
}
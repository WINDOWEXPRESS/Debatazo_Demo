package com.example.debatazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.debatazo.debaterecycler.DebateFragmento;
import com.example.debatazo.usuario.PerfilFragment;
import com.example.debatazo.databinding.ActividadPrincipalBinding;

public class ActividadPrincipal extends AppCompatActivity {

   ActividadPrincipalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 /*       setContentView(R.layout.actividad_principal);
    }*/
        binding = ActividadPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new PrincipalFragmento());

        binding.actividadPBottomNV.setBackground(null);
        binding.actividadPBottomNV.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menuB_principal:
                    replaceFragment(new PrincipalFragmento());
                    break;
                case R.id.menuB_debate:
                    replaceFragment(new DebateFragmento());
                    break;
                case R.id.menuB_valoracion:
                    replaceFragment(new ValoracionFragmento());
                    break;
                case R.id.menuB_usuario:
                    replaceFragment(new PerfilFragment());
                    break;
            }

            return true;
        });

        binding.actividadPFloatingAB.setOnClickListener(view -> showBottomDialog());

    }
    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.actividadP_frameL, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {
        //Instancia dialog , la caracteristica de que sea sin titulo , y configurar el layout
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.desplegable_publicar);

        //Vinculo con los componentes de layout
        LinearLayout debate = dialog.findViewById(R.id.desplegableP_lineaLH_crearDebate);
        LinearLayout valoracion = dialog.findViewById(R.id.desplegableP_lineaLH_crearValoracion);
        LinearLayout proximamente = dialog.findViewById(R.id.desplegableP_lineaLH_proximamente);
        ImageView cancelButton = dialog.findViewById(R.id.desplegableP_imagenV_cancelar);

        debate.setOnClickListener(v -> {

            dialog.dismiss();
            Toast.makeText(ActividadPrincipal.this,"A jorge le encanta toast",Toast.LENGTH_SHORT).show();

        });

        valoracion.setOnClickListener(v -> {

            dialog.dismiss();
            Toast.makeText(ActividadPrincipal.this,"A jorge le encanta toast",Toast.LENGTH_SHORT).show();

        });

        proximamente.setOnClickListener(v -> {

            dialog.dismiss();
            Toast.makeText(ActividadPrincipal.this,"A jorge le encanta toast",Toast.LENGTH_SHORT).show();

        });

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        // Establece el fondo del cuadro de diálogo como transparente.
        // Esto puede ser útil para crear cuadros de diálogo con esquinas redondeadas o formas personalizadas.
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //tener la animacion de entrar y salir
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimacion;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}
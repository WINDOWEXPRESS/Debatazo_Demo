package com.example.debatazo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.debatazo.databinding.ActividadPrincipalBinding;
import com.example.debatazo.debate.DebateFragmento;
import com.example.debatazo.usuario.PerfilFragment;
import com.example.debatazo.usuario.iniciarsesion.ui.login.IniciaSesion;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModel;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModelFactory;

import java.io.IOException;

public class ActividadPrincipal extends AppCompatActivity {
    // Definir una constante para el código de solicitud de la galería
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imagenPublicar;
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

        binding.actividadPFloatingAB.setOnClickListener(view -> {
            // Crear una instancia del ViewModel utilizando un ViewModelProvider y una Factory personalizada
            LoginViewModel loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);
            if (loginViewModel.getLoginRepository().isLoggedIn()) {
                showBottomDialog();
            } else {
                Intent i = new Intent(view.getContext(), IniciaSesion.class);
                startActivity(i);
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
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
        TextView debate = dialog.findViewById(R.id.desplegableP_textV_debate);
        TextView valoracion = dialog.findViewById(R.id.desplegableP_textV_valoracion);
        ImageView cancelButton = dialog.findViewById(R.id.desplegableP_imagenV_cancelar);
        imagenPublicar = dialog.findViewById(R.id.desplegableP_imageV_imagen);

        debate.setOnClickListener(v -> {

        });

        valoracion.setOnClickListener(v -> {
            Toast.makeText(ActividadPrincipal.this, "Funcion sin implementar.", Toast.LENGTH_SHORT).show();

        });
        imagenPublicar.setOnClickListener(view -> {
            // Método para abrir la galería cuando se hace clic en un botón
            abrirGaleria();
        });
        cancelButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Establece el fondo del cuadro de diálogo como transparente.
        // Esto puede ser útil para crear cuadros de diálogo con esquinas redondeadas o formas personalizadas.
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //tener la animacion de entrar y salir
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimacion;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Manejar el resultado de la selección de la imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                // Convertir la URI en un bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Establecer el bitmap en el ImageView
                imagenPublicar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
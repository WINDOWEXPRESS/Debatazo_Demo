package com.example.debatazo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.debatazo.band.BandObject;
import com.example.debatazo.configuracion.BrilloUtils;
import com.example.debatazo.databinding.ActividadPrincipalBinding;
import com.example.debatazo.debaterecycler.DebateFragmento;
import com.example.debatazo.debaterecycler.DebateProducto;
import com.example.debatazo.debaterecycler.api.ServicioDebateProducto;
import com.example.debatazo.imgur.ImgurObject;
import com.example.debatazo.imgur.ImgurService;
import com.example.debatazo.imgur.Medias;

import com.example.debatazo.savesharedpreference.SharedPreferenceUtils;
import com.example.debatazo.usuario.PerfilFragment;
import com.example.debatazo.usuario.iniciarsesion.ui.login.IniciaSesion;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModel;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModelFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActividadPrincipal extends AppCompatActivity {
    // Definir una constante para el código de solicitud de la galería

    private ImageView imagenPublicar;
    private PerfilFragment perfilFragment = new PerfilFragment();
    private LoginViewModel loginViewModel;
    private ActivityResultLauncher<String> requestResultLauncher;
    private ActivityResultLauncher<Intent> resultLauncher;
    private Uri uriImagen;
    private Medias medias = new Medias();

    private final static int IMG_VIEW_SIZE = 1000;
    ActividadPrincipalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 /*       setContentView(R.layout.actividad_principal);
    }*/
        binding = ActividadPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new PrincipalFragmento());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);
        loginViewModel.autoLogin(this);

        //ajuste de brillo
        BrilloUtils.getInstancia().brilloAppVista(this);

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
                    replaceFragment(perfilFragment);
                    break;
            }

            return true;
        });

        // Registrar el resultado de la solicitud de permiso
        requestResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            // Si el permiso está concedido, abrir la galería
            if (isGranted) {
                abrirGaleria();
            } else {
                // Si el permiso no está concedido, mostrar un mensaje de error
                Toast.makeText(this, "No se ha concedido el permiso", Toast.LENGTH_SHORT).show();
            }
        });

        // Registrar el resultado de la actividad de la galería
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // Obtener la URI de la imagen
                medias.setImageUri(result.getData().getData());
                // Establecer el tamaño de la vista de la imagen
                imagenPublicar.getLayoutParams().height = medias.IMG_VIEW_SIZE;
                // Mostrar la imagen
                imagenPublicar.setImageURI(medias.getImageUri());
            }
        });

        binding.actividadPFloatingAB.setOnClickListener(view -> {
            // Crear una instancia del ViewModel utilizando un ViewModelProvider y una Factory personalizada
            LoginViewModel loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                    .get(LoginViewModel.class);
            //Si ya ha iniciado sesion entonces mostrar el dialog
            if (loginViewModel.getLoginRepository().isLoggedIn()) {
                showBottomDialog();
            } else {
                //Ir a actividad de inicia sesion
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
        EditText titulo = dialog.findViewById(R.id.desplegableP_editTT_titulo);
        EditText descripcion = dialog.findViewById(R.id.desplegableP_editTT_descripcion);
        EditText banda_1 = dialog.findViewById(R.id.desplegableP_editTT_banda1);
        EditText banda_2 = dialog.findViewById(R.id.desplegableP_editTT_banda2);
        Button publicar = dialog.findViewById(R.id.desplegableP_button_publicarDebate);

        imagenPublicar = dialog.findViewById(R.id.desplegableP_imageV_imagen);

        debate.setOnClickListener(v -> {

        });

        valoracion.setOnClickListener(v -> {
            Toast.makeText(ActividadPrincipal.this, "Proximamente.", Toast.LENGTH_SHORT).show();

        });

        imagenPublicar.setOnClickListener(view -> {
            // Método para abrir la galería cuando se hace clic en el botónç
            // Si el permiso de acceso a la galería está concedido, abrir la galería
            if(medias.checkPermission(ActividadPrincipal.this)){
                abrirGaleria();
            }else{
                // Si el permiso no está concedido, mostrar el diálogo de permiso
                showPermissionRationaleDialog();
            }
        });

        publicar.setOnClickListener(view -> {
            //Validar que todos los campos esten llenos
            if(titulo.getText().toString().isEmpty() ||
                    descripcion.getText().toString().isEmpty() ||
                    banda_1.getText().toString().isEmpty() ||
                    banda_2.getText().toString().isEmpty()
            ){
            //Si alguno de los campos esta vacio mostrar un mensaje de error
                Toast.makeText(ActividadPrincipal.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();


            }else{
                // Si todos los campos estan llenos, publicar el debate
                //Obtener el token y el id del usuario
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                String token = sharedPref.getString(SharedPreferenceUtils.TOKEN_VALUE, "");
                int userId = sharedPref.getInt(SharedPreferenceUtils.USER_ID, 0);

                // Crear un nuevo DebateProducto con los datos del formulario
                Set<BandObject> bands = new HashSet<>();
                bands.add(new BandObject(banda_1.getText().toString(),BandObject.P));
                bands.add(new BandObject(banda_2.getText().toString(),BandObject.N));
                DebateProducto debateProducto = new DebateProducto(
                        titulo.getText().toString(),
                        descripcion.getText().toString(),
                        bands);
                if(medias.getImageUri() == null) {
                    // Si no se ha seleccionado una imagen, publicar el debate sin imagen
                    publicarDebate(debateProducto,token,userId);
                }else{
                    // Si se ha seleccionado una imagen, publicar el debate con imagen
                    publicarDebateConImagen(debateProducto,token,userId);
                }
            }
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

    // Método para abrir la galería
    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        resultLauncher.launch(intent);
    }

   // Método para mostrar el diálogo de permiso de acceso a la galería
    private void showPermissionRationaleDialog(){
        // Crear el diálogo de permiso
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Se necesita permiso"); // Título del diálogo
        builder.setMessage("Se necesita permiso para acceder a la galería"); // Mensaje del diálogo
        // Añadir botones para aceptar o cancelar el permiso
        builder.setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Solicitar el permiso de acceso a la galería
                requestResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });
        builder.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cerrar dialogo
                dialog.dismiss();
            }
        });
        builder.create().show(); // Mostrar el diálogo
    }

    // Método para publicar el debate
    private void publicarDebate(DebateProducto debateProducto,String token,int userId){
        Call<String> debateCall = ServicioDebateProducto.getInstance().getRepor().publicarDebate(
                token,
                debateProducto,
                String.valueOf(userId)
        );
        debateCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActividadPrincipal.this, response.body(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActividadPrincipal.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ActividadPrincipal.this, "Error al publicar el debate", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void publicarDebateConImagen(DebateProducto debateProducto,String token,int userId){
        try {
            // Copiar el contenido del flujo de entrada al archivo
            File file = medias.copyStreamToFile(medias.getImageUri(), ActividadPrincipal.this);
            // Crear RequestBody para la imagen
            RequestBody imgBody = RequestBody.create(MediaType.parse("image/*"), file);
            // Crear RequestBody para el nombre del archivo
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"),file.getName());
            // Crear MultipartBody.Part para la imagen
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(),imgBody);

            Call<ImgurObject> imgurCall = ImgurService.getInstance().getRepor().uploadImage(part,name);
            imgurCall.enqueue(new Callback<ImgurObject>() {

                @Override
                public void onResponse(Call<ImgurObject> call, Response<ImgurObject> response) {
                    if (response.isSuccessful()) {
                        ImgurObject imgurObject = response.body();
                        debateProducto.setImgUrl(imgurObject.getData().getLink());
                        debateProducto.setImage_delete_hash(imgurObject.getData().getDeletehash());
                        publicarDebate(debateProducto,token,userId);
                    }else{
                        Toast.makeText(ActividadPrincipal.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ImgurObject> call, Throwable t) {
                    Toast.makeText(ActividadPrincipal.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
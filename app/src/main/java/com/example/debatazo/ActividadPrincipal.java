package com.example.debatazo;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.debatazo.utils.BrilloUtils;
import com.example.debatazo.databinding.ActividadPrincipalBinding;
import com.example.debatazo.debaterecycler.DebateFragmento;
import com.example.debatazo.debaterecycler.DebateProducto;
import com.example.debatazo.debaterecycler.api.servicio.ServicioDebates;
import com.example.debatazo.imgur.ImgurObject;
import com.example.debatazo.imgur.api.ImgurService;
import com.example.debatazo.imgur.Medias;
import com.example.debatazo.usuario.PerfilFragment;
import com.example.debatazo.usuario.iniciarsesion.data.model.Token;
import com.example.debatazo.usuario.iniciarsesion.ui.login.IniciaSesion;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModel;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModelFactory;
import com.example.debatazo.utils.Dialogs;
import com.example.debatazo.utils.GlobalConstants;
import com.example.debatazo.utils.GlobalFuntion;


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActividadPrincipal extends AppCompatActivity {
    // Definir una constante para el código de solicitud de la galería

    private TextView aPrincipal_textV_fondo;
    private ProgressBar aPrincipal_progressB;
    private ImageView imagenPublicar;
    private final PerfilFragment perfilFragment = new PerfilFragment();
    private final DebateFragmento debateFragment = new DebateFragmento();
    private Fragment currentFragment = null;
    private LoginViewModel loginViewModel;
    private ActivityResultLauncher<String> requestResultLauncher;
    private ActivityResultLauncher<Intent> resultLauncher;
    private final Medias medias = new Medias();
    private Dialogs dialogs;

    ActividadPrincipalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 /*       setContentView(R.layout.actividad_principal);
    }*/
        binding = ActividadPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new DebateFragmento());

        aPrincipal_textV_fondo = findViewById(R.id.aPrincipal_textV_fondo);
        aPrincipal_progressB = findViewById(R.id.aPrincipal_progressB);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);
        loginViewModel.autoLogin(this);
        //ajuste de brillo
        BrilloUtils.getInstancia().brilloAppObserver(this);

        binding.aPrincipalFloatingAB.setBackground(null);
        binding.aPrincipalBottomNV.setSelectedItemId(R.id.menuB_debate);
        binding.aPrincipalBottomNV.setOnItemSelectedListener(item -> {
            currentFragment = getCurrentFragment();
            switch (item.getItemId()) {
                case R.id.menuB_principal:
                    replaceFragment(new PrincipalFragmento());
                    break;
                case R.id.menuB_debate:
                    if(currentFragment != null && currentFragment.getClass().equals(debateFragment.getClass())){
                        replaceFragment(new DebateFragmento());
                    }else {
                        replaceFragment(debateFragment);
                    }
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
                dialogs = new Dialogs(getResources().getString(R.string.advertencia),getResources().getString(R.string.iniciar_sesision));
                dialogs.showDialog(ActividadPrincipal.this);
            }
        });

        // Registrar el resultado de la actividad de la galería
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // Obtener la URI de la imagen
                medias.setImageUri(result.getData().getData());
                // Establecer el tamaño de la vista de la imagen
                imagenPublicar.getLayoutParams().height = medias.IMG_VIEW_SIZE/2;
                // Mostrar la imagen
                imagenPublicar.setImageURI(medias.getImageUri());
            }
        });

        binding.aPrincipalFloatingAB.setOnClickListener(view -> {
            // Crear una instancia del ViewModel utilizando un ViewModelProvider y una Factory personalizada
            LoginViewModel loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                    .get(LoginViewModel.class);
            //Si ya ha iniciado sesion entonces mostrar el dialog
            if (loginViewModel.getLoginRepository().isLoggedIn()) {
                showBottomDialog();
            } else {
                //Ir a actividad de inicia sesion
                Intent i = new Intent(view.getContext(), IniciaSesion.class);
                dialogs = new Dialogs(
                        getResources().getString(R.string.advertencia),
                        getResources().getString(R.string.iniciar_sesision),
                        i,true, true);
                dialogs.showConfirmDialog(ActividadPrincipal.this);
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.aPrincipal_frameL, fragment);
        fragmentTransaction.commit();
    }
    private Fragment getCurrentFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentById(R.id.aPrincipal_frameL);
    }

    private void showBottomDialog() {
        //Instancia dialog , la caracteristica de que sea sin titulo , y configurar el layout
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.desplegable_publicar);

        //Vinculo con los componentes de layout
        TextView debate = dialog.findViewById(R.id.dPublicar_textV_debate);
        TextView valoracion = dialog.findViewById(R.id.dPublicar_textV_valoracion);
        ImageView cancelButton = dialog.findViewById(R.id.dPublicar_imagenV_cancelar);
        EditText titulo = dialog.findViewById(R.id.dPublicar_editTT_titulo);
        EditText descripcion = dialog.findViewById(R.id.dPublicar_editTT_descripcion);
        EditText banda_1 = dialog.findViewById(R.id.dPublicar_editTT_banda1);
        EditText banda_2 = dialog.findViewById(R.id.dPublicar_editTT_banda2);
        Button publicar = dialog.findViewById(R.id.dPublicar_button_publicarDebate);

        imagenPublicar = dialog.findViewById(R.id.dPublicar_imageV_imagen);

        debate.setOnClickListener(v -> {

        });

        valoracion.setOnClickListener(v -> Toast.makeText(ActividadPrincipal.this, "Proximamente.", Toast.LENGTH_SHORT).show());

        imagenPublicar.setOnClickListener(view -> {
            // Método para abrir la galería cuando se hace clic en el botónç
            // Si el permiso de acceso a la galería está concedido, abrir la galería
            if(medias.checkPermission(this)){
                abrirGaleria();
            }else{
                GlobalFuntion.showSettingsDialog(ActividadPrincipal.this);
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
                dialogs = new Dialogs(getResources().getString(R.string.advertencia),getResources().getString(R.string.campos_obligatorios));
                dialogs.showDialog(ActividadPrincipal.this);
            }else{
                publicar.setEnabled(false);
                aPrincipal_textV_fondo.setVisibility(View.VISIBLE);
                aPrincipal_textV_fondo.bringToFront();
                aPrincipal_progressB.setVisibility(View.VISIBLE);
                aPrincipal_progressB.bringToFront();
                // Si todos los campos estan llenos, publicar el debate
                //Obtener el token y el id del usuario
                String token = Token.getInstance().getValue();
                int userId = Token.getInstance().getUserId();

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
                    publicarDebate(debateProducto,token,userId,dialog);
                }else{
                    // Si se ha seleccionado una imagen, publicar el debate con imagen
                    publicarDebateConImagen(debateProducto,token,userId,dialog);
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


    //Método para publicar un debate utilizando el servicio de debate de productos.
    private void publicarDebate(DebateProducto debateProducto, String token, int userId,Dialog dialog) {
        // Crear una llamada para publicar el debate utilizando el servicio de debate de productos.
        Call<ResponseBody> debateCall = ServicioDebates.getInstance().getRepor().publicarDebate(
                token,
                debateProducto,
                String.valueOf(userId)
        );

        // Realizar la llamada asíncrona para publicar el debate.
        debateCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String title = "";
                String mesage = "";
                // Verificar si la respuesta es exitosa.
                if (response.isSuccessful()) {
                    try {
                        // Mostrar el mensaje de respuesta en un Toast y cerrar dialog
                        title = "Exito";
                        mesage = response.body().string();

                    } catch (IOException e) {
                        // Mostrar el mensaje de error en un Toast si ocurre un error de E/S.
                        title = "Error";
                        mesage = e.getMessage();
                    }

                } else {
                    // Mostrar el mensaje de error en un Toast si la respuesta no es exitosa.
                    title = "Error";
                    mesage = response.errorBody().toString();
                }

                aPrincipal_textV_fondo.setVisibility(View.GONE);
                aPrincipal_progressB.setVisibility(View.GONE);

                dialog.dismiss();
                dialogs = new Dialogs(title,mesage);
                dialogs.showDialog(ActividadPrincipal.this);
                if(getCurrentFragment().getClass().equals(debateFragment.getClass())){
                    replaceFragment(new DebateFragmento());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof IOException){
                    dialogs = new Dialogs(getResources().getString(R.string.error), getResources().getString(R.string.conexion_fallido));
                }else{
                    dialogs = new Dialogs(getResources().getString(R.string.error), getResources().getString(R.string.error_desconocido));
                }

                aPrincipal_textV_fondo.setVisibility(View.GONE);
                aPrincipal_progressB.setVisibility(View.GONE);

                dialogs.showDialog(ActividadPrincipal.this);
                dialog.dismiss();
            }
        });
    }
    private void publicarDebateConImagen(DebateProducto debateProducto,String token,int userId,Dialog dialog){
        try {
            // Crear MultipartBody.Part para la imagen
            MultipartBody.Part part = medias.generateMultipartBody(medias.getImageUri(),ActividadPrincipal.this);

            // Crear los RequestBody de tipo texto plano
            RequestBody title = medias.createTextPlainBody(medias.SIMPLE_TITLE);
            RequestBody description = medias.createTextPlainBody(medias.SIMPLE_DESCRIPTION);
            RequestBody type = medias.createTextPlainBody(medias.SIMPLE_TYPE);

            Call<ImgurObject> imgurCall = ImgurService.getInstance().getRepor().uploadImage(part,type,title,description);
            imgurCall.enqueue(new Callback<ImgurObject>() {
                String tt = "";
                String m = "";
                @Override
                public void onResponse(Call<ImgurObject> call, Response<ImgurObject> response) {
                    if (response.isSuccessful()) {
                        ImgurObject imgurObject = response.body();

                        debateProducto.setImageUrl(imgurObject.getData().getLink());
                        debateProducto.setImageDeleteHash(imgurObject.getData().getDeletehash());

                        publicarDebate(debateProducto,token,userId,dialog);
                    }else{
                        tt = getResources().getString(R.string.error);
                        m = response.errorBody().toString();
                        dialogs = new Dialogs(tt,m);
                        dialogs.showDialog(ActividadPrincipal.this);
                    }
                }
                @Override
                public void onFailure(Call<ImgurObject> call, Throwable t) {
                    tt = getResources().getString(R.string.error);
                    m = t.getMessage();
                    dialogs = new Dialogs(tt,m);
                    dialogs.showDialog(ActividadPrincipal.this);
                    dialog.dismiss();
                }
            });
        }catch (IOException e){
            dialogs = new Dialogs(getResources().getString(R.string.error),e.getMessage());
            dialogs.showDialog(ActividadPrincipal.this);
            dialog.dismiss();
        }
    }
}
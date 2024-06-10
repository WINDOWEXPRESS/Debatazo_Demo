package com.example.debatazo.usuario.datospersonal;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.debatazo.R;
import com.example.debatazo.databinding.ActividadDatosPersonalBinding;
import com.example.debatazo.imgur.ImgurObject;
import com.example.debatazo.imgur.Medias;
import com.example.debatazo.imgur.api.ImgurService;
import com.example.debatazo.usuario.EnumPerfil;
import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModel;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModelFactory;
import com.example.debatazo.utils.BrilloUtils;
import com.example.debatazo.utils.GlobalFuntion;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadDatosPersonal extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ActividadDatosPersonalBinding binding;
    private TextView limiteNumerico;
    private TextView mensajeError;
    private TextView id;
    private TextView nombreUsuario;
    private TextView nombrePersonal;
    private EditText fecha;
    private Spinner sexo;
    private ImageView perfil;
    private String perfil_img_url;
    private EditText descripcionPersonal;
    private ImageButton volver;
    private LoginViewModel loginViewModel;
    private ProgressBar cargando;
    private Button guardar;
    private final Medias medias = new Medias();
    private boolean imagenGaleria = false;
    ActivityResultLauncher<String> requestResultLauncher ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActividadDatosPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vincularComponentes();

        //ajuste de brillo
        BrilloUtils.getInstancia().brilloAppObserver(this);

        volver.setOnClickListener(view -> finish());

        mostrarInformacion();

        perfil.setOnClickListener(view -> mostrarDialogoPerfil());

        fecha.setOnClickListener(view -> showDatePickerDialog(this));

        maximoCaracteres(descripcionPersonal, limiteNumerico);

        loginViewModel.getLoadingLiveData().observe(this, loading -> cargando.setVisibility(loading?View.VISIBLE:View.GONE));
        
        loginViewModel.getLoadingLiveData().observe((this) , loading-> {
            guardar.setEnabled(!loading);
        });

        guardar.setOnClickListener(view -> {
            updatePerfil();
        });

        // Registrar el resultado de la solicitud de permiso
        requestResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            // Si el permiso está concedido, abrir la galería
            if (isGranted) {
                abrirGaleria();
            } else {
                GlobalFuntion.showSettingsDialog(ActividadDatosPersonal.this);
            }
        });

    }

    private void updatePerfil() {
        AlertDialog.Builder confirmar = new AlertDialog.Builder(ActividadDatosPersonal.this);
        confirmar.setTitle("Confirmar");
        confirmar.setMessage("Deseas guardar los cambios?");

        confirmar.setPositiveButton("Aceptar", (dialog, which) -> {

            String sexoAbreviatura = ArrayAdapter.createFromResource(
                        this,
                        R.array.sex_abrev_array,
                        android.R.layout.simple_spinner_dropdown_item
                ).getItem(sexo.getSelectedItemPosition()).toString();
            ;
            LoggedInUser user;
//si fecha no es vacio
            if(!fecha.getText().toString().isEmpty()) {
                LocalDate fech = LocalDate.parse(fecha.getText());
                // Hora actual
                LocalTime hora = LocalTime.now();

                // Fecha y hora combinadas
                LocalDateTime fechaHora = fech.atTime(hora);

                // Zona horaria (UTC)
                ZoneOffset zonaHoraria = ZoneOffset.UTC;

                // Agregar la zona horaria a la fecha y hora combinadas
                OffsetDateTime fechaHoraConZona = fechaHora.atOffset(zonaHoraria);

                // Formatear la fecha y hora con la zona horaria en formato ISO 8601
                String fechaHoraFormateada = fechaHoraConZona.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

                user = new LoggedInUser(id.getText().toString(),nombreUsuario.getText().toString(),
                        nombrePersonal.getText().toString(),perfil_img_url,fechaHoraFormateada,
                        descripcionPersonal.getText().toString(), sexoAbreviatura);
            }else {
                 user = new LoggedInUser(id.getText().toString(),nombreUsuario.getText().toString(),
                        nombrePersonal.getText().toString(),perfil_img_url,null,
                        descripcionPersonal.getText().toString(), sexoAbreviatura);
            }


            if (imagenGaleria){
                subirImagenImgur(user);
            }else {
                loginViewModel.updatePerfil(user,mensajeError);

            }


            dialog.dismiss(); // Cierra el AlertDialog
        });

        confirmar.setNegativeButton("Cancelar", (dialog, which) -> {
            // Acción a realizar al hacer clic en Cancelar
            dialog.dismiss(); // Cierra el AlertDialog
        });
        AlertDialog alertDialog = confirmar.create();
        alertDialog.show();
    }

    private void maximoCaracteres(EditText editText, TextView textView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No necesitas realizar ninguna acción antes de que cambie el texto.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No necesitas realizar ninguna acción mientras cambia el texto.
            }

            @Override
            public void afterTextChanged(Editable editable) {
                textView.setText(getString(R.string.limite_numerico, editable.length()));
            }
        });


    }

    private void vincularComponentes() {
        limiteNumerico = binding.aDPersonalTextVLimiteNumerico;
        descripcionPersonal = binding.aDPersonalEditTTDescripcionPersonal;
        volver = binding.aDPersonalImageBVolver;
        id = binding.aDPersonalEditTTId;
        nombreUsuario = binding.aDPersonalEditTTNombre;
        nombrePersonal = binding.aDPersonalEditTTNombrePersonal;
        fecha = binding.aDPersonalEditTTFecha;
        sexo = binding.aDPersonalSpinnerSexo;
        perfil = binding.aDPersonalImageVPerfil;
        guardar = binding.aDPersonalButtonGuardar;
        mensajeError = binding.aDPersonalTextViewMensajeError;
        cargando = binding.aDPersonaProgressBCargando;
    }
    private void mostrarInformacion() {
        // Crear una instancia del ViewModel utilizando un ViewModelProvider y una Factory personalizada
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        // Verificar si el usuario está autenticado y se ha obtenido información del usuario

        if (loginViewModel.getLoginRepository().getLoggedInUserLiveData().getValue() != null) {
            // Mostrar la información del usuario en las vistas correspondientes
            loginViewModel.getLoginRepository().getLoggedInUserLiveData().observe(this,loggedInUser -> {

                id.setText(loggedInUser.getUser_id());
                nombreUsuario.setText(loggedInUser.getUser_name());
                nombrePersonal.setText(loggedInUser.getFull_name());

                if (loggedInUser.getBirth_date() !=null) {
                    // Formato original de la fecha
                    SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

                    // Parsear la fecha original
                    try {
                        // Parsear la fecha original
                        Date fechaParseada = formatoOriginal.parse(loggedInUser.getBirth_date());

                        // Nuevo formato de fecha
                        SimpleDateFormat nuevoFormato = new SimpleDateFormat("yyyy-MM-dd");
                        // Formatear la fecha parseada al nuevo formato
                        String fechaFormateada = nuevoFormato.format(fechaParseada);

                        fecha.setText(fechaFormateada);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("loggedInUser.getBirth_date() : "+loggedInUser.getBirth_date());

                String sexoObtenido = loggedInUser.getSex();
                mostrarSexo(sexoObtenido);
                descripcionPersonal.setText(loggedInUser.getSelf());

                // Cargar la imagen de perfil del usuario utilizando Picasso
                perfil_img_url = loggedInUser.getProfile_img();
                Picasso.get().load(perfil_img_url).into(perfil);

            });
        }
    }

    private void mostrarSexo(String sexoObtenido){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.sex_array,
                android.R.layout.simple_spinner_dropdown_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        sexo.setAdapter(adapter);
        switch (sexoObtenido){
            case "H"://HOMBRE
                sexo.setSelection(1);
                break;
            case "M"://MUJER
                sexo.setSelection(2);
                break;
            default:
                sexo.setSelection(0);
                break;
        }
    }
    private void showDatePickerDialog(Context context) {
        final Calendar calendar = Calendar.getInstance();
        // Establecer la fecha predeterminada (por ejemplo, 2000-01-31)
        int anioPredeterminada = 2000;
        int mesPredeterminada = 0; // Noviembre (los meses comienzan desde 0)
        int diaPredeterminada = 31;

        DatePickerDialog dialog = new DatePickerDialog(context,R.style.dialog_date);
        //dialog.setTitle(getString(R.string.seleccionar_fecha_de_nacimiento));
        dialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            fecha.setText(format.format(calendar.getTime()));
        });
        DatePicker datePicker = dialog.getDatePicker();
        //Iniciar fecha predeterminada
        datePicker.init(anioPredeterminada,mesPredeterminada,diaPredeterminada,null);
        datePicker.setMaxDate(calendar.getTimeInMillis());
        dialog.show();
    }


    private void mostrarDialogoPerfil() {
        AlertDialog.Builder perfil = new AlertDialog.Builder(ActividadDatosPersonal.this);
        // Inflar el diseño del diálogo
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.avatar_usuario, null);

        perfil.setView(view);

        final AlertDialog dialog = perfil.create();
        perfil.show();
        //Metodo para elegir perfil
        elegirAvatar(view, dialog);
    }
    private void elegirAvatar(View view, AlertDialog dialog) {
        ImageView hombre1 = view.findViewById(EnumPerfil.HOMBRE1.REFERENCIA_ID);
        ImageView hombre2 = view.findViewById(EnumPerfil.HOMBRE2.REFERENCIA_ID);
        ImageView hombre3 = view.findViewById(EnumPerfil.HOMBRE3.REFERENCIA_ID);
        ImageView mujer1 = view.findViewById(EnumPerfil.MUJER1.REFERENCIA_ID);
        ImageView mujer2 = view.findViewById(EnumPerfil.MUJER2.REFERENCIA_ID);
        ImageView mujer3 = view.findViewById(EnumPerfil.MUJER3.REFERENCIA_ID);
        ImageView mujer4 = view.findViewById(EnumPerfil.MUJER4.REFERENCIA_ID);
        ImageView galeria =  view.findViewById(R.id.aUsuario_imageV_galeria);

        hombre1.setOnClickListener(getOnClickListener(dialog,EnumPerfil.HOMBRE1));
        hombre2.setOnClickListener(getOnClickListener(dialog,EnumPerfil.HOMBRE2));
        hombre3.setOnClickListener(getOnClickListener(dialog,EnumPerfil.HOMBRE3));
        mujer1.setOnClickListener(getOnClickListener(dialog,EnumPerfil.MUJER1));
        mujer2.setOnClickListener(getOnClickListener(dialog,EnumPerfil.MUJER2));
        mujer3.setOnClickListener(getOnClickListener(dialog,EnumPerfil.MUJER3));
        mujer4.setOnClickListener(getOnClickListener(dialog,EnumPerfil.MUJER4));

        galeria.setOnClickListener(view1 -> {
            // Verificar si el permiso de lectura de almacenamiento externo está concedido
            if (medias.checkPermission(ActividadDatosPersonal.this)) {
                // El permiso ya ha sido concedido, puedes realizar la acción que requiere el permiso
                // Por ejemplo, abrir la galería
                abrirGaleria();
            } else {

                requestResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });

    }
    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void subirImagenImgur(LoggedInUser user){
        try {
            // Crear MultipartBody.Part para la imagen
            MultipartBody.Part part = medias.generateMultipartBody(medias.getImageUri(), ActividadDatosPersonal.this);

            // Crear los RequestBody de tipo texto plano
            RequestBody title = medias.createTextPlainBody(medias.SIMPLE_TITLE);
            RequestBody description = medias.createTextPlainBody(medias.SIMPLE_DESCRIPTION);
            RequestBody type = medias.createTextPlainBody(medias.SIMPLE_TYPE);

            Call<ImgurObject> imgurCall = ImgurService.getInstance().getRepor().uploadImage(part,type,title,description);
            imgurCall.enqueue(new Callback<ImgurObject>() {

                @Override
                public void onResponse(Call<ImgurObject> call, Response<ImgurObject> response) {
                    if (response.isSuccessful()) {
                        ImgurObject imgurObject = response.body();
                        Picasso.get().load(imgurObject.getData().getLink()).into(perfil);
                        user.setProfile_img(imgurObject.getData().getLink());

                        loginViewModel.updatePerfil(user,mensajeError);
                    }else{
                        System.out.println(response.errorBody().toString());
                        Toast.makeText(ActividadDatosPersonal.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ImgurObject> call, Throwable t) {
                    Toast.makeText(ActividadDatosPersonal.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
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
                perfil.setImageBitmap(bitmap);
                // Obtener la URI de la imagen
                medias.setImageUri(uri);
                imagenGaleria= true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @NonNull
    private View.OnClickListener getOnClickListener(AlertDialog dialog, EnumPerfil resourceAvatar) {
        return view1 -> {
            perfil.setImageResource(resourceAvatar.REFERENCIA_DRAWABLE);

            // Obtener la URI de la imagen
            medias.setImageUri(Uri.parse(resourceAvatar.URL));
            perfil_img_url = resourceAvatar.getURL();
            imagenGaleria= false;

            dialog.dismiss();
        };
    }

}
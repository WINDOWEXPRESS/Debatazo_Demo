package com.example.debatazo.usuario.datospersonal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.debatazo.R;
import com.example.debatazo.utils.BrilloUtils;
import com.example.debatazo.databinding.ActividadDatosPersonalBinding;

import com.example.debatazo.usuario.EnumPerfil;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModel;
import com.example.debatazo.usuario.iniciarsesion.ui.login.LoginViewModelFactory;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActividadDatosPersonal extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ActividadDatosPersonalBinding binding;
    private TextView limiteNumerico;
    private TextView id;
    private TextView nombreUsuario;
    private TextView nombrePersonal;
    private EditText fecha;
    private Spinner sexo;
    private ImageView perfil;
    private EditText descripcionPersonal;
    private ImageButton volver;
    private LoginViewModel loginViewModel;
    private Button guardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActividadDatosPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vincularComponentes();

        //ajuste de brillo
        BrilloUtils.getInstancia().brilloAppObserver(this);

        mostrarInformacion();

        perfil.setOnClickListener(view -> mostrarDialogoPerfil());

        fecha.setOnClickListener(view -> showDatePickerDialog(this));

        volver.setOnClickListener(view -> finish());

        maximoCaracteres(descripcionPersonal, limiteNumerico);

        guardar.setOnClickListener(view -> {
            AlertDialog.Builder confirmar = new AlertDialog.Builder(ActividadDatosPersonal.this);
            confirmar.setTitle("Confirmar");
            confirmar.setMessage("Deseas guardar los cambios?");

            confirmar.setPositiveButton("Aceptar", (dialog, which) -> {
                // Acción a realizar al hacer clic en Aceptar
                dialog.dismiss(); // Cierra el AlertDialog
            });

            confirmar.setNegativeButton("Cancelar", (dialog, which) -> {
                // Acción a realizar al hacer clic en Cancelar
                dialog.dismiss(); // Cierra el AlertDialog
            });
            AlertDialog alertDialog = confirmar.create();
            alertDialog.show();
        });
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

    }
    private void mostrarInformacion() {
        // Crear una instancia del ViewModel utilizando un ViewModelProvider y una Factory personalizada
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        // Verificar si el usuario está autenticado y se ha obtenido información del usuario

        if (loginViewModel.getLoginRepository().getLoggedInUserLiveData().getValue() != null) {
            // Mostrar la información del usuario en las vistas correspondientes
            loginViewModel.getLoginRepository().getLoggedInUserLiveData().observe(this,loggedInUser -> {
                descripcionPersonal.setText(loggedInUser.getSelf());
                id.setText(loggedInUser.getUser_id());
                id.setTextColor(Color.BLACK);
                nombreUsuario.setText(loggedInUser.getUser_name());
                nombrePersonal.setText(loggedInUser.getFull_name());

                //fecha.setText(loginViewModel.getLoginRepository().getUser().getAge());
                String sexoObtenido = loggedInUser.getSex();
                mostrarSexo(sexoObtenido);

                // Cargar la imagen de perfil del usuario utilizando Picasso
                Picasso.get().load(loggedInUser.getProfile_img()).into(perfil);
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

        galeria.setOnClickListener(view1 -> abrirGaleria());
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
                perfil.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @NonNull
    private View.OnClickListener getOnClickListener(AlertDialog dialog, EnumPerfil resourceAvatar) {
        return view1 -> {
            perfil.setImageResource(resourceAvatar.REFERENCIA_DRAWABLE);
            dialog.dismiss();
        };
    }

}
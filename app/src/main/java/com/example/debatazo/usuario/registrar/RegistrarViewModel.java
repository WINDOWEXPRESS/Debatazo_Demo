package com.example.debatazo.usuario.registrar;

import android.util.Patterns;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.debatazo.R;
import com.example.debatazo.usuario.md5.SaltMD5Util;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public class RegistrarViewModel extends ViewModel {
    private static final int LONGITUD_MIN = 5;
    private static final int LONGITUD_MAX = 20;
    private RegistrarUsuarioRepo userRepository;
    private MutableLiveData<RegistrarFormulaEstado> registrarFormulaEstado = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public LiveData<RegistrarFormulaEstado> getRegistrarFormulaEstado() {
        return registrarFormulaEstado;
    }

    public RegistrarViewModel() {
        userRepository = new RegistrarUsuarioRepo();
    }

    // Método para registrar un usuario
    public void registrarUsuario(String email, String password, Callback<ResponseBody> callback) {
        //encriptar la contraseña en md5 Y QUE DENTRO DEMETODO TIENE UNA SAL CONSTANTE
        String passwordCodificado = SaltMD5Util.generateSaltPassword(password);
        //Guardar los datos en el pojo para pasarlo a repositorio
        RegistrarUsuarioPojo user = new RegistrarUsuarioPojo(email, passwordCodificado);

        userRepository.registrarUsuario(user,callback,loadingLiveData);
    }

    public void RegistrarDataChanged(String email, String contrasenia,String contraseniaRepetir,@Nullable Boolean esTerminoCheckeado) {
        // Verifica si el email de usuario es válido
        if (!esEmailValido(email)) {
            // Si el email de usuario no es válido, establece un estado de formulario de inicio de sesión inválido con un mensaje de error de nombre de usuario.
            registrarFormulaEstado.setValue(new RegistrarFormulaEstado(R.string.invalido_email_usuario, null, null,null));
        }
        // Verifica si la contraseña es válida
        else if (!esContraseniaValido(contrasenia) ) {
            // Si la contraseña no es válida, establece un estado de formulario de inicio de sesión inválido con un mensaje de error de contraseña.
            registrarFormulaEstado.setValue(new RegistrarFormulaEstado(null,R.string.invalido_contrasenia, null,null));
        } else if (!esContraseniaValido(contraseniaRepetir)) {
            registrarFormulaEstado.setValue(new RegistrarFormulaEstado(null,null,R.string.invalido_contrasenia,null));
        } else if (!contrasenia.equals(contraseniaRepetir)) {
            registrarFormulaEstado.setValue(new RegistrarFormulaEstado(null,null, null,R.string.error_registrar_contrasenia));
        }else if (Boolean.TRUE.equals(esTerminoCheckeado)) {
            // Establece un estado de formulario de inicio de sesión válido.
            registrarFormulaEstado.setValue(new RegistrarFormulaEstado(true));
        }else {
            registrarFormulaEstado.setValue(new RegistrarFormulaEstado(false));
        }

    }

    //Validacion email
    private boolean esEmailValido(String username) {
        if (username == null) {
            return false;
        }
        if (!username.contains("[@.]")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }
    // Validacion contraseña
    private boolean esContraseniaValido(String password) {
        return password != null && (password.trim().length() > LONGITUD_MIN && password.trim().length() < LONGITUD_MAX);
    }
}

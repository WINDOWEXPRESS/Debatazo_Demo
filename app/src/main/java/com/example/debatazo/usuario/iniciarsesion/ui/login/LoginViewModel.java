package com.example.debatazo.usuario.iniciarsesion.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.debatazo.R;
import com.example.debatazo.usuario.iniciarsesion.data.model.Token;
import com.example.debatazo.utils.GlobalFuntion;
import com.example.debatazo.utils.SharedPreferenceUtils;
import com.example.debatazo.usuario.iniciarsesion.data.LoginCallBack;
import com.example.debatazo.usuario.iniciarsesion.data.LoginRepository;
import com.example.debatazo.usuario.iniciarsesion.data.Result;
import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;

import java.util.Objects;

public class LoginViewModel extends ViewModel {

    private static final int LONGITUD_MIN = 4;
    private static final int LONGITUD_MAX = 20;

    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    private final LoginRepository loginRepository;

    public LoginRepository getLoginRepository() {
        return loginRepository;
    }

    public LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }


    /**
     * Método para iniciar sesión con un nombre de usuario y contraseña.
     * Este método puede ser lanzado en un trabajo asíncrono separado.
     *
     * @param email    Email de usuario.
     * @param password La contraseña proporcionada por el usuario.
     */
    public void login(String email, String password, Context context) {


        // Se realiza la autenticación y se obtiene el resultado
        loginRepository.login(email, password, context, loadingLiveData, new LoginCallBack() {
            @Override
            public Result<LoggedInUser> onSuccess(Result<LoggedInUser> user) {
                // Se verifica el resultado obtenido
                if (user instanceof Result.Success) {
                    // Si la autenticación fue exitosa, se obtiene el usuario autenticado
                    LoggedInUser data = ((Result.Success<LoggedInUser>) user).getData();
                    // Se actualiza el resultado del inicio de sesión con la información del usuario autenticado
                    loginResult.setValue(new LoginResult(new LoggedInUserView(data.getUser_name())));
                } else {
                        // Si la autenticación falló, se actualiza el resultado del inicio de sesión con un mensaje de error
                        loginResult.setValue(new LoginResult(R.string.inicia_sesion_fallido));

                }
                return user;
            }

            @Override
            public Result<LoggedInUser> onFailure(Result<LoggedInUser> mensajeError) {
                if(mensajeError.toString().contains("400")||mensajeError.toString().contains("404"))
                    loginResult.setValue(new LoginResult(R.string.inicia_sesion_fallido_por_error_emailcontra));
                return null;
            }
        });
    }

    public void updatePerfil(LoggedInUser user, TextView mensajeError) {

        // Se realiza la autenticación y se obtiene el resultado
        loginRepository.updatePerfil(user,loadingLiveData,mensajeError);
    }
    // Esta función valida la existencia de un token de instancia. Si el token de instancia existe, se inicia sesión automáticamente.
    public void autoLogin(Context context) {

        // Obtener una referencia al archivo de preferencias compartidas para almacenar y recuperar el token de autenticación.
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferenceUtils.PREFS_TOKEN, Context.MODE_PRIVATE);
        Boolean hasExpirate = GlobalFuntion.comparaTiempo(sharedPreferences.getString(SharedPreferenceUtils.EXPIRATION, ""));
        // Verificar si el token y el ID de usuario están disponibles en las preferencias compartidas
        if (!Objects.requireNonNull(sharedPreferences.getString(SharedPreferenceUtils.TOKEN_VALUE, "")).isEmpty() &&
                sharedPreferences.getInt(SharedPreferenceUtils.USER_ID, 0) != 0 && Boolean.TRUE.equals(hasExpirate)) {

            //Otiene correo y contraseña de usuario y hacer login
            sharedPreferences = context.getSharedPreferences(SharedPreferenceUtils.PREFS_CUENTA, Context.MODE_PRIVATE);
            login(
                    sharedPreferences.getString(SharedPreferenceUtils.EMAIL, ""),
                    sharedPreferences.getString(SharedPreferenceUtils.CONTRASENIA, ""),
                    context
            );
        }
    }

    /**
     * Método para verificar si los datos de inicio de sesión son válidos.
     *
     * @param email Email de usuario.
     * @param password Contraseña ingresada por el usuario.
     */
    public void loginDataChanged(String email, String password) {
        // Verifica si el nombre de usuario es válido
        if (!esEmailValido(email)) {
            // Si el nombre de usuario no es válido, establece un estado de formulario de inicio de sesión inválido con un mensaje de error de nombre de usuario.
            loginFormState.setValue(new LoginFormState(R.string.invalido_email_usuario, null));
        }
        // Verifica si la contraseña es válida
        if (!esPasswordValido(password)) {
            // Si la contraseña no es válida, establece un estado de formulario de inicio de sesión inválido con un mensaje de error de contraseña.
            loginFormState.setValue(new LoginFormState(null, R.string.invalido_contrasenia));
        }
        // Si tanto el nombre de usuario como la contraseña son válidos
        if (esEmailValido(email) && esPasswordValido(password)) {
            // Establece un estado de formulario de inicio de sesión válido.
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    public void recuperarPassword(String email, TextView mensaje) {
        loginRepository.recuperarPassword(email,loadingLiveData,mensaje);
    }
    public void cambiarPassword(String token,int id ,String password,TextView mensaje) {
        loginRepository.cambiarPassword(token, id ,password,loadingLiveData,mensaje);
    }


    public void logout(Context context) {
        loginRepository.logout(context);
    }

    // Una comprobación de validación del nombre de usuario
    public boolean esEmailValido(String email) {
        if (email == null) {
            return false;
        }
        if (!email.contains("[@.]")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return !email.trim().isEmpty();
        }
    }

    // Un comprobación de validación de contraseña
    public boolean esPasswordValido(String password) {
        return password != null && (password.trim().length() > LONGITUD_MIN && password.trim().length() < LONGITUD_MAX);
    }
}
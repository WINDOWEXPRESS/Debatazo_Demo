package com.example.debatazo.usuario.iniciarsesion.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Patterns;

import com.example.debatazo.usuario.iniciarsesion.data.LoginCallBack;
import com.example.debatazo.usuario.iniciarsesion.data.LoginRepository;
import com.example.debatazo.usuario.iniciarsesion.data.Result;
import com.example.debatazo.usuario.iniciarsesion.data.model.LoggedInUser;
import com.example.debatazo.R;

public class LoginViewModel extends ViewModel {

    private static final int LONGITUD_MIN = 4;
    private static final int LONGITUD_MAX = 20;
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
    private LoginRepository loginRepository;


    public LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
    public LoginRepository getLoginRepository() {
        return loginRepository;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    /**
     * Método para iniciar sesión con un nombre de usuario y contraseña.
     * Este método puede ser lanzado en un trabajo asíncrono separado.
     *
     * @param email Email de usuario.
     * @param password La contraseña proporcionada por el usuario.
     */
    public void login(String email, String password,Context context) {
        // Se realiza la autenticación y se obtiene el resultado
                loginRepository.login(email, password,context,loadingLiveData, new LoginCallBack() {
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
                loginResult.setValue(new LoginResult(R.string.inicia_sesion_fallido));
                return null;
            }
        });
    }


    /**
     * Método para verificar si los datos de inicio de sesión son válidos.
     *
     * @param email Email de usuario.
     * @param password Contraseña ingresada por el usuario.
     */
    public void loginDataChanged(String email, String password) {
        // Verifica si el nombre de usuario es válido
        if (!isEmailValid(email)) {
            // Si el nombre de usuario no es válido, establece un estado de formulario de inicio de sesión inválido con un mensaje de error de nombre de usuario.
            loginFormState.setValue(new LoginFormState(R.string.invalido_email_usuario, null));
        }
        // Verifica si la contraseña es válida
        else if (!isPasswordValid(password)) {
            // Si la contraseña no es válida, establece un estado de formulario de inicio de sesión inválido con un mensaje de error de contraseña.
            loginFormState.setValue(new LoginFormState(null, R.string.invalido_contrasenia));
        }
        // Si tanto el nombre de usuario como la contraseña son válidos
        else {
            // Establece un estado de formulario de inicio de sesión válido.
            loginFormState.setValue(new LoginFormState(true));
        }
    }


    // A placeholder username validation check
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (!email.contains("[@.]")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return !email.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && (password.trim().length() > LONGITUD_MIN && password.trim().length() < LONGITUD_MAX);
    }
}
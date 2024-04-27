package com.example.debatazo.token.usuario.registrar;

import androidx.annotation.Nullable;

public class RegistrarFormulaEstado {

    /**
     * Data validation state of the login form.
     */

    @Nullable
    private Integer emailError;
    @Nullable
    private Integer contraseniaError;
    @Nullable
    private Integer contraseniaRepetidoError;
    @Nullable
    private Integer contraseniaNoCoincideError;
    private boolean isDataValid;

    RegistrarFormulaEstado(@Nullable Integer emailError, @Nullable Integer contraseniaError,
                           @Nullable Integer contraseniaRepetidoError, @Nullable Integer contraseniaNoCoincideError) {
        this.emailError = emailError;
        this.contraseniaError = contraseniaError;
        this.contraseniaRepetidoError = contraseniaRepetidoError;
        this.contraseniaNoCoincideError = contraseniaNoCoincideError;
        this.isDataValid = false;
    }

    RegistrarFormulaEstado(boolean isDataValid) {
        this.emailError = null;
        this.contraseniaError = null;
        this.contraseniaRepetidoError = null;
        this.contraseniaNoCoincideError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    @Nullable
    Integer getContraseniaError() {
        return contraseniaError;
    }

    @Nullable
    Integer getContraseniaRepetidoError() {
        return contraseniaRepetidoError;
    }

    @Nullable
    Integer getContraseniaNoCoincideError() {
        return contraseniaNoCoincideError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
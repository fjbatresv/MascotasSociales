package com.jmbsystems.fjbatresv.mascotassociales.login.ui;

import java.util.Map;

/**
 * Created by javie on 4/07/2016.
 */
public interface LoginView {
    void handleSocialNetworks(boolean mostrar);
    void handleProgressbar(boolean mostrar, int barra);
    void handleMainLoginFields(boolean mostrar);
    void signInSuccess();
    void signInError(String error);
    void signUpSuccess();
    void signUpError(String error);
    void validSession(Map<String, Object> options);
}

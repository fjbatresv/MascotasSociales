package com.jmbsystems.fjbatresv.mascotassociales.login.ui;

/**
 * Created by javie on 4/07/2016.
 */
public interface LoginView {
    void handleSocialNetworks(boolean mostrar);
    void handleProgressbar(boolean mostrar);
    void handleMainLoginFields(boolean mostrar);
    void signInSuccess();
    void signInError(String error);
}

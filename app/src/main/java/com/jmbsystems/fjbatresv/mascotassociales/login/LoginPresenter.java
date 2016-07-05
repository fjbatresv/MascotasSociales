package com.jmbsystems.fjbatresv.mascotassociales.login;

import com.facebook.AccessToken;
import com.jmbsystems.fjbatresv.mascotassociales.login.events.LoginEvent;

import java.util.Map;

/**
 * Created by javie on 4/07/2016.
 */
public interface LoginPresenter {
    void handleMainLogin(boolean accion);
    void mainSignin(String email, String password);
    void fbSignin(AccessToken token);
    void twSignin(Map<String, String> options);
    void onEventMainThread(LoginEvent event);

    void onCreate();
    void onDestroy();
}

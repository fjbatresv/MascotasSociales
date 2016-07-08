package com.jmbsystems.fjbatresv.mascotassociales.login;

import android.util.Log;

import com.facebook.AccessToken;

import java.util.Map;

/**
 * Created by javie on 5/07/2016.
 */
public class LoginInteractorImplementation implements LoginInteractor {
    private LoginRepository repo;

    public LoginInteractorImplementation(LoginRepository repo) {
        this.repo = repo;
    }

    @Override
    public void mainSignin(String email, String password) {
        repo.mainSignin(email, password);
    }

    @Override
    public void mainSignup(String email, String password) {
        repo.mainSignup(email, password);
    }

    @Override
    public void fbSignin(AccessToken token) {
        repo.fbSignin(token);
    }

    @Override
    public void twSignin(Map<String, String> options) {

        Log.e("twlogin", "interactor received");
        repo.twSignin(options);
    }

    @Override
    public void validLogin() {
        repo.validLogin();
    }
}

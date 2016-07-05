package com.jmbsystems.fjbatresv.mascotassociales.login;

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
    public void fbSignin(AccessToken token) {
        repo.fbSignin(token);
    }

    @Override
    public void twSignin(Map<String, String> options) {
        repo.twSignin(options);
    }
}

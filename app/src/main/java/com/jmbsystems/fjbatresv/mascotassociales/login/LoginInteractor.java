package com.jmbsystems.fjbatresv.mascotassociales.login;

import com.facebook.AccessToken;

import java.util.Map;

/**
 * Created by javie on 5/07/2016.
 */
public interface LoginInteractor {
    void mainSignin(String email, String password);
    void fbSignin(AccessToken token);
    void twSignin(Map<String, String> options);
}

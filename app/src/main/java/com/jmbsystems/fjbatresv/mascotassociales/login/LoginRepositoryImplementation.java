package com.jmbsystems.fjbatresv.mascotassociales.login;

import com.facebook.AccessToken;
import com.firebase.client.FirebaseError;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseActionListenerCallback;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.login.events.LoginEvent;

import java.util.Map;

/**
 * Created by javie on 5/07/2016.
 */
public class LoginRepositoryImplementation implements LoginRepository {
    private EventBus bus;
    private FirebaseApi api;

    public LoginRepositoryImplementation(EventBus bus, FirebaseApi api) {
        this.bus = bus;
        this.api = api;
    }

    @Override
    public void mainSignin(String email, String password) {
        api.login(email, password, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                post(LoginEvent.SIGNIN_SUCCESS, null);
            }

            @Override
            public void onError(FirebaseError error) {
                post(LoginEvent.SIGNIN_ERROR, error.getMessage());
            }
        });
    }

    @Override
    public void fbSignin(AccessToken token) {
        api.loginFacebook(token, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                post(LoginEvent.SOCIAL_SIGNIN_SUCCESS, null);
            }

            @Override
            public void onError(FirebaseError error) {
                post(LoginEvent.SOCIAL_SIGNIN_ERROR, error.getMessage());
            }
        });
    }

    @Override
    public void twSignin(Map<String, String> options) {
        api.loginTwitter(options, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                post(LoginEvent.SOCIAL_SIGNIN_SUCCESS, null);
            }

            @Override
            public void onError(FirebaseError error) {
                post(LoginEvent.SOCIAL_SIGNIN_ERROR, error.getMessage());
            }
        });
    }

    private void post(int type, String error){
        bus.post(new LoginEvent(type, error));
    }
}

package com.jmbsystems.fjbatresv.mascotassociales.login;

import android.util.Log;

import com.facebook.AccessToken;
import com.firebase.client.FirebaseError;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseActionListenerCallback;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.domain.Util;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Session;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.login.events.LoginEvent;

import java.util.Map;
import java.util.Objects;

/**
 * Created by javie on 5/07/2016.
 */
public class LoginRepositoryImplementation implements LoginRepository {
    private EventBus bus;
    private FirebaseApi api;
    private Util util;

    public LoginRepositoryImplementation(EventBus bus, FirebaseApi api, Util util) {
        this.bus = bus;
        this.api = api;
        this.util = util;
    }

    @Override
    public void mainSignin(String email, String password) {
        api.login(email, password, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                Session.getInstancia().setSessionType(Session.SESSION_LOCAL);
                Map<String, Object> options = api.viewAuthData();
                Object value = options.get("email");
                Session.getInstancia().setImage(String.valueOf(util.getavatarUrl(String.valueOf(value))));
                Session.getInstancia().setUsername(String.valueOf(value));
                Session.getInstancia().setNombre(String.valueOf(value));
                post(LoginEvent.SIGNIN_SUCCESS, null, null);
            }

            @Override
            public void onError(FirebaseError error) {
                post(LoginEvent.SIGNIN_ERROR, error.getMessage(), null);
            }
        });
    }

    @Override
    public void mainSignup(final String email, final String password) {
        api.signup(email, password, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                post(LoginEvent.SIGNUP_SUCCESS, null, null);
                mainSignin(email, password);
            }

            @Override
            public void onError(FirebaseError error) {
                post(LoginEvent.SIGNUP_ERROR, error.getMessage(), null);
            }
        });
    }

    @Override
    public void fbSignin(AccessToken token) {
        api.loginFacebook(token, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                Session.getInstancia().setSessionType(Session.SESSION_FACEBOOK);
                Map<String, Object> options = api.viewAuthData();
                Object value = options.get("profileImageURL");
                Session.getInstancia().setImage(String.valueOf(value));
                value = options.get("id");
                Session.getInstancia().setUsername(String.valueOf(value));
                value = options.get("displayName");
                Log.e("nombre", value.toString());
                Session.getInstancia().setNombre(String.valueOf(value));
                post(LoginEvent.SOCIAL_SIGNIN_SUCCESS, null, null);
            }

            @Override
            public void onError(FirebaseError error) {
                post(LoginEvent.SOCIAL_SIGNIN_ERROR, error.getMessage(), null);
            }
        });
    }

    @Override
    public void twSignin(Map<String, String> options) {
        Log.e("twlogin", "repo received");
        api.loginTwitter(options, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                Log.e("twlogin", "repo auth");
                Map<String, Object> options = api.viewAuthData();
                Session.getInstancia().setSessionType(Session.SESSION_TWITTER);
                Object value = options.get("profileImageURL");
                Session.getInstancia().setImage(String.valueOf(value));
                value = options.get("username");
                Session.getInstancia().setUsername(String.valueOf(value));
                value = options.get("displayName");
                Log.e("nombre", value.toString());
                Session.getInstancia().setNombre(String.valueOf(value));
                post(LoginEvent.SOCIAL_SIGNIN_SUCCESS, null, null);
            }

            @Override
            public void onError(FirebaseError error) {
                post(LoginEvent.SOCIAL_SIGNIN_ERROR, error.getMessage(), null);
            }
        });
    }

    @Override
    public void validLogin() {
        api.checkForSession(new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                Map<String, Object> res = api.viewAuthData();
                post(LoginEvent.VALID_LOGIN, null, res);
            }

            @Override
            public void onError(FirebaseError error) {
                post(LoginEvent.VALID_LOGIN, "Sesión no iniciada", null);
            }
        });
    }

    private void post(int type, String error, Map<String, Object> options){
        bus.post(new LoginEvent(type, error, options));
    }
}

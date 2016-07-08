package com.jmbsystems.fjbatresv.mascotassociales.login;

import android.util.Log;

import com.facebook.AccessToken;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.login.events.LoginEvent;
import com.jmbsystems.fjbatresv.mascotassociales.login.ui.LoginView;

import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

/**
 * Created by javie on 5/07/2016.
 */
public class LoginPresenterImplementation implements LoginPresenter {
    private LoginView view;
    private LoginInteractor interactor;
    private EventBus bus;

    public LoginPresenterImplementation(LoginView view, LoginInteractor interactor, EventBus bus) {
        this.view = view;
        this.interactor = interactor;
        this.bus = bus;
    }

    @Override
    public void handleMainLogin(boolean accion) {
        view.handleSocialNetworks(!accion);
        view.handleMainLoginFields(accion);
    }

    @Override
    public void mainSignin(String email, String password) {
        view.handleProgressbar(true, 1);
        interactor.mainSignin(email, password);
    }

    @Override
    public void mainSignup(String email, String password) {
        interactor.mainSignup(email, password);
    }

    @Override
    public void fbSignin(AccessToken token) {
        view.handleProgressbar(true, 2);
        interactor.fbSignin(token);
    }

    @Override
    public void twSignin(Map<String, String> options) {
        view.handleProgressbar(true, 2);
        interactor.twSignin(options);
    }

    @Override
    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        switch (event.getType()){
            case LoginEvent.SIGNIN_SUCCESS:
                view.handleProgressbar(false, 1);
                view.signInSuccess();
                break;
            case LoginEvent.SIGNIN_ERROR:
                view.handleProgressbar(false, 1);
                view.signInError(event.getError());
                break;
            case LoginEvent.SIGNUP_SUCCESS:
                view.handleProgressbar(false, 1);
                view.signUpSuccess();
                break;
            case LoginEvent.SIGNUP_ERROR:
                view.handleProgressbar(false, 1);
                view.signUpError(event.getError());
                break;
            case LoginEvent.SOCIAL_SIGNIN_SUCCESS:
                view.handleProgressbar(false, 2);
                view.signInSuccess();
                break;
            case LoginEvent.SOCIAL_SIGNIN_ERROR:
                view.handleProgressbar(false, 2);
                view.signInError(event.getError());
                break;
            case LoginEvent.VALID_LOGIN:
                if (event.getError() == null){
                    view.validSession(event.getOptions());
                    view.signInSuccess();
                }
                break;
        }
    }

    @Override
    public void onCreate() {
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        view = null;
        bus.unRegister(this);
    }

    @Override
    public void validLogin() {
        interactor.validLogin();
    }
}

package com.jmbsystems.fjbatresv.mascotassociales.login;

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
        view.handleProgressbar(true);
        interactor.mainSignin(email, password);
    }

    @Override
    public void fbSignin(AccessToken token) {
        interactor.fbSignin(token);
    }

    @Override
    public void twSignin(Map<String, String> options) {
        interactor.twSignin(options);
    }

    @Override
    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        switch (event.getType()){
            case LoginEvent.SIGNIN_SUCCESS:
                view.handleProgressbar(false);
                view.signInSuccess();
                break;
            case LoginEvent.SIGNIN_ERROR:
                view.handleProgressbar(false);
                view.signInError(event.getError());
                break;
            case LoginEvent.SOCIAL_SIGNIN_SUCCESS:
                view.signInSuccess();
                break;
            case LoginEvent.SOCIAL_SIGNIN_ERROR:
                view.signInError(event.getError());
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
}

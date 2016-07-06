package com.jmbsystems.fjbatresv.mascotassociales.login.DI;

import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.domain.Util;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.login.LoginInteractor;
import com.jmbsystems.fjbatresv.mascotassociales.login.LoginInteractorImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.login.LoginPresenter;
import com.jmbsystems.fjbatresv.mascotassociales.login.LoginPresenterImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.login.LoginRepository;
import com.jmbsystems.fjbatresv.mascotassociales.login.LoginRepositoryImplementation;
import com.jmbsystems.fjbatresv.mascotassociales.login.ui.LoginView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by javie on 5/07/2016.
 */
@Module
public class LoginModule {
    private LoginView view;

    public LoginModule(LoginView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    LoginView providesLoginView(){
        return this.view;
    }

    @Provides
    @Singleton
    LoginPresenter providesLoginPresenter(LoginView loginView, EventBus eventBus, LoginInteractor interactor){
        return new LoginPresenterImplementation(loginView, interactor, eventBus);
    }

    @Provides
    @Singleton
    LoginInteractor providesLoginInteractor(LoginRepository repo){
        return new LoginInteractorImplementation(repo);
    }

    @Provides
    @Singleton
    LoginRepository providesLoginRepository(EventBus bus, FirebaseApi api, Util util){
        return new LoginRepositoryImplementation(bus, api, util);
    }
}

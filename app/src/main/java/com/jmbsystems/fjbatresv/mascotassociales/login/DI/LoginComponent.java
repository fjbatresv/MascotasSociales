package com.jmbsystems.fjbatresv.mascotassociales.login.DI;

import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesAppModule;
import com.jmbsystems.fjbatresv.mascotassociales.domain.DI.DomainModule;
import com.jmbsystems.fjbatresv.mascotassociales.libs.DI.LibsModule;
import com.jmbsystems.fjbatresv.mascotassociales.login.ui.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by javie on 5/07/2016.
 */
@Singleton
@Component(modules = {LoginModule.class, LibsModule.class, DomainModule.class, MascotasSocialesAppModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}

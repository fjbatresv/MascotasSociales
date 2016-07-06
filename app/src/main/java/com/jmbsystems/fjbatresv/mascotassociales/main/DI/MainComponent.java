package com.jmbsystems.fjbatresv.mascotassociales.main.DI;

import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesAppModule;
import com.jmbsystems.fjbatresv.mascotassociales.domain.DI.DomainModule;
import com.jmbsystems.fjbatresv.mascotassociales.libs.DI.LibsModule;
import com.jmbsystems.fjbatresv.mascotassociales.main.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by javie on 30/06/2016.
 */
@Singleton
@Component(modules = {MascotasSocialesAppModule.class, DomainModule.class, LibsModule.class, MainModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}

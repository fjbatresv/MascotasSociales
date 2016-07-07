package com.jmbsystems.fjbatresv.mascotassociales.photoMap.DI;

import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesApp;
import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesAppModule;
import com.jmbsystems.fjbatresv.mascotassociales.domain.DI.DomainModule;
import com.jmbsystems.fjbatresv.mascotassociales.libs.DI.LibsModule;
import com.jmbsystems.fjbatresv.mascotassociales.photoMap.ui.PhotoMapActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by javie on 7/07/2016.
 */
@Singleton
@Component(modules = {MascotasSocialesAppModule.class, DomainModule.class, LibsModule.class, PhotoMapModule.class})
public interface PhotoMapComponent {
    void inject(PhotoMapActivity activity);
}

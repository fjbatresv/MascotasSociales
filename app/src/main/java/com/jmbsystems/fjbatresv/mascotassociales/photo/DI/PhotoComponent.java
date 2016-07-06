package com.jmbsystems.fjbatresv.mascotassociales.photo.DI;

import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesAppModule;
import com.jmbsystems.fjbatresv.mascotassociales.domain.DI.DomainModule;
import com.jmbsystems.fjbatresv.mascotassociales.libs.DI.LibsModule;
import com.jmbsystems.fjbatresv.mascotassociales.photo.ui.PhotoActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by javie on 6/07/2016.
 */
@Singleton
@Component(modules = {MascotasSocialesAppModule.class, DomainModule.class, LibsModule.class, PhotoModule.class})
public interface PhotoComponent {
    void inject(PhotoActivity activity);
}

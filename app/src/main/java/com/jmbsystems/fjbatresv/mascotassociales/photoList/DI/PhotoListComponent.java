package com.jmbsystems.fjbatresv.mascotassociales.photoList.DI;

import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesApp;
import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesAppModule;
import com.jmbsystems.fjbatresv.mascotassociales.domain.DI.DomainModule;
import com.jmbsystems.fjbatresv.mascotassociales.libs.DI.LibsModule;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.ui.PhotoListActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by javie on 7/07/2016.
 */
@Singleton
@Component(modules = {MascotasSocialesAppModule.class, DomainModule.class, LibsModule.class, PhotoListModule.class})
public interface PhotoListComponent {
    void inject(PhotoListActivity activity);
}

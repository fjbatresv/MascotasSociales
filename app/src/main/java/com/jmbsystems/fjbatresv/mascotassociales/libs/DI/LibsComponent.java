package com.jmbsystems.fjbatresv.mascotassociales.libs.DI;

import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by javie on 29/06/2016.
 */
@Singleton
@Component(modules = {LibsModule.class, MascotasSocialesAppModule.class})
public interface LibsComponent {
}

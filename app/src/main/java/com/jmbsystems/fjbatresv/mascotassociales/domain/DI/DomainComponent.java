package com.jmbsystems.fjbatresv.mascotassociales.domain.DI;

import com.jmbsystems.fjbatresv.mascotassociales.MascotasSocialesAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by javie on 28/06/2016.
 */
@Singleton
@Component(modules = {DomainModule.class, MascotasSocialesAppModule.class})
public interface DomainComponent {

}

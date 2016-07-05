package com.jmbsystems.fjbatresv.mascotassociales;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by javie on 5/07/2016.
 */
@Module
public class MascotasSocialesAppModule {
    private MascotasSocialesApp app;

    public MascotasSocialesAppModule(MascotasSocialesApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application providesSocialPhotoMapApp(){
        return this.app;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application){
        return application.getSharedPreferences(
                "UserPrefs",
                Context.MODE_PRIVATE
        );
    }

    @Provides
    @Singleton
    Context provideApplicationContext(){
        return this.app.getApplicationContext();
    }
}

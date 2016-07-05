package com.jmbsystems.fjbatresv.mascotassociales.domain.DI;

import android.content.Context;
import android.location.Geocoder;

import com.firebase.client.Firebase;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.domain.Util;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by javie on 28/06/2016.
 */
@Module
public class DomainModule {
    private final static String FIREBASE_URL = "https://mascotas-sociales.firebaseio.com/";

    @Provides
    @Singleton
    FirebaseApi providesFirebaseApi(Firebase firebase){
        return new FirebaseApi(firebase);
    }

    @Provides
    @Singleton
    Firebase providesFirebase(){
        return new Firebase(FIREBASE_URL);
    }

    @Provides
    @Singleton
    Util providesUtil(Geocoder geocoder){
        return new Util(geocoder);
    }

    @Provides
    @Singleton
    Geocoder providesGeoCoder(Context context){
        return new Geocoder(context);
    }
}

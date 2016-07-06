package com.jmbsystems.fjbatresv.mascotassociales.main;

import android.location.Location;

import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Session;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageStorage;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageStorageFinishedListener;
import com.jmbsystems.fjbatresv.mascotassociales.main.events.MainEvent;

import java.io.File;

/**
 * Created by javie on 5/07/2016.
 */
public class MainRepositoryImplementation implements MainRepository {
    private FirebaseApi api;
    private EventBus bus;

    public MainRepositoryImplementation(EventBus bus, FirebaseApi api) {
        this.bus = bus;
        this.api = api;
    }

    @Override
    public void logout() {
        api.logout();
        post(MainEvent.LOGOUT, null);
    }


    private void post(int type, String error){
        bus.post(new MainEvent(type, error));
    }
}

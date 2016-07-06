package com.jmbsystems.fjbatresv.mascotassociales.photo;

import android.location.Location;

import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Session;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageStorage;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.ImageStorageFinishedListener;
import com.jmbsystems.fjbatresv.mascotassociales.main.events.MainEvent;
import com.jmbsystems.fjbatresv.mascotassociales.photo.events.PhotoEvent;

import java.io.File;

/**
 * Created by javie on 6/07/2016.
 */
public class PhotoRepositoryImplementation implements PhotoRepository {
    private FirebaseApi api;
    private ImageStorage storage;
    private EventBus bus;

    public PhotoRepositoryImplementation(FirebaseApi api, ImageStorage storage, EventBus bus) {
        this.api = api;
        this.storage = storage;
        this.bus = bus;
    }

    @Override
    public void uploadPhoto(Location location, String path, final Photo foto) {
        final String newFotoId = api.create();
        foto.setId(newFotoId);
        foto.setEmail(Session.getInstancia().getUsername() + "|" + String.valueOf(Session.getInstancia().getSessionType()));
        if (location != null){
            foto.setLatitude(location.getLatitude());
            foto.setLongitud(location.getLongitude());
        }
        post(PhotoEvent.UPLOAD_INIT, null);
        ImageStorageFinishedListener listener = new ImageStorageFinishedListener() {
            @Override
            public void onSuccess() {
                String url = storage.getImageUrl(newFotoId);
                foto.setUrl(url);
                api.update(foto);
                post(PhotoEvent.UPLOAD_COMPLETE, null);
            }

            @Override
            public void onError(String error) {
                post(PhotoEvent.UPLOAD_ERROR, error);
            }
        };
        storage.upload(new File(path), newFotoId, listener);
    }

    private void post(int type, String error){
        bus.post(new MainEvent(type, error));
    }
}

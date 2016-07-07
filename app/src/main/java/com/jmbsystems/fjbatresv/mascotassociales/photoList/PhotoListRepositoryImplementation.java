package com.jmbsystems.fjbatresv.mascotassociales.photoList;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseActionListenerCallback;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseEventListenerCallback;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Session;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.events.PhotoListEvent;

/**
 * Created by javie on 6/07/2016.
 */
public class PhotoListRepositoryImplementation implements PhotoListRepository {
    private FirebaseApi api;
    private EventBus bus;

    public PhotoListRepositoryImplementation(FirebaseApi api, EventBus bus) {
        this.api = api;
        this.bus = bus;
    }

    @Override
    public void subscribe() {
        api.cheackForData(new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {}

            @Override
            public void onError(FirebaseError error) {
                if (error != null){
                    bus.post(new PhotoListEvent(null, error.getMessage(), PhotoListEvent.READ_EVENT));
                }else {
                    bus.post(new PhotoListEvent(null, "", PhotoListEvent.READ_EVENT));
                }
            }
        });
        api.subscribe(new FirebaseEventListenerCallback() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                Photo foto = dataSnapshot.getValue(Photo.class);
                foto.setId(dataSnapshot.getKey());
                foto.setPublishedByMe(Session.getInstancia().getUsername().equals(foto.getEmail()));
                bus.post(new PhotoListEvent(foto, null, PhotoListEvent.READ_EVENT));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Photo foto = dataSnapshot.getValue(Photo.class);
                foto.setId(dataSnapshot.getKey());
                bus.post(new PhotoListEvent(foto, null, PhotoListEvent.DELETE_EVENT));
            }

            @Override
            public void onCancelled(FirebaseError error) {
                if (error != null){
                    bus.post(new PhotoListEvent(null, error.getMessage(), PhotoListEvent.READ_EVENT));
                }else{
                    bus.post(new PhotoListEvent(null, "", PhotoListEvent.READ_EVENT));
                }
            }
        });
    }

    @Override
    public void unSubscribe() {
        api.unSubscribe();
    }

    @Override
    public void removePhoto(final Photo foto) {
        api.remove(foto, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                bus.post(new PhotoListEvent(foto, null, PhotoListEvent.DELETE_EVENT));
            }

            @Override
            public void onError(FirebaseError error) {
                if (error != null){
                    bus.post(new PhotoListEvent(null, error.getMessage(), PhotoListEvent.DELETE_EVENT));
                }else{
                    bus.post(new PhotoListEvent(null, "", PhotoListEvent.DELETE_EVENT));
                }
            }
        });
    }


}

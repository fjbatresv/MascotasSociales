package com.jmbsystems.fjbatresv.mascotassociales.photoMap;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseActionListenerCallback;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseApi;
import com.jmbsystems.fjbatresv.mascotassociales.domain.FirebaseEventListenerCallback;
import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;
import com.jmbsystems.fjbatresv.mascotassociales.libs.base.EventBus;
import com.jmbsystems.fjbatresv.mascotassociales.photoMap.events.PhotoMapEvent;

/**
 * Created by javie on 7/07/2016.
 */
public class PhotoMapRepositoryImplementation implements PhotoMapRepository {
    private EventBus bus;
    private FirebaseApi api;

    public PhotoMapRepositoryImplementation(EventBus bus, FirebaseApi api) {
        this.bus = bus;
        this.api = api;
    }

    @Override
    public void subscribe() {
        api.cheackForData(new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(FirebaseError error) {
                if (error != null){
                    post(PhotoMapEvent.READ_EVENT, null, error.getMessage());
                }else{
                    post(PhotoMapEvent.READ_EVENT, null, "");
                }
            }
        });
        api.subscribe(new FirebaseEventListenerCallback() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                Photo foto = dataSnapshot.getValue(Photo.class);
                foto.setId(dataSnapshot.getKey());
                String email = api.getAuthEmail();
                foto.setPublishedByMe(email.equals(foto.getEmail()));
                post(PhotoMapEvent.READ_EVENT, foto, null);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Photo foto = dataSnapshot.getValue(Photo.class);
                foto.setId(dataSnapshot.getKey());
                post(PhotoMapEvent.DELETE_EVENT, foto, null);
            }

            @Override
            public void onCancelled(FirebaseError error) {
                if (error != null){
                    post(PhotoMapEvent.READ_EVENT, null, error.getMessage());
                }else{
                    post(PhotoMapEvent.READ_EVENT, null, "");
                }
            }
        });
    }

    @Override
    public void unSubscribe() {
        api.unSubscribe();
    }

    private void post(int type, Photo foto, String error){
        bus.post(new PhotoMapEvent(type, foto, error));
    }
}

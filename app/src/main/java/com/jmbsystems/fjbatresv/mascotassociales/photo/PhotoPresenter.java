package com.jmbsystems.fjbatresv.mascotassociales.photo;

import android.location.Location;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;
import com.jmbsystems.fjbatresv.mascotassociales.main.events.MainEvent;
import com.jmbsystems.fjbatresv.mascotassociales.photo.events.PhotoEvent;

/**
 * Created by javie on 6/07/2016.
 */
public interface PhotoPresenter {
    void onCreate();
    void onDestroy();
    void uploadPhoto(Location location, String path, Photo photo);
    void onEventMainThread(PhotoEvent event);
}

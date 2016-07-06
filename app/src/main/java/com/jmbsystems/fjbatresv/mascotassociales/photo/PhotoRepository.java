package com.jmbsystems.fjbatresv.mascotassociales.photo;

import android.location.Location;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;

/**
 * Created by javie on 6/07/2016.
 */
public interface PhotoRepository {
    void uploadPhoto(Location location, String path, Photo photo);
}

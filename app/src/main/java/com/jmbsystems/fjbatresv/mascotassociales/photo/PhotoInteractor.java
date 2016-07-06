package com.jmbsystems.fjbatresv.mascotassociales.photo;

import android.location.Location;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;

/**
 * Created by javie on 6/07/2016.
 */
public interface PhotoInteractor {
    void execute(Location location, String path, Photo photo);
}

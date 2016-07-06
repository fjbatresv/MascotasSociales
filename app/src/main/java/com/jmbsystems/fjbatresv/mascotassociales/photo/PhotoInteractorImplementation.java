package com.jmbsystems.fjbatresv.mascotassociales.photo;

import android.location.Location;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;

/**
 * Created by javie on 6/07/2016.
 */
public class PhotoInteractorImplementation implements PhotoInteractor {
    private PhotoRepository repo;

    public PhotoInteractorImplementation(PhotoRepository repo) {
        this.repo = repo;
    }

    @Override
    public void execute(Location location, String path, Photo photo) {
        repo.uploadPhoto(location, path, photo);
    }
}

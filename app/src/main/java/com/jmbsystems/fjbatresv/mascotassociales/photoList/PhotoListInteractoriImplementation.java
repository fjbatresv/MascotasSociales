package com.jmbsystems.fjbatresv.mascotassociales.photoList;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;

/**
 * Created by javie on 6/07/2016.
 */
public class PhotoListInteractoriImplementation implements PhotoListInteractor {
    private PhotoListRepository repository;

    public PhotoListInteractoriImplementation(PhotoListRepository repository) {
        this.repository = repository;
    }

    @Override
    public void subscribe() {
        repository.subscribe();
    }

    @Override
    public void unSubscribe() {
        repository.unSubscribe();
    }

    @Override
    public void removePhoto(Photo foto) {
        repository.removePhoto(foto);
    }
}

package com.jmbsystems.fjbatresv.mascotassociales.photoMap;

/**
 * Created by javie on 7/07/2016.
 */
public class PhotoMapInteractorImplemtation implements PhotoMapInteractor {
    private PhotoMapRepository repo;

    public PhotoMapInteractorImplemtation(PhotoMapRepository repo) {
        this.repo = repo;
    }

    @Override
    public void subscribe() {
        repo.subscribe();
    }

    @Override
    public void unSubscribe() {
        repo.unSubscribe();
    }
}

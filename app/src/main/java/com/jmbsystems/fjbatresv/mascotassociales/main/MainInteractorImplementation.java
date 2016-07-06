package com.jmbsystems.fjbatresv.mascotassociales.main;

import android.location.Location;

/**
 * Created by javie on 5/07/2016.
 */
public class MainInteractorImplementation implements MainInteractor {
    private MainRepository repo;

    public MainInteractorImplementation(MainRepository repo) {
        this.repo = repo;
    }

    @Override
    public void logout() {
        repo.logout();
    }
}

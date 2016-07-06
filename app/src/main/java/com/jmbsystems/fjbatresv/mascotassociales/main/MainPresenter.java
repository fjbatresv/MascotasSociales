package com.jmbsystems.fjbatresv.mascotassociales.main;

import android.location.Location;

import com.jmbsystems.fjbatresv.mascotassociales.main.events.MainEvent;

/**
 * Created by javie on 5/07/2016.
 */
public interface MainPresenter {
    void onCreate();
    void onDestroy();
    void logout();
    void onEventMainThread(MainEvent event);
}

package com.jmbsystems.fjbatresv.mascotassociales.photoMap;

import com.jmbsystems.fjbatresv.mascotassociales.photoMap.events.PhotoMapEvent;

/**
 * Created by javie on 6/07/2016.
 */
public interface PhotoMapPresenter {
    void onCreate();
    void onDestroy();

    void subscribe();

    void onEventMainThread(PhotoMapEvent event);
}

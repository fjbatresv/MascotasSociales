package com.jmbsystems.fjbatresv.mascotassociales.photoList;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;
import com.jmbsystems.fjbatresv.mascotassociales.photoList.events.PhotoListEvent;

/**
 * Created by javie on 6/07/2016.
 */
public interface PhotoListPresenter {
    void onCreate();
    void onDestroy();

    void removePhoto(Photo foto);
    void onEventMainThread(PhotoListEvent event);
}

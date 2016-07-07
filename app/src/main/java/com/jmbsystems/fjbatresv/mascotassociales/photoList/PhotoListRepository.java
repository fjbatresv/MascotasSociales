package com.jmbsystems.fjbatresv.mascotassociales.photoList;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;

/**
 * Created by javie on 6/07/2016.
 */
public interface PhotoListRepository {
    void subscribe();
    void unSubscribe();

    void removePhoto(Photo foto);
}

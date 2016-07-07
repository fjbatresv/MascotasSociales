package com.jmbsystems.fjbatresv.mascotassociales.photoMap.ui;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;

/**
 * Created by javie on 6/07/2016.
 */
public interface PhotoMapView {
    void addPhoto(Photo foto);
    void removePhoto(Photo foto);
    void onPhotosError(String error);
    void loading(boolean accion);
}

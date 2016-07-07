package com.jmbsystems.fjbatresv.mascotassociales.photoList.ui;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;

import java.util.List;

/**
 * Created by javie on 6/07/2016.
 */
public interface PhotoListView {
    void onError(String error);
    void toggleContent(boolean mostrar);
    void toggleProgress(boolean mostrar);

    void addPhoto(Photo foto);
    void removePhoto(Photo foto);
}

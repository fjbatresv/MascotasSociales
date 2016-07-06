package com.jmbsystems.fjbatresv.mascotassociales.photo.ui;

/**
 * Created by javie on 6/07/2016.
 */
public interface PhotoView {
    void onUploadInit();
    void onUploadComplete();
    void onUploadError(String error);
    void handleProgressBar(boolean mostrar);
}

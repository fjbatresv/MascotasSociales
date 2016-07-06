package com.jmbsystems.fjbatresv.mascotassociales.photoList.ui.adapters;

import android.widget.ImageView;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;

/**
 * Created by javie on 6/07/2016.
 */
public interface OnItemClickListener {
    void onShareClick(Photo foto, ImageView img);
    void onDeleteClick(Photo foto);
}

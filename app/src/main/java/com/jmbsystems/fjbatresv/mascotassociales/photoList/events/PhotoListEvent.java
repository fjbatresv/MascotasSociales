package com.jmbsystems.fjbatresv.mascotassociales.photoList.events;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;

import java.util.List;

/**
 * Created by javie on 6/07/2016.
 */
public class PhotoListEvent {
    private Photo foto;
    private String error;
    private int type;

    public final static int READ_EVENT = 0;
    public final static int DELETE_EVENT = 1;

    public PhotoListEvent() {
    }

    public PhotoListEvent(Photo foto, String error, int type) {
        this.foto = foto;
        this.error = error;
        this.type = type;
    }

    public Photo getFoto() {
        return foto;
    }

    public void setFoto(Photo foto) {
        this.foto = foto;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

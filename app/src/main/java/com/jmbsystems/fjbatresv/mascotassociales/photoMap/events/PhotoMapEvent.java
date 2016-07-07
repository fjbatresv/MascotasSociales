package com.jmbsystems.fjbatresv.mascotassociales.photoMap.events;

import com.jmbsystems.fjbatresv.mascotassociales.enitites.Photo;

/**
 * Created by javie on 7/07/2016.
 */
public class PhotoMapEvent {
    private int type;
    private Photo foto;
    private String error;

    public final static int READ_EVENT = 0;
    public final static int DELETE_EVENT = 1;

    public PhotoMapEvent() {
    }

    public PhotoMapEvent(int type, Photo foto, String error) {
        this.type = type;
        this.foto = foto;
        this.error = error;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
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
}

package com.jmbsystems.fjbatresv.mascotassociales.photo.events;

/**
 * Created by javie on 6/07/2016.
 */
public class PhotoEvent {
    private int type;
    private String error;
    public static final int  UPLOAD_INIT = 0;
    public static final int  UPLOAD_COMPLETE = 1;
    public static final int  UPLOAD_ERROR = 2;

    public PhotoEvent() {
    }

    public PhotoEvent(int type, String error) {
        this.type = type;
        this.error = error;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

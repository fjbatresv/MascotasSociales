package com.jmbsystems.fjbatresv.mascotassociales.main.events;

/**
 * Created by javie on 5/07/2016.
 */
public class MainEvent {
    public static final int LOGOUT = 3;
    private int type;
    private String error;

    public MainEvent() {
    }

    public MainEvent(int type, String error) {
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

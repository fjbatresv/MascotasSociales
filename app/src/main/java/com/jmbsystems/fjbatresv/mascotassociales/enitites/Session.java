package com.jmbsystems.fjbatresv.mascotassociales.enitites;

/**
 * Created by javie on 5/07/2016.
 */

public class Session {
    public final static int SESSION_LOCAL = 0;
    public final static int SESSION_FACEBOOK = 1;
    public final static int SESSION_TWITTER = 2;

    private int sessionType = SESSION_LOCAL;
    private String nombre;
    private String username;
    private String image;

    private static Session instancia;

    public static Session getInstancia(){
        if (instancia == null){
            instancia = new Session();
        }
        return instancia;
    }

    public Session() {
    }

    public Session(int sessionType, String nombre, String username, String image) {
        this.sessionType = sessionType;
        this.nombre = nombre;
        this.username = username;
        this.image = image;
    }

    public int getSessionType() {
        return sessionType;
    }

    public void setSessionType(int sessionType) {
        this.sessionType = sessionType;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static void setInstancia(Session instancia) {
        Session.instancia = instancia;
    }
}

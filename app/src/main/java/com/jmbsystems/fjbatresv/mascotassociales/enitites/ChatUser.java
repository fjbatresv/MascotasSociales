package com.jmbsystems.fjbatresv.mascotassociales.enitites;

/**
 * Created by javie on 7/07/2016.
 */
public class ChatUser {
    private String nombre;
    private String username;
    private String image;

    public ChatUser() {
    }

    public ChatUser(String nombre, String username, String image) {
        this.nombre = nombre;
        this.username = username;
        this.image = image;
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

    @Override
    public boolean equals(Object o) {
        boolean respuesta = false;
        if (o instanceof ChatUser){
            ChatUser tmp = (ChatUser) o;
            respuesta = tmp.getUsername().equals(this.getUsername());
        }
        return respuesta;
    }
}

package com.jmbsystems.fjbatresv.mascotassociales.enitites;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by javie on 5/07/2016.
 */
public class Photo {
    @JsonIgnore
    private String id;
    @JsonIgnore
    private boolean publishedByMe;
    private String url;
    private String email;
    private double latitude;
    private double longitud;
    private String tags;
    private String comentario;

    public Photo() {
    }

    public Photo(String id, boolean publishedByMe, String url, String email, double latitude, double longitud) {
        this.id = id;
        this.publishedByMe = publishedByMe;
        this.url = url;
        this.email = email;
        this.latitude = latitude;
        this.longitud = longitud;
    }

    public Photo(String url, String email, double latitude, double longitud) {
        this.url = url;
        this.email = email;
        this.latitude = latitude;
        this.longitud = longitud;
    }

    public Photo(String tags, String comentario) {
        this.tags = tags;
        this.comentario = comentario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPublishedByMe() {
        return publishedByMe;
    }

    public void setPublishedByMe(boolean publishedByMe) {
        this.publishedByMe = publishedByMe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}

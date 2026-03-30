package com.miapp.agentegamer.data.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GameDto {

    private int id;
    private String name;
    private double precioEstimado;
    private List<PlatformWrapper> platforms;
    private String plataformasTexto;

    @SerializedName("released")
    private String releaseDate;

    @SerializedName("background_image")
    private String imageUrl;

    private double rating;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate(){
        return releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setPrecioEstimado(double precioEstimado) {
        this.precioEstimado = precioEstimado;
    }

    public double getPrecioEstimado() {
        return precioEstimado;
    }

    public List<PlatformWrapper> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<PlatformWrapper> platforms) {
        this.platforms = platforms;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlataformasTexto(String texto) {
        this.plataformasTexto = texto;
    }

    public String getPlataformasTexto(){
        return plataformasTexto;
    }

    public class PlatformWrapper {
        private Platform platform;

        public Platform getPlatform() {
            return platform;
        }

        public void setPlatform(Platform platform) {
            this.platform = platform;
        }
    }

    public class Platform {
        private String name;
        private String slug;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }
    }

}

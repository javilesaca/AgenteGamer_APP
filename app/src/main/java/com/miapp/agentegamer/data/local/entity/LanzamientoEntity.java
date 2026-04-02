package com.miapp.agentegamer.data.local.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "lanzamientos", indices = {@Index(value = {"fechaLanzamiento"})})
public class LanzamientoEntity {

    @PrimaryKey
    public int gameId;

    public String nombre;
    public long fechaLanzamiento;
    public double precioEstimado;
    public String imageUrl;
    public String plataformas;
    public double rating;

    public LanzamientoEntity(int gameId, String nombre, long fechaLanzamiento, double precioEstimado,
                             String imageUrl, String plataformas, double rating) {
        this.gameId = gameId;
        this.nombre = nombre;
        this.fechaLanzamiento = fechaLanzamiento;
        this.precioEstimado = precioEstimado;
        this.imageUrl = imageUrl != null ? imageUrl : "";
        this.plataformas = plataformas != null ? plataformas : "";
        this.rating = rating;
    }

    public int getGameId() {
        return gameId;
    }

    public String getNombre() {
        return nombre;
    }

    public long getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public double getPrecioEstimado() {
        return precioEstimado;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPlataformas() {
        return plataformas;
    }

    public double getRating() {
        return rating;
    }
}

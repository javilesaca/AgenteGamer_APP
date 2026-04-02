package com.miapp.agentegamer.data.local.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "wishlist", indices = {
    @Index(value = {"userId"}),
    @Index(value = {"userId", "gameId"}, unique = true)
})
public class WishlistEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String userId;
    private int gameId;
    private String nombre;
    private String fechaLanzamiento;
    private String imagenUrl;
    private String plataforma;
    private double precioEstimado;



    public WishlistEntity(String userId, int gameId, String nombre, String fechaLanzamiento, String imagenUrl, String plataforma, double precioEstimado) {
        this.userId = userId;
        this.gameId = gameId;
        this.nombre = nombre;
        this.fechaLanzamiento = fechaLanzamiento;
        this.imagenUrl = imagenUrl;
        this.plataforma = plataforma;
        this.precioEstimado = precioEstimado;
    }

    //Getters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getGameId() {
        return gameId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public double getPrecioEstimado() {
        return precioEstimado;
    }

    public String getPlataforma() { return plataforma; }

    public void setPlataforma(String plataforma) { this.plataforma = plataforma; }
}

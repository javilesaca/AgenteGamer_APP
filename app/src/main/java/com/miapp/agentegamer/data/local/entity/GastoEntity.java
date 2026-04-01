package com.miapp.agentegamer.data.local.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "gastos", indices = {@Index(value = {"mes", "anio"})})
public class GastoEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nombreJuego;
    private double precio;
    private long fecha;  // Guardada en milisegundos
    private int mes;
    private int anio;
    private String imagenUrl;

    // Constructor
    public GastoEntity(String nombreJuego, double precio, long fecha, String imagenUrl) {
        this.nombreJuego = nombreJuego;
        this.precio = precio;
        this.fecha = fecha;
        this.imagenUrl = imagenUrl;

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(fecha);

        this.mes = cal.get(Calendar.MONTH) +1;
        this.anio = cal.get(Calendar.YEAR);
    }

    // GETTERS Y SETTERS
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNombreJuego() { return nombreJuego; }

    public void setNombreJuego(String nombreJuego) { this.nombreJuego = nombreJuego; }

    public double getPrecio() { return precio; }

    public void setPrecio(double precio) { this.precio = precio; }

    public long getFecha() { return fecha; }

    public void setFecha(long fecha) {
        this.fecha = fecha;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(fecha);
        this.mes = cal.get(Calendar.MONTH) + 1;
        this.anio = cal.get(Calendar.YEAR);
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes){
        this.mes = mes;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getAnio() {
        return anio;
    }

    public String getImagenUrl() { return imagenUrl; }

    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

}

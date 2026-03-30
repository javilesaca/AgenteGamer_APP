package com.miapp.agentegamer.data.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;

public class UsuarioEntity {
    private String email;
    private String nombre;

    @PropertyName("presupuestoMensual")
    private Double presupuestoMensual;

    private Timestamp fechaCreacion;
    private String rol;

    // Required empty constructor for Firestore
    public UsuarioEntity() {}

    // Getters
    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    @PropertyName("presupuestoMensual")
    public Double getPresupuestoMensual() {
        return presupuestoMensual;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public String getRol() {
        return rol;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @PropertyName("presupuestoMensual")
    public void setPresupuestoMensual(Double presupuestoMensual) {
        this.presupuestoMensual = presupuestoMensual;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

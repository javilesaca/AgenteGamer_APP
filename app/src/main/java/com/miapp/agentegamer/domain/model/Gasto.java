package com.miapp.agentegamer.domain.model;

public class Gasto {

    private final String id;
    private final String nombre;
    private final double precio;

    public Gasto(String id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public Gasto(String nombre, double precio) {
        this(null, nombre, precio);
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
}

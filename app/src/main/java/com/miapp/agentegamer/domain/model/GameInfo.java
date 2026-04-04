package com.miapp.agentegamer.domain.model;

/**
 * Representa la información básica de un juego para cálculos financieros.
 * 
 * <p>Esta clase es un objeto de valor inmutable que encapsulate los datos
 * necesarios para estimar precios y evaluar compras. Se utiliza en el sistema
 * financiero para determinar rangos de precio basados en la calificación
 * del juego.</p>
 * 
 * <p>Los atributos principales son el nombre del juego y su calificación
 * (rating), los cuales determinan la estimación de precio realizada por
 * {@link SistemaFinanciero#estimarPrecio(GameInfo)}.</p>
 * 
 * @author AgenteGamer
 * @version 1.0
 * @since 2024
 * @see SistemaFinanciero
 */
public class GameInfo {

    /** Nombre oficial del juego. No puede ser null ni estar vacío. */
    private final String name;
    
    /** Calificación del juego en escala de 0 a 5. Valores mayores a 4.5 se consideran tinggi. */
    private final double rating;

    /**
     * Constructor con información del juego.
     * 
     * @param name   Nombre del juego
     * @param rating Calificación del juego (0-5)
     */
    public GameInfo(String name, double rating) {
        this.name = name;
        this.rating = rating;
    }

    /**
     * Obtiene el nombre del juego.
     * 
     * @return Nombre del juego
     */
    public String getName() { return name; }
    
    /**
     * Obtiene la calificación del juego.
     * 
     * @return Rating del juego en escala de 0 a 5
     */
    public double getRating() { return rating; }
}

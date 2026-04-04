package com.miapp.agentegamer.domain.model;

/**
 * Representa un gasto o transacción de un juego en el sistema financiero.
 * 
 * <p>Esta clase es un objeto de valor inmutable que encapsulate la información
 * básica de un gasto: identificador único, nombre del juego y precio.</p>
 * 
 * <p>Se utiliza principalmente en el cálculo del presupuesto mensual y en la
 * evaluación de compras dentro del sistema de gestión financiera.</p>
 * 
 * @author AgenteGamer
 * @version 1.0
 * @since 2024
 */
public class Gasto {

    /** Identificador único del gasto en el sistema. Puede ser null si aún no se ha persistido. */
    private final String id;
    
    /** Nombre del juego o elemento asociado al gasto. */
    private final String nombre;
    
    /** Precio del gasto en euros. Valor positivo mayor a cero. */
    private final double precio;

    /**
     * Constructor completo con todos los parámetros.
     * 
     * @param id     Identificador único del gasto (puede ser null)
     * @param nombre Nombre del juego o elemento
     * @param precio Precio del gasto en euros
     */
    public Gasto(String id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    /**
     * Constructor sin ID para crear un nuevo gasto antes de persistirlo.
     * El ID será null hasta que el repositorio le asigne uno.
     * 
     * @param nombre Nombre del juego o elemento
     * @param precio Precio del gasto en euros
     */
    public Gasto(String nombre, double precio) {
        this(null, nombre, precio);
    }

    /**
     * Obtiene el identificador único del gasto.
     * 
     * @return Identificador del gasto, o null si no ha sido persistido
     */
    public String getId() { return id; }
    
    /**
     * Obtiene el nombre del juego o elemento asociado al gasto.
     * 
     * @return Nombre del gasto
     */
    public String getNombre() { return nombre; }
    
    /**
     * Obtiene el precio del gasto en euros.
     * 
     * @return Precio del gasto
     */
    public double getPrecio() { return precio; }
}

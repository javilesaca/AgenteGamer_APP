package com.miapp.agentegamer.data.local.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Entidad de Room que representa un juego en la lista de deseos (wishlist).
 * <p>
 * Esta clase mapea la tabla {@code wishlist} de la base de datos SQLite local.
 * Cada instancia representa un juego que el usuario ha marcado como deseado,
 * almacenando información como el nombre, fecha de lanzamiento estimada, precio,
 * plataforma e imagen del juego.
 * <p>
 * La entidad implementa {@link Serializable} para permitir el paso de objetos
 * entre actividades o fragmentos. Está indexada por {@code userId} y por la
 * combinación única de {@code userId} y {@code gameId} para evitar duplicados.
 *
 * @see Entity
 * @see Serializable
 */
@Entity(tableName = "wishlist", indices = {
    @Index(value = {"userId"}),
    @Index(value = {"userId", "gameId"}, unique = true)
})
public class WishlistEntity implements Serializable {

    /** Clave primaria auto-generada para cada entrada de wishlist */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /** Identificador único del usuario en Firebase */
    private String userId;
    
    /** Identificador del juego en la API externa (RAWG) */
    private int gameId;
    
    /** Nombre del juego */
    private String nombre;
    
    /** Fecha de lanzamiento del juego (formato string) */
    private String fechaLanzamiento;
    
    /** URL de la imagen del juego */
    private String imagenUrl;
    
    /** Plataforma del juego (PC, PlayStation, Xbox, etc.) */
    private String plataforma;
    
    /** Precio estimado del juego */
    private double precioEstimado;


    /**
     * Constructor completo para crear una nueva entidad de wishlist.
     *
     * @param userId Identificador único del usuario en Firebase
     * @param gameId Identificador del juego en la API externa (RAWG)
     * @param nombre Nombre del juego
     * @param fechaLanzamiento Fecha de lanzamiento en formato string
     * @param imagenUrl URL de la imagen del juego
     * @param plataforma Plataforma del juego
     * @param precioEstimado Precio estimado del juego
     */
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

    /**
     * Obtiene el identificador único de la entrada en la wishlist.
     *
     * @return ID de la entrada en la base de datos
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único de la entrada.
     *
     * @param id Nuevo ID de la entrada
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el identificador del usuario.
     *
     * @return ID de usuario en Firebase
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Establece el identificador del usuario.
     *
     * @param userId Nuevo ID de usuario
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Obtiene el identificador del juego en la API externa.
     *
     * @return ID del juego en RAWG
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Obtiene el nombre del juego.
     *
     * @return Nombre del juego
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la fecha de lanzamiento del juego.
     *
     * @return Fecha de lanzamiento en formato string
     */
    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    /**
     * Obtiene la URL de la imagen del juego.
     *
     * @return URL de la imagen
     */
    public String getImagenUrl() {
        return imagenUrl;
    }

    /**
     * Obtiene el precio estimado del juego.
     *
     * @return Precio estimado
     */
    public double getPrecioEstimado() {
        return precioEstimado;
    }

    /**
     * Obtiene la plataforma del juego.
     *
     * @return Nombre de la plataforma
     */
    public String getPlataforma() { return plataforma; }

    /**
     * Establece la plataforma del juego.
     *
     * @param plataforma Nueva plataforma
     */
    public void setPlataforma(String plataforma) { this.plataforma = plataforma; }
}

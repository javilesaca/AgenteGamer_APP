package com.miapp.agentegamer.data.local.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidad de Room que representa un lanzamiento próximo de un juego.
 * <p>
 * Esta clase mapea la tabla {@code lanzamientos} de la base de datos SQLite local.
 * Cada instancia representa un juego que el usuario está siguiendo y que aún no
 * ha sido lanzado o está próximo a lanzarse. Almacena información como el nombre,
 * fecha de lanzamiento, precio estimado, plataformas disponibles y rating del juego.
 * <p>
 * La entidad está indexada por {@code fechaLanzamiento}, {@code userId} y por la
 * combinación única de {@code userId} y {@code gameId} para optimizar las consultas
 * y evitar duplicados.
 *
 * @see Entity
 */
@Entity(tableName = "lanzamientos", indices = {
    @Index(value = {"fechaLanzamiento"}),
    @Index(value = {"userId"}),
    @Index(value = {"userId", "gameId"}, unique = true)
})
public class LanzamientoEntity {

    /** Clave primaria auto-generada para cada lanzamiento */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /** Identificador único del usuario en Firebase */
    public String userId;
    
    /** Identificador del juego en la API externa (RAWG) */
    public int gameId;
    
    /** Nombre del juego */
    public String nombre;
    
    /** Fecha de lanzamiento en milisegundos (UNIX epoch) */
    public long fechaLanzamiento;
    
    /** Precio estimado del juego */
    public double precioEstimado;
    
    /** URL de la imagen del juego */
    public String imageUrl;
    
    /** Plataformas donde estará disponible el juego (separadas por coma) */
    public String plataformas;
    
    /** Puntuación o rating del juego */
    public double rating;

    /**
     * Constructor completo para crear una nueva entidad de lanzamiento.
     *
     * @param userId Identificador único del usuario en Firebase
     * @param gameId Identificador del juego en la API externa (RAWG)
     * @param nombre Nombre del juego
     * @param fechaLanzamiento Fecha de lanzamiento en milisegundos
     * @param precioEstimado Precio estimado del juego
     * @param imageUrl URL de la imagen del juego
     * @param plataformas Plataformas disponibles (separadas por coma)
     * @param rating Puntuación del juego
     */
    public LanzamientoEntity(String userId, int gameId, String nombre, long fechaLanzamiento, double precioEstimado,
                             String imageUrl, String plataformas, double rating) {
        this.userId = userId;
        this.gameId = gameId;
        this.nombre = nombre;
        this.fechaLanzamiento = fechaLanzamiento;
        this.precioEstimado = precioEstimado;
        this.imageUrl = imageUrl != null ? imageUrl : "";
        this.plataformas = plataformas != null ? plataformas : "";
        this.rating = rating;
    }

    /**
     * Obtiene el identificador único del lanzamiento.
     *
     * @return ID del lanzamiento en la base de datos
     */
    public int getId() {
        return id;
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
     * Obtiene la fecha de lanzamiento en milisegundos.
     *
     * @return Fecha en milisegundos (UNIX epoch)
     */
    public long getFechaLanzamiento() {
        return fechaLanzamiento;
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
     * Obtiene la URL de la imagen del juego.
     *
     * @return URL de la imagen
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Obtiene las plataformas del juego.
     *
     * @return Plataformas separadas por coma
     */
    public String getPlataformas() {
        return plataformas;
    }

    /**
     * Obtiene la puntuación (rating) del juego.
     *
     * @return Rating del juego
     */
    public double getRating() {
        return rating;
    }
}

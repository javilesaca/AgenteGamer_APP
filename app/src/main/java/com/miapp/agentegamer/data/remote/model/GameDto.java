package com.miapp.agentegamer.data.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Data Transfer Object (DTO) que representa un videojuego obtenido de la API de RAWG.
 * <p>
 * Esta clase actúa como mapeo entre la estructura JSON retornada por la API de RAWG
 * y el modelo de objetos utilizado en la aplicación. Contiene información básica
 * del juego como identificador, nombre, fecha de lanzamiento, imagen de portada,
 * rating y plataformas disponibles.
 * <p>
 * Los campos annotateados con {@link SerializedName} indican el nombre del campo
 * en la respuesta JSON de la API que no coincide con el nombre Java.
 *
 * @see GamesResponse
 */
public class GameDto {

    /** Identificador único del juego en la base de datos de RAWG */
    private int id;
    
    /** Nombre oficial del videojuego */
    private String name;
    
    /** Precio estimado del juego (calculado internamente por la app, no viene de la API) */
    private double precioEstimado;
    
    /** Lista de plataformas donde está disponible el juego */
    private List<PlatformWrapper> platforms;
    
    /** Texto descriptivo de las plataformas (generado internamente) */
    private String plataformasTexto;

    /** Fecha de lanzamiento del juego (formato ISO 8601: YYYY-MM-DD) */
    @SerializedName("released")
    private String releaseDate;

    /** URL de la imagen de portada del juego */
    @SerializedName("background_image")
    private String imageUrl;

    /** Puntuación media del juego en RAWG (escala 0-5) */
    private double rating;


    /**
     * Obtiene el identificador único del juego.
     * @return ID del juego en RAWG
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del juego.
     * @return Nombre del videojuegos
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la fecha de lanzamiento del juego.
     * @return Fecha de lanzamiento en formato YYYY-MM-DD, o null si no disponible
     */
    public String getReleaseDate(){
        return releaseDate;
    }

    /**
     * Obtiene la URL de la imagen de portada del juego.
     * @return URL de la imagen, o null si no tiene imagen
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Obtiene el rating del juego.
     * @return Puntuación del juego (típicamente entre 0 y 5)
     */
    public double getRating() {
        return rating;
    }

    /**
     * Establece el precio estimado del juego.
     * @param precioEstimado Precio estimado a asignar
     */
    public void setPrecioEstimado(double precioEstimado) {
        this.precioEstimado = precioEstimado;
    }

    /**
     * Obtiene el precio estimado del juego.
     * @return Precio estimado
     */
    public double getPrecioEstimado() {
        return precioEstimado;
    }

    /**
     * Obtiene la lista de plataformas del juego.
     * @return Lista de PlatformWrapper conteniendo las plataformas
     */
    public List<PlatformWrapper> getPlatforms() {
        return platforms;
    }

    /**
     * Establece la lista de plataformas del juego.
     * @param platforms Lista de plataformas a asignar
     */
    public void setPlatforms(List<PlatformWrapper> platforms) {
        this.platforms = platforms;
    }

    /**
     * Establece el nombre del juego.
     * @param name Nombre a asignar
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Establece el texto descriptivo de plataformas.
     * @param texto Texto descriptivo de las plataformas
     */
    public void setPlataformasTexto(String texto) {
        this.plataformasTexto = texto;
    }

    /**
     * Obtiene el texto descriptivo de plataformas.
     * @return Texto descriptivo de plataformas
     */
    public String getPlataformasTexto(){
        return plataformasTexto;
    }

    /**
     * Clase wrapper para representar una plataforma dentro de la respuesta de la API.
     * <p>
     * La API de RAWG retorna las plataformas anidadas dentro de un objeto wrapper,
     * por lo que esta clase intermedia es necesaria para el mapeo correcto.
     */
    public class PlatformWrapper {
        /** Plataforma propiamente dicha */
        private Platform platform;

        /**
         * Obtiene la plataforma.
         * @return Objeto Platform con los datos de la plataforma
         */
        public Platform getPlatform() {
            return platform;
        }

        /**
         * Establece la plataforma.
         * @param platform Plataforma a asignar
         */
        public void setPlatform(Platform platform) {
            this.platform = platform;
        }
    }

    /**
     * Clase que representa una plataforma de videojuegos.
     * <p>
     * Contiene el nombre legible de la plataforma y su identificador slug.
     */
    public class Platform {
        /** Nombre de la plataforma (ej: "PlayStation 5", "PC", "Xbox Series S/X") */
        private String name;
        
        /** Identificador slug de la plataforma (ej: "playstation5", "pc") */
        private String slug;

        /**
         * Obtiene el nombre de la plataforma.
         * @return Nombre de la plataforma
         */
        public String getName() {
            return name;
        }

        /**
         * Establece el nombre de la plataforma.
         * @param name Nombre a asignar
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Obtiene el slug de la plataforma.
         * @return Slug de la plataforma
         */
        public String getSlug() {
            return slug;
        }

        /**
         * Establece el slug de la plataforma.
         * @param slug Slug a asignar
         */
        public void setSlug(String slug) {
            this.slug = slug;
        }
    }

}

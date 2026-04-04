package com.miapp.agentegamer.data.remote.api;

import com.miapp.agentegamer.data.remote.model.GamesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interfaz de servicio para la API de RAWG (Videojuegos).
 * <p>
 * Define los endpoints disponibles para interactuar con la API de RAWG,
 * que proporciona información sobre videojuegos incluyendo títulos,
 * plataformas, fechas de lanzamiento, capturas de pantalla, géneros,
 * desarrolladores, editoriales y más.
 * <p>
 * Cada método representa una llamada HTTP GET a un endpoint específico
 * de la API. Los resultados se encapsulan en objetos {@link GamesResponse}
 * que contienen listas de {@link com.miapp.agentegamer.data.remote.model.GameDto}.
 *
 * @see retrofit2.Retrofit
 * @see GamesResponse
 * @see com.miapp.agentegamer.data.remote.model.GameDto
 */
public interface GamesApiService {

    /**
     * Obtiene una lista de videojuegos ordenados por rating.
     * <p>
     * Llama al endpoint /games de la API de RAWG y retorna los juegos
     * mejor valorados según su puntuación de rating.
     *
     * @param apiKey    Clave API de RAWG para autenticación
     * @param ordering  Criterio de ordenación (-rating para mayor rating primero)
     * @param pageSize  Número de resultados por página
     * @return          Llamada asíncrona que retorna {@link GamesResponse} con la lista de juegos
     */
    @GET("games")
    Call<GamesResponse> getGames(
        @Query("key") String apiKey,
        @Query("ordering") String ordering,
        @Query("page_size") int pageSize
    );

    /**
     * Busca videojuegos por texto en su nombre.
     * <p>
     * Realiza una búsqueda en la base de datos de RAWG buscando juegos
     * cuyo nombre contenga el texto proporcionado.
     *
     * @param apiKey    Clave API de RAWG para autenticación
     * @param texto     Texto a buscar en el nombre del juego
     * @param page      Número de página para paginación
     * @param pageSize  Número de resultados por página
     * @return          Llamada asíncrona que retorna {@link GamesResponse} con resultados de búsqueda
     */
    @GET("games")
    Call<GamesResponse> searchGames(
            @Query("key") String apiKey,
            @Query("search") String texto,
            @Query("page") int page,
            @Query("page_size") int pageSize
    );

    /**
     * Obtiene videojuegos próximos a lanzar en un rango de fechas.
     * <p>
     * Recupera juegos que tienen fecha de lanzamiento futura dentro
     * del rango de fechas especificado, ordenados según el criterio indicado.
     * Útil para ver los próximos lanzamientos de videojuegos.
     *
     * @param apiKey    Clave API de RAWG para autenticación
     * @param dates     Rango de fechas en formato "YYYY-MM-DD,YYYY-MM-DD"
     * @param ordering  Criterio de ordenación (-released para más próximos primero)
     * @param pageSize  Número de resultados por página
     * @return          Llamada asíncrona que retorna {@link GamesResponse} con juegos próximos
     */
    @GET("games")
    Call<GamesResponse> getFutureGames(
            @Query("key") String apiKey,
            @Query("dates") String dates,
            @Query("ordering") String ordering,
            @Query("page_size") int pageSize
    );

    /**
     * Obtiene videojuegos lanzados recientemente.
     * <p>
     * Recupera juegos que fueron lanzados recientemente dentro del rango
     * de fechas especificado, ordenados por fecha de lanzamiento.
     * Útil para mostrar los últimos lanzamientos al usuario.
     *
     * @param apiKey    Clave API de RAWG para autenticación
     * @param dates     Rango de fechas en formato "YYYY-MM-DD,YYYY-MM-DD"
     * @param ordering  Criterio de ordenación (-released para más recientes primero)
     * @param pageSize  Número de resultados por página
     * @return          Llamada asíncrona que retorna {@link GamesResponse} con juegos recientes
     */
    @GET("games")
    Call<GamesResponse> getRecentlyReleasedGames(
            @Query("key") String apiKey,
            @Query("dates") String dates,
            @Query("ordering") String ordering,
            @Query("page_size") int pageSize
    );
}

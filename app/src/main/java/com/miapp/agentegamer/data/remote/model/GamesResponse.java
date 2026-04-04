package com.miapp.agentegamer.data.remote.model;

import java.util.List;

/**
 * Wrapper de respuesta para las consultas a la API de RAWG.
 * <p>
 * Esta clase encapsula la estructura de respuesta estándar de la API de RAWG,
 * que devuelve los resultados dentro de un objeto contenedor. El campo "results"
 * contiene la lista de videojuegos que coinciden con los criterios de la consulta.
 * <p>
 * La API de RAWG utiliza paginación, por lo que esta clase representa una
 * única página de resultados. Para obtener más resultados, se deben realizar
 * llamadas adicionales especificando el número de página.
 *
 * @see GameDto
 */
public class GamesResponse {

    /** Lista de videojuegos retornados en esta página de resultados */
    private List<GameDto> results;

    /**
     * Obtiene la lista de videojuegos de la respuesta.
     * @return Lista de GameDto con los resultados de la consulta
     */
    public List<GameDto> getResults() {
        return results;
    }
}

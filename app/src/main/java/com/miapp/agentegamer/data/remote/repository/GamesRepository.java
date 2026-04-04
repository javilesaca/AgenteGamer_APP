package com.miapp.agentegamer.data.remote.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.miapp.agentegamer.data.remote.api.GamesApiService;
import com.miapp.agentegamer.data.remote.api.RetrofitClient;
import com.miapp.agentegamer.data.remote.model.GameDto;
import com.miapp.agentegamer.data.remote.model.GamesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repositorio para gestionar la obtención de datos de videojuegos desde la API de RAWG.
 * <p>
 * Esta clase actúa como intermediario entre la capa de presentación (ViewModels)
 * y la API externa de RAWG. Implementa el patrón Repository para abstraer la
 * fuente de datos remota y proporcionar una interfaz unificada para obtener
 * información de videojuegos.
 * <p>
 * El repositorio maneja las siguientes operaciones:
 * <ul>
 *   <li>Obtener juegos mejor valorados</li>
 *   <li>Buscar juegos por nombre</li>
 *   <li>Obtener lanzamientos recientes</li>
 *   <li>Buscar juegos con paginación</li>
 * </ul>
 * <p>
 * Todas las operaciones de red se ejecutan de forma asíncrona y los resultados
 * se publican a través de objetos {@link LiveData} para观察 cambios en tiempo real.
 *
 * @see GameDto
 * @see GamesApiService
 * @see RetrofitClient
 */
public class GamesRepository {

    /** Servicio de API para llamadas a RAWG */
    private final GamesApiService apiService;
    
    /** Clave API de RAWG para autenticación */
    private final String apiKey;
    
    /** LiveData que contiene la lista de juegos más reciente */
    private final MutableLiveData<List<GameDto>> gamesLiveData = new MutableLiveData<>();
    
    /** LiveData que indica si hay una operación en curso */
    private final MutableLiveData<Boolean> cargando = new MutableLiveData<>();
    
    /** Página actual para paginación */
    private int currentPage = 1;
    
    /** Flag para evitar múltiples llamadas simultáneas */
    private boolean isLoading = false;
    
    /** Acumulador de resultados para paginación */
    private final List<GameDto> acumulado = new ArrayList<>();

    /**
     * Constructor del repositorio.
     * <p>
     * Inicializa el servicio API utilizando el cliente Retrofit configurado
     * y la clave API proporcionada.
     *
     * @param apiKey Clave API de RAWG para autenticación
     */
    public GamesRepository(String apiKey){
        this.apiKey = apiKey;
        apiService = RetrofitClient.getInstance()
                .create(GamesApiService.class);
    }

    /**
     * Obtiene un LiveData que indica si hay operaciones en curso.
     * <p>
     * Este observable puede ser utilizado por la capa de presentación para
     * mostrar indicadores de carga mientras se realizan peticiones a la API.
     *
     * @return LiveData que emite true cuando hay carga en proceso, false cuando no
     */
    public LiveData<Boolean> getCargando() {
        return cargando;
    }

    /**
     * Obtiene los videojuegos mejor valorados.
     * <p>
     * Realiza una consulta a la API de RAWG para obtener los 10 juegos
     * mejor valorados según su puntuación. El resultado se emite a través
     * del LiveData retornado.
     *
     * @return LiveData que emite la lista de juegos mejor valorados, o null en caso de error
     */
    public LiveData<List<GameDto>> getGames() {
        cargando.setValue(true);
        apiService.getGames(apiKey, "-rating", 10).enqueue(new Callback<GamesResponse>() {
            @Override
            public void onResponse(Call<GamesResponse> call, Response<GamesResponse> response) {
                cargando.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    gamesLiveData.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<GamesResponse> call, Throwable throwable) {
                cargando.setValue(false);
                gamesLiveData.setValue(null);
            }
        });

        return gamesLiveData;
    }

    /**
     * Obtiene los videojuegos lanzados en los últimos 30 días.
     * <p>
     * Realiza una consulta a la API para obtener juegos que fueron lanzados
     * recientemente, ordenados por fecha de lanzamiento. Útil para mostrar
     * una sección de "Recién lanzados" al usuario.
     *
     * @return LiveData que emite la lista de juegos recientes, o null en caso de error
     */
    public LiveData<List<GameDto>> getRecentlyReleasedGames() {
        // Últimos 30 días de juegos lanzados, ordenados por fecha de lanzamiento
        String today = java.time.LocalDate.now().toString(); // 2026-04-01
        String thirtyDaysAgo = java.time.LocalDate.now().minusDays(30).toString(); // 2026-03-02
        String dates = thirtyDaysAgo + "," + today;

        cargando.setValue(true);
        apiService.getRecentlyReleasedGames(apiKey, dates, "-released", 20)
                .enqueue(new Callback<GamesResponse>() {
                    @Override
                    public void onResponse(Call<GamesResponse> call, Response<GamesResponse> response) {
                        cargando.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            gamesLiveData.setValue(response.body().getResults());
                        }
                    }

                    @Override
                    public void onFailure(Call<GamesResponse> call, Throwable throwable) {
                        cargando.setValue(false);
                        gamesLiveData.setValue(null);
                    }
                });

        return gamesLiveData;
    }

    /**
     * Busca videojuegos con soporte para paginación.
     * <p>
     * Realiza búsquedas por nombre de juego con acumulación de resultados.
     * Si {@code reset} es true, limpia los resultados anteriores y empieza
     * desde la página 1. De lo contrario, añade los resultados a la lista
     * acumulada existente.
     * <p>
     * Este método es ideal para implementaciones de "infinite scroll"
     * donde se cargan más resultados a medida que el usuario hace scroll.
     *
     * @param query  Texto de búsqueda
     * @param reset  Si true, reinicia la paginación; si false, añade más resultados
     * @return       LiveData que emite la lista acumulada de resultados de búsqueda
     */
    public LiveData<List<GameDto>> buscarJuegosPaginados(String query, boolean reset) {
        MutableLiveData<List<GameDto>> data = new MutableLiveData<>();

        if (isLoading) return data;

        if (reset) {
            currentPage = 1;
            acumulado.clear();
        }

        isLoading = true;
        cargando.postValue(true);

        apiService.searchGames(apiKey, query, currentPage, 20)
                .enqueue(new Callback<GamesResponse>() {
                    @Override
                    public void onResponse(Call<GamesResponse> call, Response<GamesResponse> response) {
                        isLoading = false;
                        cargando.postValue(false);

                        if(response.isSuccessful() && response.body() != null) {
                            acumulado.addAll(response.body().getResults());
                            currentPage++;
                            data.setValue(acumulado);
                        }
                    }

                    @Override
                    public void onFailure(Call<GamesResponse> call, Throwable throwable) {
                        isLoading = false;
                        cargando.postValue(false);
                    }
                });
        return data;
    }
}

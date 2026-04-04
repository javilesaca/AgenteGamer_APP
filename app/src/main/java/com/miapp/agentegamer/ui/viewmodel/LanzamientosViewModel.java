package com.miapp.agentegamer.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

import com.miapp.agentegamer.data.local.entity.LanzamientoEntity;
import com.miapp.agentegamer.data.remote.api.GamesApiService;
import com.miapp.agentegamer.data.remote.model.GameDto;
import com.miapp.agentegamer.data.remote.model.GamesResponse;
import com.miapp.agentegamer.domain.repository.LanzamientoRepository;
import com.miapp.agentegamer.util.FechaUtils;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * LanzamientosViewModel
 * ---------------------
 * ViewModel que gestiona los datos de próximos lanzamientos de videojuegos.
 * Utiliza el patrón MVVM y combina datos locales (Room) con datos remotos (API).
 * 
 * Funcionalidades:
 * - Provee los próximos lanzamientos guardados en la base de datos local
 * - Precarga lanzamientos de los próximos 15 días desde la API de RAWG
 * - Filtra solo lanzamientos futuros reales (0-15 días)
 * - Inserta los juegos en la base de datos local para acceso offline
 * - Cancela llamadas anteriores si se inicia una nueva precarga
 * 
 * @see LanzamientoRepository
 * @see GamesApiService
 * @see LanzamientoEntity
 */
@HiltViewModel
public class LanzamientosViewModel extends AndroidViewModel {

    // Repositorio de lanzamientos para acceder a la base de datos local
    private final LanzamientoRepository repo;
    // Servicio de API para obtener juegos de RAWG
    private final GamesApiService api;
    // Clave de API de RAWG
    private final String apiKey;
    // Llamada actual a la API (para poder cancelarla)
    private Call<GamesResponse> currentCall;

    @Inject
    public LanzamientosViewModel(@NonNull Application app, LanzamientoRepository repo,
            GamesApiService api, String apiKey) {
        super(app);
        this.repo = repo;
        this.api = api;
        this.apiKey = apiKey;
    }

    /**
     * Devuelve los lanzamientos futuros guardados en Room
     */
    public LiveData<List<LanzamientoEntity>> getLanzamientos() {
        return repo.getProximosLanzamientos(System.currentTimeMillis());
    }

    /**
     * Precarga lanzamientos de los próximos 15 días desde la API
     * y los guarda en la base de datos local.
     */
    public void precargaProximos15Dias() {
        // Cancel any previous call
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
        }

        String fechas = FechaUtils.hoy() + "," + FechaUtils.dentroDeDias(15);

        currentCall = api.getFutureGames(apiKey, fechas, "released", 20);
        currentCall.enqueue(new Callback<GamesResponse>() {

            @Override
            public void onResponse(Call<GamesResponse> call,
                                   Response<GamesResponse> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                long ahora = System.currentTimeMillis();

                for (GameDto juego : response.body().getResults()) {

                    // Parse seguro de fecha
                    Date fecha = FechaUtils.parseFecha(juego.getReleaseDate());
                    if (fecha == null) continue;

                    long fechaMs = fecha.getTime();

                    // Filtrar solo futuros reales (0–15 días)
                    long dias = FechaUtils.diasHasta(fechaMs);
                    if (dias <= 0 || dias > 15) continue;

                    // Extraer plataformas
                    String plataformas = "";
                    if (juego.getPlatforms() != null) {
                        StringBuilder sb = new StringBuilder();
                        for (GameDto.PlatformWrapper wrapper : juego.getPlatforms()) {
                            if (wrapper.getPlatform() != null) {
                                if (sb.length() > 0) sb.append(", ");
                                sb.append(wrapper.getPlatform().getName());
                            }
                        }
                        plataformas = sb.toString();
                    }

                    // Crear entidad con todos los campos
                    LanzamientoEntity lanzamiento = new LanzamientoEntity(
                            null,
                            juego.getId(),
                            juego.getName(),
                            fechaMs,
                            0.00,
                            juego.getImageUrl(),
                            plataformas,
                            juego.getRating()
                    );

                    // Insertar (Room ignorará si ya existe)
                    repo.insertar(lanzamiento);
                }
            }

            @Override
            public void onFailure(Call<GamesResponse> call, Throwable t) {
                // Log opcional
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
        }
    }
}

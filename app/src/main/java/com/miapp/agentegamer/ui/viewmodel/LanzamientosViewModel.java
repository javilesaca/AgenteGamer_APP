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

@HiltViewModel
public class LanzamientosViewModel extends AndroidViewModel {

    private final LanzamientoRepository repo;
    private final GamesApiService api;
    private final String apiKey;
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

                    // Crear entidad
                    LanzamientoEntity lanzamiento = new LanzamientoEntity(
                            juego.getId(),          // PK
                            juego.getName(),
                            fechaMs,
                            0.00
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

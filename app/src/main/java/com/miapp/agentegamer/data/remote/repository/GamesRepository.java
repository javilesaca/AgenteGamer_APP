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

public class GamesRepository {

    private final GamesApiService apiService;
    private final String apiKey;
    private final MutableLiveData<List<GameDto>> gamesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> cargando = new MutableLiveData<>();
    private int currentPage = 1;
    private boolean isLoading = false;
    private final List<GameDto> acumulado = new ArrayList<>();

    public GamesRepository(String apiKey){
        this.apiKey = apiKey;
        apiService = RetrofitClient.getInstance()
                .create(GamesApiService.class);
    }

    public LiveData<Boolean> getCargando() {
        return cargando;
    }

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

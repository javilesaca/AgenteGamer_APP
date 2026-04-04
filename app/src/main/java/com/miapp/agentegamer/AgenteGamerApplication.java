package com.miapp.agentegamer;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.work.Configuration;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.miapp.agentegamer.data.remote.model.GameDto;
import com.miapp.agentegamer.data.remote.repository.GamesRepository;
import com.miapp.agentegamer.ui.worker.SistemaFinancieroWorker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class AgenteGamerApplication extends Application implements Configuration.Provider {

    @Inject
    HiltWorkerFactory workerFactory;

    private List<GameDto> preloadedGames = null;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public List<GameDto> getPreloadedGames() {
        return preloadedGames;
    }

    public void setPreloadedGames(List<GameDto> games) {
        this.preloadedGames = games;
    }

    public void preloadGames() {
        executor.execute(() -> {
            try {
                GamesRepository repository = new GamesRepository(BuildConfig.RAWG_API_KEY);
                androidx.lifecycle.LiveData<List<GameDto>> liveData = repository.getRecentlyReleasedGames();
                
                // Create observer to capture result and then remove itself
                Observer<List<GameDto>> observer = games -> {
                    // This callback runs on main thread due to Retrofit callback being on main
                    if (games != null && !games.isEmpty()) {
                        setPreloadedGames(games);
                    }
                    // Remove observer after receiving data to prevent memory leaks
                    liveData.removeObserver(observer);
                };
                
                liveData.observeForever(observer);
            } catch (Exception e) {
                // Silently fail - games will load normally on the screen
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();

        PeriodicWorkRequest agenteWorker = new PeriodicWorkRequest.Builder(
                SistemaFinancieroWorker.class,
                24,
                TimeUnit.HOURS
        ).build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "agente_financiero_worker",
                ExistingPeriodicWorkPolicy.KEEP,
                agenteWorker
        );
    }

    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build();
    }
}

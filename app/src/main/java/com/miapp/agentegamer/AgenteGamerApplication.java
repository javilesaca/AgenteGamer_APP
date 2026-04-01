package com.miapp.agentegamer;

import android.app.Application;

import androidx.hilt.work.HiltWorkerFactory;
import androidx.work.Configuration;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.miapp.agentegamer.ui.worker.SistemaFinancieroWorker;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class AgenteGamerApplication extends Application implements Configuration.Provider {

    @Inject
    HiltWorkerFactory workerFactory;

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

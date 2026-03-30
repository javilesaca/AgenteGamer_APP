package com.miapp.agentegamer.di;

import android.content.Context;

import androidx.room.Room;

import com.miapp.agentegamer.data.local.database.AppDatabase;
import com.miapp.agentegamer.domain.model.SistemaFinanciero;
import com.miapp.agentegamer.data.local.dao.GastoDao;
import com.miapp.agentegamer.data.local.dao.LanzamientoDao;
import com.miapp.agentegamer.data.local.dao.WishlistDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public static AppDatabase provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                "agente_gamer_db"
        ).fallbackToDestructiveMigration().build();
    }

    @Provides
    public static GastoDao provideGastoDao(AppDatabase database) {
        return database.gastoDao();
    }

    @Provides
    public static WishlistDao provideWishlistDao(AppDatabase database) {
        return database.wishlistDao();
    }

    @Provides
    public static LanzamientoDao provideLanzamientoDao(AppDatabase database) {
        return database.lanzamientoDao();
    }

    @Provides
    @Singleton
    public static ExecutorService provideExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    public static SistemaFinanciero provideSistemaFinanciero() {
        return new SistemaFinanciero(100);
    }
}

package com.miapp.agentegamer.di;

import com.miapp.agentegamer.data.remote.api.GamesApiService;
import com.miapp.agentegamer.data.remote.api.RetrofitClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public static Retrofit provideRetrofit() {
        return RetrofitClient.getInstance();
    }

    @Provides
    public static GamesApiService provideGamesApiService(Retrofit retrofit) {
        return retrofit.create(GamesApiService.class);
    }
}

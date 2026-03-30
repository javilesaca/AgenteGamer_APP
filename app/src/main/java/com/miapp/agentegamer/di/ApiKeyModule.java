package com.miapp.agentegamer.di;

import com.miapp.agentegamer.BuildConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ApiKeyModule {

    @Provides
    @Singleton
    public static String provideRawgApiKey() {
        return BuildConfig.RAWG_API_KEY;
    }
}
package com.miapp.agentegamer.di;

import com.miapp.agentegamer.data.repository.GastoRepositoryImpl;
import com.miapp.agentegamer.data.repository.LanzamientoRepositoryImpl;
import com.miapp.agentegamer.data.repository.WishlistRepositoryImpl;
import com.miapp.agentegamer.data.repository.UserRepositoryImpl;
import com.miapp.agentegamer.domain.repository.GastoRepository;
import com.miapp.agentegamer.domain.repository.LanzamientoRepository;
import com.miapp.agentegamer.domain.repository.WishlistRepository;
import com.miapp.agentegamer.domain.repository.UserRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Binds
    @Singleton
    public abstract GastoRepository bindGastoRepository(GastoRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract WishlistRepository bindWishlistRepository(WishlistRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract LanzamientoRepository bindLanzamientoRepository(LanzamientoRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract UserRepository bindUserRepository(UserRepositoryImpl impl);
}
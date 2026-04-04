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

/**
 * Módulo de Hilt que proporciona las implementaciones de los repositorios
 * del dominio de la aplicación.
 * <p>
 * Este módulo utiliza la anotación {@code @Binds} para vincular las interfaces
 * de repositorio definidas en la capa de dominio con sus implementaciones
 * concretas en la capa de datos. Este enfoque sigue el principio de
 * inversión de dependencias (DIP) del SOLID, donde las capas superiores
 * (dominio) definen abstracciones que las capas inferiores (datos) implementan.
 * <p>
 * La anotación {@code @Module} indica que esta clase es un módulo de Hilt.
 * {@code @InstallIn(SingletonComponent.class)} especifica que las dependencias
 * proporcionadas serán singletons durante todo el ciclo de vida de la aplicación.
 * <p>
 * La anotación {@code @Binds} es más eficiente que {@code @Provides} cuando se
 * trata de vincular clases abstractas o interfaces, ya que indica a Hilt cómo
 * resolver las dependencias de forma más directa.
 *
 * @see Module
 * @see InstallIn
 * @see SingletonComponent
 * @see Binds
 */
@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    /**
     * Vincula la implementación {@link GastoRepositoryImpl} a la interfaz
     * {@link GastoRepository}.
     * <p>
     * Este método permite que Hilt inyecte la implementación de repositorio de
     * gastos wherever se requiera la interfaz {@code GastoRepository}. La anotación
     * {@code @Singleton} asegura que solo exista una instancia de este repositorio
     * en toda la aplicación.
     *
     * @param impl implementación concreta del repositorio de gastos que será
     *              inyectada por Hilt
     * @return la interfaz {@link GastoRepository} vinculada a su implementación
     * @see Singleton
     * @see Binds
     */
    @Binds
    @Singleton
    public abstract GastoRepository bindGastoRepository(GastoRepositoryImpl impl);

    /**
     * Vincula la implementación {@link WishlistRepositoryImpl} a la interfaz
     * {@link WishlistRepository}.
     * <p>
     * Este método permite que Hilt inyecte la implementación del repositorio de
     * wishlist wherever se requiera la interfaz {@code WishlistRepository}.
     * La anotación {@code @Singleton} garantiza una única instancia del repositorio.
     *
     * @param impl implementación concreta del repositorio de wishlist que será
     *              inyectada por Hilt
     * @return la interfaz {@link WishlistRepository} vinculada a su implementación
     * @see Singleton
     * @see Binds
     */
    @Binds
    @Singleton
    public abstract WishlistRepository bindWishlistRepository(WishlistRepositoryImpl impl);

    /**
     * Vincula la implementación {@link LanzamientoRepositoryImpl} a la interfaz
     * {@link LanzamientoRepository}.
     * <p>
     * Este método permite que Hilt inyecte la implementación del repositorio de
     * lanzamientos wherever se requiera la interfaz {@code LanzamientoRepository}.
     * La anotación {@code @Singleton} garantiza una única instancia del repositorio.
     *
     * @param impl implementación concreta del repositorio de lanzamientos que será
     *              inyectada por Hilt
     * @return la interfaz {@link LanzamientoRepository} vinculada a su implementación
     * @see Singleton
     * @see Binds
     */
    @Binds
    @Singleton
    public abstract LanzamientoRepository bindLanzamientoRepository(LanzamientoRepositoryImpl impl);

    /**
     * Vincula la implementación {@link UserRepositoryImpl} a la interfaz
     * {@link UserRepository}.
     * <p>
     * Este método permite que Hilt inyecte la implementación del repositorio de
     * usuarios wherever se requiera la interfaz {@code UserRepository}. La anotación
     * {@code @Singleton} garantiza una única instancia del repositorio.
     *
     * @param impl implementación concreta del repositorio de usuarios que será
     *              inyectada por Hilt
     * @return la interfaz {@link UserRepository} vinculada a su implementación
     * @see Singleton
     * @see Binds
     */
    @Binds
    @Singleton
    public abstract UserRepository bindUserRepository(UserRepositoryImpl impl);
}
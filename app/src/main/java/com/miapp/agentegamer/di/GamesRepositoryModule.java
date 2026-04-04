package com.miapp.agentegamer.di;

import com.miapp.agentegamer.BuildConfig;
import com.miapp.agentegamer.data.remote.repository.GamesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Módulo de Hilt que proporciona el repositorio de juegos para acceder a la
 * API externa de RAWG.
 * <p>
 * Este módulo provee la instancia del {@link GamesRepository}, que es el
 * responsable de manejar toda la lógica de comunicación con la API de RAWG
 * (Rapid API for Video Games). El repositorio utiliza la API key almacenada
 * en {@link BuildConfig#RAWG_API_KEY} para autenticar las peticiones.
 * <p>
 * Se instala en {@link SingletonComponent} para garantizar que el repositorio
 * sea un singleton durante todo el ciclo de vida de la aplicación. Esto
 * permite mantener en caché las respuestas de la API y evitar peticiones
 * redundantes.
 * <p>
 * La anotación {@code @Module} indica que esta clase es un módulo de Hilt que
 * define métodos para proporcionar dependencias. {@code @InstallIn(SingletonComponent.class)}
 * especifica que las dependencias proporcionadas estarán disponibles en todo
 * el aplicativo.
 *
 * @see Module
 * @see InstallIn
 * @see SingletonComponent
 * @see GamesRepository
 * @see BuildConfig
 */
@Module
@InstallIn(SingletonComponent.class)
public class GamesRepositoryModule {

    /**
     * Proporciona el repositorio singleton de juegos para acceder a la API de RAWG.
     * <p>
     * Este método crea y devuelve una instancia de {@link GamesRepository}
     * inicializada con la API key de RAWG tomada de {@link BuildConfig#RAWG_API_KEY}.
     * El repositorio es el componente central que maneja todas las operaciones
     * relacionadas con la búsqueda, obtención de detalles y gestión de juegos
     * desde la API externa.
     * <p>
     * La anotación {@code @Singleton} garantiza que se reutilice la misma instancia
     * del repositorio en toda la aplicación, optimizando el uso de la red y
     * manteniendo coherencia en los datos cacheados.
     *
     * @return instancia singleton de {@link GamesRepository} configurada con la
     *         API key de RAWG
     * @see BuildConfig#RAWG_API_KEY
     * @see Singleton
     */
    @Provides
    @Singleton
    public static GamesRepository provideGamesRepository() {
        return new GamesRepository(BuildConfig.RAWG_API_KEY);
    }
}

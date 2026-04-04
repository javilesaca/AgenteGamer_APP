package com.miapp.agentegamer.di;

import com.miapp.agentegamer.data.remote.api.GamesApiService;
import com.miapp.agentegamer.data.remote.api.RetrofitClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

/**
 * Módulo de Hilt que proporciona las dependencias relacionadas con la red
 * y la comunicación HTTP.
 * <p>
 * Este módulo provee la instancia de Retrofit para realizar peticiones HTTP
 * a la API externa de RAWG (Rapid API for Video Games), y el servicio de API
 * específico para consumir los endpoints de juegos.
 * <p>
 * Se instala en {@link SingletonComponent} para garantizar que las instancias
 * de red sean singleton durante todo el ciclo de vida de la aplicación.
 * <p>
 * La anotación {@code @Module} indica que esta clase es un módulo de Hilt que
 * define métodos para proporcionar dependencias. {@code @InstallIn(SingletonComponent.class)}
 * especifica que las dependencias proporcionadas estarán disponibles en todo
 * el aplicativo.
 *
 * @see Module
 * @see InstallIn
 * @see SingletonComponent
 * @see Retrofit
 * @see GamesApiService
 */
@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    /**
     * Proporciona la instancia singleton de Retrofit para comunicación HTTP.
     * <p>
     * Este método devuelve la instancia de {@link Retrofit} configurada por
     * {@link RetrofitClient}, que incluye la configuración base de la URL de la
     * API de RAWG y el convertidor de JSON (Gson).
     * <p>
     * La anotación {@code @Singleton} garantiza que se reutilice la misma
     * instancia de Retrofit en toda la aplicación, optimizando el uso de
     * recursos de red.
     *
     * @return instancia singleton de {@link Retrofit} configurada para la API de RAWG
     * @see RetrofitClient#getInstance()
     * @see Singleton
     */
    @Provides
    @Singleton
    public static Retrofit provideRetrofit() {
        return RetrofitClient.getInstance();
    }

    /**
     * Proporciona el servicio de API para consumir los endpoints de juegos de RAWG.
     * <p>
     * Este método crea una implementación de {@link GamesApiService} utilizando
     * la instancia de Retrofit proporcionada. El servicio permite realizar las
     * llamadas HTTP a la API de RAWG de forma type-safe, mapeando las respuestas
     * a objetos Java.
     *
     * @param retrofit instancia de {@link Retrofit} inyectada automáticamente
     *                  por Hilt para crear el servicio de API
     * @return implementación de {@link GamesApiService} lista para realizar
     *         peticiones HTTP a la API de RAWG
     * @see Retrofit#create(Class)
     */
    @Provides
    public static GamesApiService provideGamesApiService(Retrofit retrofit) {
        return retrofit.create(GamesApiService.class);
    }
}

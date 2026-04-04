package com.miapp.agentegamer.di;

import com.miapp.agentegamer.BuildConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Módulo de Hilt que proporciona la clave de API para el servicio externo de RAWG.
 * <p>
 * Este módulo provee la API key necesaria para autenticarse con la API de RAWG
 * (Rapid API for Video Games). La clave se obtiene directamente desde
 * {@link BuildConfig#RAWG_API_KEY}, que es generada en tiempo de compilación
 * a partir del archivo {@code local.properties} o las variables de entorno del
 * sistema de build.
 * <p>
 * Se instala en {@link SingletonComponent} para garantizar que la API key sea
 * un singleton durante todo el ciclo de vida de la aplicación. Aunque no es
 * estrictamente necesario para una cadena de caracteres, esta práctica
 * proporciona consistencia y evita posibles recreaciones innecesarias.
 * <p>
 * La anotación {@code @Module} indica que esta clase es un módulo de Hilt que
 * define métodos para proporcionar dependencias. {@code @InstallIn(SingletonComponent.class)}
 * especifica que las dependencias proporcionadas estarán disponibles en todo
 * el aplicativo.
 *
 * @see Module
 * @see InstallIn
 * @see SingletonComponent
 * @see BuildConfig
 */
@Module
@InstallIn(SingletonComponent.class)
public class ApiKeyModule {

    /**
     * Proporciona la clave de API de RAWG para autenticarse con el servicio externo.
     * <p>
     * Este método devuelve la API key almacenada en {@link BuildConfig#RAWG_API_KEY}.
     * Esta clave es necesaria para realizar peticiones autenticadas a la API de RAWG
     * y obtener datos de juegos, búsquedas y otra información relacionada.
     * <p>
     * La anotación {@code @Singleton} garantiza que se utilice la misma instancia
     * de la cadena de la API key en toda la aplicación, aunque en la práctica
     * el valor es inmutable.
     *
     * @return cadena con la API key de RAWG tomada de {@link BuildConfig#RAWG_API_KEY}
     * @see BuildConfig#RAWG_API_KEY
     * @see Singleton
     */
    @Provides
    @Singleton
    public static String provideRawgApiKey() {
        return BuildConfig.RAWG_API_KEY;
    }
}
package com.miapp.agentegamer.data.remote.api;


/**
 * Cliente singleton de Retrofit para la API de RAWG (Rawg.io).
 * <p>
 * Esta clase proporciona una instancia única de {@link Retrofit} configurada
 * para realizar llamadas a la API de RAWG, que es el servicio externo utilizado
 * por la aplicación para obtener datos de videojuegos.
 * <p>
 * La configuración incluye:
 * <ul>
 *   <li>URL base de la API: https://api.rawg.io/api/</li>
 *   <li>Conversor Gson para parsing de JSON</li>
 * </ul>
 * <p>
 * Este cliente implementa el patrón Singleton para garantizar que solo exista
 * una instancia de Retrofit en toda la aplicación, evitando múltiples
 * configuraciones y optimizando recursos.
 *
 * @see Retrofit
 * @see retrofit2.Retrofit.Builder
 */
public class RetrofitClient {

    /** URL base de la API de RAWG */
    private static final  String BASE_URL = "https://api.rawg.io/api/";
    
    /** Instancia única de Retrofit (patrón singleton) */
    private static Retrofit retrofit;

    /**
     * Obtiene la instancia única de Retrofit.
     * <p>
     * Si la instancia no existe, la crea con la configuración por defecto:
     * <ul>
     *   <li>Base URL: https://api.rawg.io/api/</li>
     *   <li>Conversor: GsonConverterFactory para parsing de JSON</li>
     * </ul>
     *
     * @return instancia configurada de Retrofit lista para crear servicios API
     */
    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

package com.miapp.agentegamer;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.work.Configuration;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.miapp.agentegamer.data.remote.model.GameDto;
import com.miapp.agentegamer.data.remote.repository.GamesRepository;
import com.miapp.agentegamer.ui.worker.SistemaFinancieroWorker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;
import com.miapp.agentegamer.BuildConfig;

/**
 * Clase principal de aplicación que sirve como punto de entrada para AgenteGamer.
 * <p>
 * Esta clase extiende {@link Application} y es el componente principal de la app Android.
 * Se encarga de tres responsabilidades principales:
 * <ul>
 *     <li>Gestionar la inyección de dependencias mediante Hilt</li>
 *     <li>Configurar y gestionar WorkManager para tareas en segundo plano</li>
 *     <li>Pre-cargar datos de juegos para mejorar el rendimiento inicial</li>
 * </ul>
 * <p>
 * La anotación {@code @HiltAndroidApp} habilita la generación automática del gráfico
 * de dependencias de Hilt para toda la aplicación.
 *
 * @see Application
 * @see HiltAndroidApp
 * @see Configuration.Provider
 */
@HiltAndroidApp
public class AgenteGamerApplication extends Application implements Configuration.Provider {

    /**
     * Fábrica de workers de Hilt inyectada automáticamente.
     * Permite que los WorkManager workers tengan acceso a las dependencias de Hilt.
     */
    @Inject
    HiltWorkerFactory workerFactory;

    /** Lista de juegos pre-cargados para evitar llamadas API repetidas */
    private List<GameDto> preloadedGames = null;
    
    /** Executor para operaciones en segundo plano (carga de juegos) */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    /** Handler para ejecutar operaciones en el hilo principal */
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * Obtiene la lista de juegos pre-cargados.
     * <p>
     * Este método devuelve los juegos que fueron pre-cargados al iniciar la aplicación.
     * Si los juegos no han sido pre-cargados aún, devuelve {@code null}.
     *
     * @return Lista de objetos {@link GameDto} pre-cargados, o {@code null} si no hay datos
     * @see #preloadGames()
     * @see #setPreloadedGames(List)
     */
    public List<GameDto> getPreloadedGames() {
        return preloadedGames;
    }

    /**
     * Establece la lista de juegos pre-cargados.
     * <p>
     * Método utilizado internamente para almacenar los juegos obtenidos
     * de la API cuando la operación de pre-carga se completa.
     *
     * @param games Lista de juegos a almacenar en caché
     * @see #preloadGames()
     * @see #getPreloadedGames()
     */
    public void setPreloadedGames(List<GameDto> games) {
        this.preloadedGames = games;
    }

    /**
     * Pre-carga los juegos más recientes desde la API de RAWG.
     * <p>
     * Este método ejecuta en un hilo secundario una consulta a la API de RAWG
     * para obtener los juegos lanzados recientemente. Los resultados se almacenan
     * en caché mediante {@link #setPreloadedGames(List)} para que estén disponibles
     * inmediatamente al mostrar las pantallas de la aplicación.
     * <p>
     * Si la operación falla, se ignora silenciosamente y los juegos se cargarán
     * normalmente bajo demanda cuando se navigue a las pantallas correspondientes.
     *
     * @see #getPreloadedGames()
     * @see #setPreloadedGames(List)
     */
    public void preloadGames() {
        executor.execute(() -> {
            try {
                GamesRepository repository = new GamesRepository(BuildConfig.RAWG_API_KEY);
                androidx.lifecycle.LiveData<List<GameDto>> liveData = repository.getRecentlyReleasedGames();
                
                final Observer<List<GameDto>>[] observerRef = new Observer[1];
                
                observerRef[0] = games -> {
                    if (games != null && !games.isEmpty()) {
                        setPreloadedGames(games);
                    }
                    liveData.removeObserver(observerRef[0]);
                };
                
                liveData.observeForever(observerRef[0]);
            } catch (Exception e) {
                // Silently fail - games will load normally on the screen
            }
        });
    }

    /**
     * Callback llamado cuando la aplicación se inicia por primera vez.
     * <p>
     * Se ejecuta al iniciar el proceso de la aplicación y configura el worker
     * periódico para el cálculo del sistema financiero. El worker se ejecuta
     * cada 24 horas para analizar los gastos del usuario y proporcionar
     * recomendaciones financieras basadas en su historial de compras.
     *
     * @see #getWorkManagerConfiguration()
     * @see SistemaFinancieroWorker
     */
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

    /**
     * Proporciona la configuración de WorkManager para la aplicación.
     * <p>
     * Este método implementa la interfaz {@link Configuration.Provider} y devuelve
     * una configuración personalizada que incluye la {@link HiltWorkerFactory} inyectada.
     * Esto permite que los Workers de WorkManager tengan acceso a las dependencias
     * gestionadas por Hilt.
     *
     * @return Objeto {@link Configuration} con la fábrica de workers configurada
     * @see Configuration.Provider
     * @see HiltWorkerFactory
     */
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build();
    }
}

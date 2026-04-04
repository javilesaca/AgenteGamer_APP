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

/**
 * Módulo de Hilt que proporciona las dependencias relacionadas con la
 * base de datos local de la aplicación.
 * <p>
 * Este módulo provee la instancia de la base de datos Room, los objetos de
 * acceso a datos (DAOs) y el executor para operaciones en segundo plano.
 * Se instala en {@link SingletonComponent} para garantizar que las instancias
 * sean únicas y persistentes durante todo el ciclo de vida de la aplicación.
 * <p>
 * La anotación {@code @Module} indica que esta clase es un módulo de Hilt que
 * define métodos para proporcionar dependencias. {@code @InstallIn(SingletonComponent.class)}
 * especifica que las dependencias proporcionadas por este módulo estarán
 * disponibles en todo el aplicativo como singletons.
 *
 * @see Module
 * @see InstallIn
 * @see SingletonComponent
 */
@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    /**
     * Proporciona la instancia singleton de la base de datos Room.
     * <p>
     * Este método crea y configura una instancia de {@link AppDatabase} utilizando
     * el nombre de base de datos "agente_gamer_db". Se utiliza
     * {@code fallbackToDestructiveMigration()} para permitir destruir la base de
     * datos y recrearla cuando se detectan migraciones no manejadas, facilitando
     * el desarrollo y pruebas.
     *
     * @param context contexto de la aplicación inyectado por Hilt mediante
     *                {@link ApplicationContext @ApplicationContext}. Se usa para
     *                acceder al contexto de la aplicación necesario para construir
     *                la base de datos Room.
     * @return instancia singleton de {@link AppDatabase} configurada y lista para usar
     * @see Room#databaseBuilder(Context, Class, String)
     * @see Singleton
     */
    @Provides
    @Singleton
    public static AppDatabase provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                "agente_gamer_db"
        ).fallbackToDestructiveMigration().build();
    }

    /**
     * Proporciona el DAO para operaciones relacionadas con gastos.
     * <p>
     * Este DAO permite realizar operaciones CRUD (Create, Read, Update, Delete)
     * sobre la entidad de gastos en la base de datos Room.
     *
     * @param database instancia de {@link AppDatabase} inyectada automáticamente
     *                  por Hilt para obtener el DAO correspondiente
     * @return instancia de {@link GastoDao} para operar sobre la tabla de gastos
     * @see AppDatabase#gastoDao()
     */
    @Provides
    public static GastoDao provideGastoDao(AppDatabase database) {
        return database.gastoDao();
    }

    /**
     * Proporciona el DAO para operaciones relacionadas con la wishlist de juegos.
     * <p>
     * Este DAO permite gestionar la lista de deseos de juegos del usuario,
     * incluyendo agregar, eliminar y consultar juegos en la wishlist.
     *
     * @param database instancia de {@link AppDatabase} inyectada automáticamente
     *                  por Hilt para obtener el DAO correspondiente
     * @return instancia de {@link WishlistDao} para operar sobre la tabla de wishlist
     * @see AppDatabase#wishlistDao()
     */
    @Provides
    public static WishlistDao provideWishlistDao(AppDatabase database) {
        return database.wishlistDao();
    }

    /**
     * Proporciona el DAO para operaciones relacionadas con lanzamientos de juegos.
     * <p>
     * Este DAO permite gestionar los lanzamientos de juegos próximos, incluyendo
     * consultar y actualizar la información de lanzamientos en la base de datos.
     *
     * @param database instancia de {@link AppDatabase} inyectada automáticamente
     *                  por Hilt para obtener el DAO correspondiente
     * @return instancia de {@link LanzamientoDao} para operar sobre la tabla de lanzamientos
     * @see AppDatabase#lanzamientoDao()
     */
    @Provides
    public static LanzamientoDao provideLanzamientoDao(AppDatabase database) {
        return database.lanzamientoDao();
    }

    /**
     * Proporciona un ExecutorService para ejecutar operaciones en un hilo separado.
     * <p>
     * Este executor de hilo único es utilizado para realizar operaciones de base
     * de datos de forma asíncrona, evitando bloquear el hilo principal de la UI.
     * La anotación {@code @Singleton} garantiza que se reutilice la misma instancia
     * en toda la aplicación.
     *
     * @return {@link ExecutorService} configurado como un ejecutor de hilo único
     *         para operaciones en segundo plano
     * @see Executors#newSingleThreadExecutor()
     * @see Singleton
     */
    @Provides
    @Singleton
    public static ExecutorService provideExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

    /**
     * Proporciona una instancia del sistema financiero para el cálculo de presupuestos.
     * <p>
     * El {@link SistemaFinanciero} gestiona el presupuesto inicial del usuario
     * para gastos en juegos. Se inicializa con un monto predeterminado de 100 euros.
     *
     * @return nueva instancia de {@link SistemaFinanciero} con presupuesto inicial de 100
     */
    @Provides
    public static SistemaFinanciero provideSistemaFinanciero() {
        return new SistemaFinanciero(100);
    }
}

package com.miapp.agentegamer.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.miapp.agentegamer.data.local.dao.GastoDao;
import com.miapp.agentegamer.data.local.dao.LanzamientoDao;
import com.miapp.agentegamer.data.local.dao.WishlistDao;
import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.data.local.entity.LanzamientoEntity;
import com.miapp.agentegamer.data.local.entity.WishlistEntity;

/**
 * Clase singleton que representa la base de datos Room de la aplicación AgenteGamer.
 * <p>
 * Esta clase gestiona el acceso a la base de datos SQLite local mediante Room.
 * Implementa el patrón singleton para garantizar una única instancia de la base
 * de datos en toda la aplicación.
 * <p>
 * <b>Entidades gestionadas:</b>
 * <ul>
 *     <li>{@link GastoEntity} - Almacena los gastos de juegos del usuario</li>
 *     <li>{@link WishlistEntity} - Almacena la lista de deseos de juegos</li>
 *     <li>{@link LanzamientoEntity} - Almacena los próximos lanzamientos</li>
 * </ul>
 * <p>
 * <b>DAOs disponibles:</b>
 * <ul>
 *     <li>{@link GastoDao} - Operaciones CRUD para gastos</li>
 *     <li>{@link WishlistDao} - Operaciones CRUD para wishlist</li>
 *     <li>{@link LanzamientoDao} - Operaciones CRUD para lanzamientos</li>
 * </ul>
 * <p>
 * La base de datos utiliza migración destructiva ({@code fallbackToDestructiveMigration()})
 * lo que significa que si cambia la versión de la base de datos, se eliminará y recreará
 * automáticamente. Esto es apropiado para desarrollo pero debe manejarse con precaución
 * en producción.
 *
 * @see RoomDatabase
 * @see GastoDao
 * @see WishlistDao
 * @see LanzamientoDao
 */
@Database(entities = {GastoEntity.class, WishlistEntity.class, LanzamientoEntity.class}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    /** Instancia singleton de la base de datos */
    private static volatile AppDatabase INSTANCE;

    /**
     * Obtiene el DAO para operaciones CRUD sobre la tabla de gastos.
     *
     * @return Instancia de {@link GastoDao} para gestionar gastos
     */
    public abstract GastoDao gastoDao();
    
    /**
     * Obtiene el DAO para operaciones CRUD sobre la tabla de wishlist.
     *
     * @return Instancia de {@link WishlistDao} para gestionar la lista de deseos
     */
    public abstract WishlistDao wishlistDao();
    
    /**
     * Obtiene el DAO para operaciones CRUD sobre la tabla de lanzamientos.
     *
     * @return Instancia de {@link LanzamientoDao} para gestionar lanzamientos
     */
    public abstract LanzamientoDao lanzamientoDao();


    /**
     * Obtiene la instancia singleton de la base de datos.
     * <p>
     * Utiliza inicialización lazy con sincronización doble para garantizar
     * thread-safety. El primer hilo que entre al bloque synchronized creará
     * la instancia, y los hilos subsecuentes la reutilizarán.
     * <p>
     * La base de datos se crea con el nombre {@code "agente_gamer_db"}.
     *
     * @param context Contexto de la aplicación (se obtiene el contexto de aplicación automáticamente)
     * @return Instancia singleton de {@link AppDatabase}
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "agente_gamer_db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}

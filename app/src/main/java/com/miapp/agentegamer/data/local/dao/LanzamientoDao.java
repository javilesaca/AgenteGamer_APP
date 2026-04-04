package com.miapp.agentegamer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miapp.agentegamer.data.local.entity.LanzamientoEntity;

import java.util.List;

/**
 * Interfaz de acceso a datos (DAO) para la gestión de lanzamientos de juegos.
 * <p>
 * Define las operaciones CRUD y consultas sobre la tabla {@code lanzamientos}
 * de la base de datos Room. Esta interfaz permite a los usuarios seguir los
 * próximos lanzamientos de juegos que les interesan.
 * <p>
 * Los lanzamientos se filtran por fecha, mostrando únicamente aquellos con
 * fecha de lanzamiento igual o mayor a la fecha actual.
 *
 * @see Dao
 * @see LanzamientoEntity
 */
@Dao
public interface LanzamientoDao {

    /**
     * Obtiene los próximos lanzamientos de juegos de un usuario.
     * <p>
     * Devuelve un {@link LiveData} con los lanzamientos cuya fecha de lanzamiento
     * es igual o mayor a la fecha actual, ordenados cronológicamente. Útil para
     * mostrar al usuario qué juegos próximos tiene en seguimiento.
     *
     * @param userId Identificador único del usuario en Firebase
     * @param hoy Fecha actual en milisegundos (UNIX epoch)
     * @return LiveData con la lista de próximos lanzamientos
     */
    @Query("SELECT * FROM lanzamientos WHERE userId = :userId AND fechaLanzamiento >= :hoy ORDER BY fechaLanzamiento ASC")
    LiveData<List<LanzamientoEntity>> getProximosLanzamientos(String userId, long hoy);

    /**
     * Inserta un nuevo lanzamiento en la base de datos.
     * <p>
     * Utiliza la estrategia {@link OnConflictStrategy#IGNORE} para evitar
     * duplicados. Si el juego ya existe (determinado por la restricción única
     * en {@code userId} y {@code gameId}), no se realiza ninguna acción.
     *
     * @param lanzamiento Objeto {@link LanzamientoEntity} a insertar
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertar(LanzamientoEntity lanzamiento);

    /**
     * Actualiza un lanzamiento existente en la base de datos.
     * <p>
     * El lanzamiento se identifica por su clave primaria (campo {@code id}).
     * Útil para modificar información como el precio estimado o las plataformas.
     *
     * @param lanzamiento Objeto {@link LanzamientoEntity} con los datos actualizados
     */
    @Update
    void actualizar(LanzamientoEntity lanzamiento);

    /**
     * Elimina todos los lanzamientos de un usuario.
     * <p>
     * Operación de mantenimiento que borra todos los registros de lanzamientos
     * asociados al usuario especificado.
     *
     * @param userId Identificador único del usuario en Firebase
     */
    @Query("DELETE FROM lanzamientos WHERE userId = :userId")
    void borrarTodos(String userId);
}

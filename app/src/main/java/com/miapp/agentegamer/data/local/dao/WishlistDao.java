package com.miapp.agentegamer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miapp.agentegamer.data.local.entity.WishlistEntity;

import java.util.List;

/**
 * Interfaz de acceso a datos (DAO) para la gestión de la lista de deseos (wishlist).
 * <p>
 * Define todas las operaciones CRUD y consultas sobre la tabla {@code wishlist}
 * de la base de datos Room. Esta interfaz permite a los usuarios guardar juegos
 * que desean comprar o estar pendientes de su lanzamiento.
 * <p>
 * La estrategia de conflicto {@link OnConflictStrategy#REPLACE} asegura que si un
 * juego ya existe en la wishlist, se actualice con los nuevos datos.
 *
 * @see Dao
 * @see WishlistEntity
 */
@Dao
public interface WishlistDao {

    /**
     * Inserta o replace un juego en la lista de deseos.
     * <p>
     * Si el juego ya existe (determinado por la约束 única en {@code userId} y
     * {@code gameId}), se actualiza con los nuevos datos. Esta operación es útil
     * para actualizar precios estimados o información del juego.
     *
     * @param juego Objeto {@link WishlistEntity} a insertar o actualizar
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WishlistEntity juego);

    /**
     * Obtiene todos los juegos de la lista de deseos de un usuario.
     * <p>
     * Devuelve un {@link LiveData} que permite observar cambios en tiempo real
     * cuando se añaden, modifican o eliminan juegos de la wishlist.
     *
     * @param userId Identificador único del usuario en Firebase
     * @return LiveData con la lista de juegos en la wishlist
     */
    @Query("SELECT * FROM wishlist WHERE userId = :userId")
    LiveData<List<WishlistEntity>> getWishlist(String userId);

    /**
     * Actualiza un juego existente en la lista de deseos.
     * <p>
     * El juego se identifica por su clave primaria (campo {@code id}).
     * Útil para modificar atributos como el precio estimado o la fecha de lanzamiento.
     *
     * @param juego Objeto {@link WishlistEntity} con los datos actualizados
     */
    @Update
    void actualizar(WishlistEntity juego);

    /**
     * Elimina un juego de la lista de deseos.
     * <p>
     * El juego se identifica por su clave primaria (campo {@code id}).
     *
     * @param juego Objeto {@link WishlistEntity} a eliminar
     */
    @Delete
    void borrar(WishlistEntity juego);

    /**
     * Obtiene todos los juegos de la wishlist de forma síncrona.
     * <p>
     * Versión síncrona de {@link #getWishlist} que devuelve una lista directamente.
     * Útil para operaciones en segundo plano que no requieren observe cambios.
     *
     * @param userId Identificador único del usuario en Firebase
     * @return Lista de {@link WishlistEntity} en la wishlist
     */
    @Query("SELECT * FROM wishlist WHERE userId = :userId")
    List<WishlistEntity> getWishlistSync(String userId);

    /**
     * Obtiene el gasto total estimado de todos los juegos en la wishlist.
     * <p>
     * Suma el campo {@code precioEstimado} de todos los juegos del usuario.
     * Útil para mostrar al usuario cuánto gastaría si comprara todos los juegos.
     *
     * @param userId Identificador único del usuario en Firebase
     * @return Suma de los precios estimados, o {@code null} si no hay juegos
     */
    @Query("SELECT SUM(precioEstimado) FROM wishlist WHERE userId = :userId")
    Double getTotalGastado(String userId);

    /**
     * Elimina todos los juegos de la wishlist de un usuario.
     * <p>
     * Operación de mantenimiento que borra todos los registros de la wishlist
     * asociados al usuario especificado.
     *
     * @param userId Identificador único del usuario en Firebase
     */
    @Query("DELETE FROM wishlist WHERE userId = :userId")
    void deleteAll(String userId);

}

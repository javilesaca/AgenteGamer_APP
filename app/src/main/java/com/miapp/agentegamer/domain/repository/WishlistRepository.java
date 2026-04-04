package com.miapp.agentegamer.domain.repository;

import androidx.lifecycle.LiveData;

import com.miapp.agentegamer.data.local.entity.WishlistEntity;

import java.util.List;

/**
 * Interfaz del repositorio para la gestión de la lista de deseos (wishlist) del usuario.
 * 
 * <p>Define el contrato para las operaciones de acceso a datos relacionadas
 * con los juegos que el usuario ha añadido a su lista de deseos para comprar
 * en el futuro. Implementa el patrón Repository para abstraer la fuente de datos.</p>
 * 
 * <p>La wishlist permite al usuario:</p>
 * <ul>
 *   <li>Guardar juegos de interés para compra futura</li>
 *   <li>Visualizar su lista de deseos</li>
 *   <li>Actualizar información de juegos guardados</li>
 *   <li>Eliminar juegos de la lista</li>
 *   <li>Calcular el gasto potencial total de la wishlist</li>
 * </ul>
 * 
 * @author AgenteGamer
 * @version 1.0
 * @since 2024
 * @see WishlistEntity
 */
public interface WishlistRepository {
    
    /**
     * Inserta un nuevo juego en la lista de deseos.
     * 
     * @param juego WishlistEntity a añadir a la wishlist
     */
    void insertar(WishlistEntity juego);
    
    /**
     * Obtiene todos los juegos de la wishlist como LiveData.
     * 
     * <p>Retorna un observable que permite a la UI actualizarse automáticamente
     * cuando se modifican los datos de la wishlist.</p>
     * 
     * @return LiveData con la lista de juegos en la wishlist
     */
    LiveData<List<WishlistEntity>> getWishlist();
    
    /**
     * Actualiza un juego existente en la wishlist.
     * 
     * <p>Se utiliza para actualizar información como precio estimado,
     * fecha de lanzamiento, rating u otros atributos del juego.</p>
     * 
     * @param juego WishlistEntity con los datos actualizados
     */
    void actualizar(WishlistEntity juego);
    
    /**
     * Elimina un juego de la wishlist.
     * 
     * <p>Se invoca cuando el usuario decide quitar un juego de su
     * lista de deseos, ya sea porque lo compró o ya no le interesa.</p>
     * 
     * @param juego WishlistEntity a eliminar de la wishlist
     */
    void borrar(WishlistEntity juego);
    
    /**
     * Obtiene todos los juegos de la wishlist de forma síncrona.
     * 
     * <p>Versión síncrona que retorna directamente la lista sin LiveData.
     * Útil para operaciones que requieren acceso inmediato a los datos.</p>
     * 
     * @return Lista de WishlistEntity con todos los juegos de la wishlist
     */
    List<WishlistEntity> getWishlistSync();
    
    /**
     * Calcula el gasto potencial total de todos los juegos en la wishlist.
     * 
     * <p>Suma los precios estimados de todos los juegos en la lista de deseos.
     * Este valor representa cuánto gastaría el usuario si comprara todos los
     * juegos de su wishlist.</p>
     * 
     * @return Suma total de los precios estimados de la wishlist, o null si está vacía
     */
    Double getTotalGastado();
}

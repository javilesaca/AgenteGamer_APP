package com.miapp.agentegamer.data.repository;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.miapp.agentegamer.data.local.dao.WishlistDao;
import com.miapp.agentegamer.data.local.entity.WishlistEntity;
import com.miapp.agentegamer.domain.repository.WishlistRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

/**
 * Implementación del repositorio de wishlist que actúa como intermediario
 * entre los ViewModels y la capa de persistencia local (Room).
 * <p>
 * Esta clase implementa la interfaz {@link WishlistRepository} y gestiona
 * la lista de deseos del usuario: juegos que el usuario ha marcado como
 * "quiero comprar" o "me interesa".
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Gestionar la lista de deseos del usuario</li>
 *   <li>Calcular el gasto total en wishlist</li>
 *   <li>Ejecutar operaciones en hilos secundarios</li>
 * </ul>
 * <p>
 * Las operaciones de escritura se ejecutan de forma asíncrona mediante
 * un {@link ExecutorService} de hilo único para evitar problemas de
 * concurrencia con la base de datos.
 *
 * @see WishlistEntity
 * @see WishlistDao
 */
public class WishlistRepositoryImpl implements WishlistRepository {

    /** DAO para operaciones de base de datos de wishlist */
    private final WishlistDao dao;
    
    /** Executor de hilo único para operaciones de base de datos */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Constructor con inyección de dependencias.
     *
     * @param dao DAO para operaciones de wishlist
     */
    @Inject
    public WishlistRepositoryImpl(WishlistDao dao) {
        this.dao = dao;
    }

    /**
     * Obtiene el ID del usuario actualmente autenticado.
     *
     * @return UID del usuario actual, o "" si no hay sesión activa
     */
    private String getCurrentUserId() {
        return FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "";
    }

    /**
     * Inserta un juego en la wishlist.
     * <p>
     * La operación se ejecuta de forma asíncrona. Antes de insertar,
     * se asigna el userId del juego al usuario actual.
     *
     * @param juego Juego a añadir a la wishlist
     */
    @Override
    public void insertar(WishlistEntity juego) {
        executor.execute(() -> {
            juego.setUserId(getCurrentUserId());
            dao.insert(juego);
        });
    }

    /**
     * Obtiene la wishlist completa del usuario actual.
     *
     * @return LiveData con la lista de juegos en la wishlist
     */
    @Override
    public LiveData<List<WishlistEntity>> getWishlist() {
        return dao.getWishlist(getCurrentUserId());
    }

    /**
     * Actualiza un juego existente en la wishlist.
     * <p>
     * La operación se ejecuta de forma asíncrona.
     *
     * @param juego Juego con los datos actualizados
     */
    @Override
    public void actualizar(WishlistEntity juego) {
        executor.execute(() -> dao.actualizar(juego));
    }

    /**
     * Borra un juego de la wishlist.
     * <p>
     * La operación se ejecuta de forma asíncrona.
     *
     * @param juego Juego a borrar de la wishlist
     */
    @Override
    public void borrar(WishlistEntity juego) {
        executor.execute(() -> dao.borrar(juego));
    }

    /**
     * Obtiene la wishlist de forma síncrona.
     * <p>
     * Útil cuando se necesita acceso inmediato a los datos sin observar cambios.
     *
     * @return Lista de juegos en la wishlist
     */
    @Override
    public List<WishlistEntity> getWishlistSync() {
        return dao.getWishlistSync(getCurrentUserId());
    }

    /**
     * Calcula el gasto total si se compraran todos los juegos de la wishlist.
     * <p>
     * Suma los precios estimados de todos los juegos en la wishlist.
     *
     * @return Total estimado de gasto en wishlist
     */
    @Override
    public Double getTotalGastado() {
        return dao.getTotalGastado(getCurrentUserId());
    }
}

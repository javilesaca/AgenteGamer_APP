package com.miapp.agentegamer.data.repository;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.miapp.agentegamer.data.local.dao.LanzamientoDao;
import com.miapp.agentegamer.data.local.entity.LanzamientoEntity;
import com.miapp.agentegamer.domain.repository.LanzamientoRepository;

import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

/**
 * Implementación del repositorio de lanzamientos que actúa como intermediario
 * entre los ViewModels y la capa de persistencia local (Room).
 * <p>
 * Esta clase implementa la interfaz {@link LanzamientoRepository} y gestiona
 * los lanzamientos de videojuegos guardados por el usuario: juegos que fueron
 * marcados como "lanzados" o "quiero seguir el lanzamiento".
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Gestionar los lanzamientos guardados del usuario</li>
 *   <li>Obtener próximos lanzamientos</li>
 *   <li>Ejecutar operaciones en hilos secundarios</li>
 * </ul>
 * <p>
 * Las operaciones se ejecutan de forma asíncrona mediante Executors de hilo único.
 *
 * @see LanzamientoEntity
 * @see LanzamientoDao
 */
public class LanzamientoRepositoryImpl implements LanzamientoRepository {

    /** DAO para operaciones de base de datos de lanzamientos */
    private final LanzamientoDao lanzamientoDao;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param lanzamientoDao DAO para operaciones de lanzamientos
     */
    @Inject
    public LanzamientoRepositoryImpl(LanzamientoDao lanzamientoDao) {
        this.lanzamientoDao = lanzamientoDao;
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
     * Obtiene los próximos lanzamientos del usuario.
     * <p>
     * Recupera todos los lanzamientos guardados cuya fecha sea posterior
     * a la fecha actual (o la fecha pasada como parámetro).
     *
     * @param hoy Timestamp de referencia para filtrar lanzamientos futuros
     * @return   LiveData con la lista de próximos lanzamientos
     */
    @Override
    public LiveData<List<LanzamientoEntity>> getProximosLanzamientos(long hoy) {
        return lanzamientoDao.getProximosLanzamientos(getCurrentUserId(), hoy);
    }

    /**
     * Inserta un nuevo lanzamiento.
     * <p>
     * La operación se ejecuta de forma asíncrona. Antes de insertar,
     * se asigna el userId del lanzamiento al usuario actual.
     *
     * @param lanzamiento Lanzamiento a insertar
     */
    @Override
    public void insertar(LanzamientoEntity lanzamiento) {
        Executors.newSingleThreadExecutor().execute(() -> {
            lanzamiento.userId = getCurrentUserId();
            lanzamientoDao.insertar(lanzamiento);
        });
    }

    /**
     * Borra todos los lanzamientos del usuario actual.
     * <p>
     * La operación se ejecuta de forma asíncrona.
     */
    @Override
    public void borrarTodos() {
        Executors.newSingleThreadExecutor().execute(() -> lanzamientoDao.borrarTodos(getCurrentUserId()));
    }
}

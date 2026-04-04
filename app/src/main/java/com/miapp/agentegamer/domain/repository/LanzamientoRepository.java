package com.miapp.agentegamer.domain.repository;

import androidx.lifecycle.LiveData;

import com.miapp.agentegamer.data.local.entity.LanzamientoEntity;

import java.util.List;

/**
 * Interfaz del repositorio para la gestión de próximos lanzamientos de videojuegos.
 * 
 * <p>Define el contrato para las operaciones de acceso a datos relacionadas
 * con los lanzamientos próximos de juegos. Permite al usuario mantener un
 * registro de los juegos que saldrán pronto y que le interesan.</p>
 * 
 * <p>Esta interfaz proporciona:</p>
 * <ul>
 *   <li>Consultar próximos lanzamientos desde una fecha dada</li>
 *   <li>Insertar nuevos lanzamientos</li>
 *   <li>Limpiar la tabla de lanzamientos</li>
 * </ul>
 * 
 * @author AgenteGamer
 * @version 1.0
 * @since 2024
 * @see LanzamientoEntity
 */
public interface LanzamientoRepository {
    
    /**
     * Obtiene los próximos lanzamientos a partir de una fecha específica.
     * 
     * <p>Retorna todos los lanzamientos cuya fecha sea posterior o igual
     * a la fecha proporcionada como parámetro.</p>
     * 
     * @param hoy Timestamp de la fecha de referencia (generalmente la fecha actual)
     * @return LiveData con lista de LanzamientoEntity ordenados por fecha ascendente
     */
    LiveData<List<LanzamientoEntity>> getProximosLanzamientos(long hoy);
    
    /**
     * Inserta un nuevo lanzamiento en el sistema.
     * 
     * <p>Se utiliza para añadir juegos upcoming a la lista de seguimiento
     * de lanzamientos del usuario.</p>
     * 
     * @param lanzamiento LanzamientoEntity a insertar
     */
    void insertar(LanzamientoEntity lanzamiento);
    
    /**
     * Elimina todos los registros de lanzamientos.
     * 
     * <p>Se utiliza para limpiar la tabla antes de una sincronización
     * completa con nuevos datos de la API.</p>
     */
    void borrarTodos();
}

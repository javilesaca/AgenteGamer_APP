package com.miapp.agentegamer.domain.repository;

import androidx.lifecycle.LiveData;

import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.data.local.dao.GastoDao.MonthlyTotal;

import java.util.List;

/**
 * Interfaz del repositorio para la gestión de gastos del usuario.
 * 
 * <p>Define el contrato para las operaciones de acceso a datos relacionadas
 * con los gastos de videojuegos. Implementa el patrón Repository para
 * abstraer la fuente de datos de la capa de dominio.</p>
 * 
 * <p>Esta interfaz permite:</p>
 * <ul>
 *   <li>Obtener lista de todos los gastos</li>
 *   <li>Insertar, actualizar y borrar gastos</li>
 *   <li>Consultar gastos del mes actual</li>
 *   <li>Obtener totales mensuales para gráficos de tendencias</li>
 *   <li>Consultar gastos en rangos de fechas específicos</li>
 * </ul>
 * 
 * @author AgenteGamer
 * @version 1.0
 * @since 2024
 * @see GastoEntity
 * @see MonthlyTotal
 */
public interface GastoRepository {
    
    /**
     * Obtiene todos los gastos como LiveData para observar cambios en tiempo real.
     * 
     * @return LiveData con la lista de todos los gastos registrados
     */
    LiveData<List<GastoEntity>> obtenerGastos();
    
    /**
     * Inserta un nuevo gasto en el sistema.
     * 
     * @param gasto GastoEntity a insertar
     */
    void insertarGasto(GastoEntity gasto);
    
    /**
     * Actualiza un gasto existente.
     * 
     * @param gasto GastoEntity con los datos actualizados
     */
    void actualizarGasto(GastoEntity gasto);
    
    /**
     * Elimina un gasto del sistema.
     * 
     * @param gasto GastoEntity a eliminar
     */
    void borrarGasto(GastoEntity gasto);
    
    /**
     * Elimina todos los gastos registrados.
     * Utilizado principalmente al restablecer datos del usuario.
     */
    void borrarTodosLosGastos();
    
    /**
     * Obtiene el total de gastos del mes actual.
     * 
     * @return LiveData con el total gastado en el mes en curso
     */
    LiveData<Double> getGastoMesActual();
    
    /**
     * Obtiene el total gastado en el mes actual de forma síncrona.
     * 
     * @param callback Interfaz para retornar el resultado asíncronamente
     */
    void getTotalGastadoMesSync(OnTotalGastadoCallback callback);

    // ==================== NUEVOS MÉTODOS PARA DASHBOARD ====================

    /**
     * Obtiene totales mensuales para gráfico de tendencias del Dashboard.
     * 
     * <p>Devuelve una lista de objetos MonthlyTotal que contienen el mes
     * y el total gastado en cada uno de los últimos N meses.</p>
     * 
     * @param months Número de meses hacia atrás a incluir en el resultado
     * @return Lista de MonthlyTotal con mes y total
     * @see MonthlyTotal
     */
    List<MonthlyTotal> getMonthlyTotalsSync(int months);

    /**
     * Obtiene los últimos N gastos para el card de recientes del Dashboard.
     * 
     * @param limit Número máximo de gastos a retornar
     * @return LiveData con lista de gastos ordenados por fecha descendente
     */
    LiveData<List<GastoEntity>> getRecentGastos(int limit);

    /**
     * Obtiene el total gastado en un rango de fechas específico.
     * 
     * @param startDate Timestamp de inicio del rango (milisegundos)
     * @param endDate Timestamp de fin del rango (milisegundos)
     * @return LiveData con el total gastado en el período
     */
    LiveData<Double> getTotalForDateRange(long startDate, long endDate);

    /**
     * Obtiene el total gastado en un rango de fechas específico de forma síncrona.
     * 
     * @param startDate Timestamp de inicio del rango (milisegundos)
     * @param endDate Timestamp de fin del rango (milisegundos)
     * @return Total gastado en el período especificado
     */
    double getTotalForDateRangeSync(long startDate, long endDate);

    /**
     * Interfaz de callback para operaciones que retornan el total gastado.
     */
    interface OnTotalGastadoCallback {
        /**
         * Callback invoked when the total calculation is complete.
         * 
         * @param total Total gastado calculado
         */
        void onSuccess(double total);
    }
}

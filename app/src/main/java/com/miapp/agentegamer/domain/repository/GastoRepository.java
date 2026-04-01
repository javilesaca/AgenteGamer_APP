package com.miapp.agentegamer.domain.repository;

import androidx.lifecycle.LiveData;

import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.data.local.dao.GastoDao.MonthlyTotal;

import java.util.List;

public interface GastoRepository {
    LiveData<List<GastoEntity>> obtenerGastos();
    void insertarGasto(GastoEntity gasto);
    void actualizarGasto(GastoEntity gasto);
    void borrarGasto(GastoEntity gasto);
    void borrarTodosLosGastos();
    LiveData<Double> getGastoMesActual();
    void getTotalGastadoMesSync(OnTotalGastadoCallback callback);

    // ==================== NUEVOS MÉTODOS PARA DASHBOARD ====================

    /**
     * Obtiene totales mensuales para gráfico de tendencias.
     * @param months Número de meses hacia atrás
     * @return Lista de MonthlyTotal con mes y total
     */
    List<MonthlyTotal> getMonthlyTotalsSync(int months);

    /**
     * Obtiene los últimos N gastos para el card de recientes.
     * @param limit Número máximo de gastos
     * @return LiveData con lista de gastos
     */
    LiveData<List<GastoEntity>> getRecentGastos(int limit);

    /**
     * Obtiene total gastado en rango de fechas.
     * @param startDate Timestamp inicio
     * @param endDate Timestamp fin
     * @return LiveData con total
     */
    LiveData<Double> getTotalForDateRange(long startDate, long endDate);

    /**
     * Obtiene total gastado en rango de fechas (síncrono).
     * @param startDate Timestamp inicio
     * @param endDate Timestamp fin
     * @return Total gastado
     */
    double getTotalForDateRangeSync(long startDate, long endDate);

    interface OnTotalGastadoCallback {
        void onSuccess(double total);
    }
}

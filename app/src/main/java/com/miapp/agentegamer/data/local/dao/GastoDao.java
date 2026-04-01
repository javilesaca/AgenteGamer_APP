package com.miapp.agentegamer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Index;

import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.domain.model.MonthlyExpense;

import java.util.List;

@Dao
public interface GastoDao {

    @Insert
    void insertGasto(GastoEntity gasto);

    @Update
    void updateGasto(GastoEntity gasto);

    @Delete
    void deleteGasto(GastoEntity gasto);

    @Query("SELECT * FROM gastos ORDER BY fecha DESC")
    LiveData<List<GastoEntity>> getAllGastos();

    @Query("DELETE FROM gastos")
    void deleteAll();

    @Query("SELECT IFNULL(SUM(precio),0) FROM gastos " +
            "WHERE strftime('%m',fecha/1000,'unixepoch') = printf('%02d', :mes) " +
            "AND strftime('%Y',fecha/1000,'unixepoch') = CAST(:anio AS TEXT)")
    LiveData<Double> getGastoTotalMes(int mes, int anio);

    @Query("SELECT IFNULL(SUM(precio),0) FROM gastos WHERE mes = :mes AND anio = :anio")
    double getTotalGastadoMes(int mes, int anio);

    // ==================== NUEVAS QUERIES PARA DASHBOARD ====================

    /**
     * Obtiene gastos agrupados por mes para los últimos N meses.
     * Retorna lista de pares (mes, total) ordenados cronológicamente.
     */
    @Query("SELECT strftime('%Y-%m', fecha/1000, 'unixepoch') as mes, " +
           "SUM(precio) as total " +
           "FROM gastos " +
           "WHERE fecha >= :startDate " +
           "GROUP BY mes " +
           "ORDER BY mes ASC " +
           "LIMIT :limit")
    List<MonthlyTotal> getMonthlyTotals(long startDate, int limit);

    /**
     * Claseauxiliar para resultado de query mensual.
     */
    class MonthlyTotal {
        public String mes;
        public double total;
    }

    /**
     * Obtiene los últimos N gastos ordenados por fecha.
     * Para el card de "Últimos Gastos" en el Dashboard.
     */
    @Query("SELECT * FROM gastos ORDER BY fecha DESC LIMIT :limit")
    LiveData<List<GastoEntity>> getRecentGastos(int limit);

    /**
     * Obtiene el total gastado en un rango de fechas.
     * Para comparar mes actual vs mes anterior.
     */
    @Query("SELECT IFNULL(SUM(precio),0) FROM gastos " +
           "WHERE fecha >= :startDate AND fecha < :endDate")
    LiveData<Double> getTotalForDateRange(long startDate, long endDate);

    /**
     * Obtiene el total gastado en un rango de fechas (versión síncrona).
     * Para cálculos que requieren valor inmediato.
     */
    @Query("SELECT IFNULL(SUM(precio),0) FROM gastos " +
           "WHERE fecha >= :startDate AND fecha < :endDate")
    double getTotalForDateRangeSync(long startDate, long endDate);

}

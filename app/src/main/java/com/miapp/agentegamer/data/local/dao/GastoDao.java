package com.miapp.agentegamer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import com.miapp.agentegamer.data.local.entity.GastoEntity;

import java.util.List;

@Dao
public interface GastoDao {

    @Insert
    void insertGasto(GastoEntity gasto);

    @Update
    void updateGasto(GastoEntity gasto);

    @Delete
    void deleteGasto(GastoEntity gasto);

    @Query("SELECT * FROM gastos WHERE userId = :userId ORDER BY fecha DESC")
    LiveData<List<GastoEntity>> getAllGastos(String userId);

    @Query("DELETE FROM gastos WHERE userId = :userId")
    void deleteAll(String userId);

    @Query("SELECT IFNULL(SUM(precio),0) FROM gastos " +
            "WHERE userId = :userId " +
            "AND strftime('%m',fecha/1000,'unixepoch') = printf('%02d', :mes) " +
            "AND strftime('%Y',fecha/1000,'unixepoch') = CAST(:anio AS TEXT)")
    LiveData<Double> getGastoTotalMes(String userId, int mes, int anio);

    @Query("SELECT IFNULL(SUM(precio),0) FROM gastos WHERE userId = :userId AND mes = :mes AND anio = :anio")
    double getTotalGastadoMes(String userId, int mes, int anio);

    @Query("SELECT strftime('%Y-%m', fecha/1000, 'unixepoch') as mes, " +
           "SUM(precio) as total " +
           "FROM gastos " +
           "WHERE userId = :userId AND fecha >= :startDate " +
           "GROUP BY mes " +
           "ORDER BY mes ASC " +
           "LIMIT :limit")
    List<MonthlyTotal> getMonthlyTotals(String userId, long startDate, int limit);

    class MonthlyTotal {
        public String mes;
        public double total;
    }

    @Query("SELECT * FROM gastos WHERE userId = :userId ORDER BY fecha DESC LIMIT :limit")
    LiveData<List<GastoEntity>> getRecentGastos(String userId, int limit);

    @Query("SELECT IFNULL(SUM(precio),0) FROM gastos " +
           "WHERE userId = :userId AND fecha >= :startDate AND fecha < :endDate")
    LiveData<Double> getTotalForDateRange(String userId, long startDate, long endDate);

    @Query("SELECT IFNULL(SUM(precio),0) FROM gastos " +
           "WHERE userId = :userId AND fecha >= :startDate AND fecha < :endDate")
    double getTotalForDateRangeSync(String userId, long startDate, long endDate);

}

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

}

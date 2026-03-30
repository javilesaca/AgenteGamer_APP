package com.miapp.agentegamer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miapp.agentegamer.data.local.entity.LanzamientoEntity;

import java.util.List;

@Dao
public interface LanzamientoDao {

    @Query("SELECT * FROM lanzamientos WHERE fechaLanzamiento >= :hoy ORDER BY fechaLanzamiento ASC")
    LiveData<List<LanzamientoEntity>> getProximosLanzamientos(long hoy);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertar(LanzamientoEntity lanzamiento);

    @Update
    void actualizar(LanzamientoEntity lanzamiento);

    @Query("DELETE FROM lanzamientos")
    void borrarTodos();
}

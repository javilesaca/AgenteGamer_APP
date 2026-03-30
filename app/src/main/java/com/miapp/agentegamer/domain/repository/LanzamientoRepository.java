package com.miapp.agentegamer.domain.repository;

import androidx.lifecycle.LiveData;

import com.miapp.agentegamer.data.local.entity.LanzamientoEntity;

import java.util.List;

public interface LanzamientoRepository {
    LiveData<List<LanzamientoEntity>> getProximosLanzamientos(long hoy);
    void insertar(LanzamientoEntity lanzamiento);
    void borrarTodos();
}

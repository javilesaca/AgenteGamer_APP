package com.miapp.agentegamer.domain.repository;

import androidx.lifecycle.LiveData;
import com.miapp.agentegamer.data.model.UsuarioEntity;

public interface UserRepository {
    void obtenerUsuario(OnUsuarioCallback callback);
    void obtenerPresupuesto(OnPresupuestoCallback callback);
    void actualizarPresupuesto(double presupuesto, OnPresupuestoCallback callback);
    LiveData<Double> getPresupuestoLiveData();
    void actualizarNombre(String nombre, OnActualizarNombreCallback callback);

    interface OnUsuarioCallback {
        void onSuccess(UsuarioEntity usuario);
        void onError();
    }

    interface OnPresupuestoCallback {
        void onSuccess(double presupuesto);
        void onError();
    }

    interface OnActualizarNombreCallback {
        void onSuccess();
        void onError();
    }
}

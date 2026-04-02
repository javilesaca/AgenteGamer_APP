package com.miapp.agentegamer.domain.repository;

import androidx.lifecycle.LiveData;
import com.miapp.agentegamer.data.model.UsuarioEntity;

public interface UserRepository {
    void obtenerUsuario(OnUsuarioCallback callback);
    void obtenerPresupuesto(OnPresupuestoCallback callback);
    void actualizarPresupuesto(double presupuesto, OnPresupuestoCallback callback);
    LiveData<Double> getPresupuestoLiveData();
    LiveData<String> getMonedaLiveData();
    void actualizarNombre(String nombre, OnActualizarNombreCallback callback);
    void actualizarMoneda(String moneda, OnMonedaCallback callback);
    void resetForNewUser();

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

    interface OnMonedaCallback {
        void onSuccess(String moneda);
        void onError();
    }
}

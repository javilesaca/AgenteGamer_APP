package com.miapp.agentegamer.domain.usecase;

import com.miapp.agentegamer.domain.repository.UserRepository;

import javax.inject.Inject;

public class UpdateSettingsUseCase {

    private final UserRepository userRepository;

    @Inject
    public UpdateSettingsUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updatePresupuesto(double nuevoPresupuesto, UserRepository.OnPresupuestoCallback callback) {
        if (nuevoPresupuesto < 0) {
            callback.onError();
            return;
        }
        userRepository.actualizarPresupuesto(nuevoPresupuesto, callback);
    }

    public void updateMoneda(String moneda, UserRepository.OnMonedaCallback callback) {
        if (moneda == null || moneda.isEmpty()) {
            callback.onError();
            return;
        }
        userRepository.actualizarMoneda(moneda, callback);
    }
}

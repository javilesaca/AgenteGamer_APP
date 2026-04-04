package com.miapp.agentegamer.domain.usecase;

import com.miapp.agentegamer.domain.repository.UserRepository;

import javax.inject.Inject;

/**
 * Caso de uso para actualizar la configuración del usuario.
 * 
 * <p>Encapsula la lógica de negocio relacionada con la actualización de
 * ajustes del usuario, específicamente el presupuesto mensual y la moneda
 * preferida. Este caso de uso validando los datos antes de delegar la
 * persistencia al repositorio.</p>
 * 
 * <p>Validaciones realizadas:</p>
 * <ul>
 *   <li>El presupuesto debe ser mayor o igual a 0</li>
 *   <li>La moneda no puede ser null ni estar vacía</li>
 * </ul>
 * 
 * <p>Este caso de uso sigue el patrón de diseñoInteractor/Delegate y forma
 * parte de la capa de dominio de Clean Architecture.</p>
 * 
 * @author AgenteGamer
 * @version 1.0
 * @since 2024
 * @see UserRepository
 */
public class UpdateSettingsUseCase {

    /** Repositorio de usuario para operaciones de persistencia. */
    private final UserRepository userRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param userRepository Repositorio de usuario inyectado
     */
    @Inject
    public UpdateSettingsUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Actualiza el presupuesto mensual del usuario.
     * 
     * <p>Valida que el nuevo presupuesto sea un valor válido (≥ 0) antes
     * de delegar la actualización al repositorio.</p>
     * 
     * @param nuevoPresupuesto Nuevo valor del presupuesto mensual en euros
     * @param callback Callback para notificar el resultado de la operación
     */
    public void updatePresupuesto(double nuevoPresupuesto, UserRepository.OnPresupuestoCallback callback) {
        if (nuevoPresupuesto < 0) {
            callback.onError();
            return;
        }
        userRepository.actualizarPresupuesto(nuevoPresupuesto, callback);
    }

    /**
     * Actualiza la moneda preferida del usuario.
     * 
     * <p>Valida que la moneda no sea null ni esté vacía antes de
     * delegar la actualización al repositorio.</p>
     * 
     * @param moneda Código de la nueva moneda (ej: "EUR", "USD", "GBP")
     * @param callback Callback para notificar el resultado de la operación
     */
    public void updateMoneda(String moneda, UserRepository.OnMonedaCallback callback) {
        if (moneda == null || moneda.isEmpty()) {
            callback.onError();
            return;
        }
        userRepository.actualizarMoneda(moneda, callback);
    }
}

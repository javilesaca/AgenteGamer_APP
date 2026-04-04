package com.miapp.agentegamer.domain.repository;

import androidx.lifecycle.LiveData;
import com.miapp.agentegamer.data.model.UsuarioEntity;

/**
 * Interfaz del repositorio para la gestión de datos del usuario.
 * 
 * <p>Define el contrato para las operaciones de acceso a datos relacionadas
 * con la configuración y perfil del usuario. Gestiona el presupuesto mensual,
 * la moneda preferida y otros ajustes personales.</p>
 * 
 * <p>Esta interfaz proporciona:</p>
 * <ul>
 *   <li>Obtener información del usuario (nombre, presupuesto)</li>
 *   <li>Actualizar presupuesto mensual</li>
 *   <li>Cambiar moneda de visualización</li>
 *   <li>Actualizar nombre del usuario</li>
 *   <li>Obtener datos en tiempo real mediante LiveData</li>
 *   <li>Restablecer datos para nuevo usuario</li>
 * </ul>
 * 
 * @author AgenteGamer
 * @version 1.0
 * @since 2024
 * @see UsuarioEntity
 */
public interface UserRepository {
    
    /**
     * Obtiene los datos completos del usuario.
     * 
     * <p>Retorna toda la información del perfil del usuario, incluyendo
     * nombre, presupuesto y moneda preferida.</p>
     * 
     * @param callback Interfaz para retornar el resultado asíncronamente
     */
    void obtenerUsuario(OnUsuarioCallback callback);
    
    /**
     * Obtiene el presupuesto mensual actual del usuario.
     * 
     * @param callback Interfaz para retornar el resultado asíncronamente
     */
    void obtenerPresupuesto(OnPresupuestoCallback callback);
    
    /**
     * Actualiza el presupuesto mensual del usuario.
     * 
     * <p>El nuevo presupuesto debe ser un valor positivo. Si es menor a 0,
     * se invoca onError() del callback.</p>
     * 
     * @param presupuesto Nuevo valor del presupuesto mensual en euros
     * @param callback Interfaz para retornar el resultado asíncronamente
     */
    void actualizarPresupuesto(double presupuesto, OnPresupuestoCallback callback);
    
    /**
     * Obtiene el presupuesto como LiveData para observar cambios en tiempo real.
     * 
     * @return LiveData con el presupuesto actual del usuario
     */
    LiveData<Double> getPresupuestoLiveData();
    
    /**
     * Obtiene la moneda preferida como LiveData.
     * 
     * @return LiveData con el código de moneda (ej: "EUR", "USD")
     */
    LiveData<String> getMonedaLiveData();
    
    /**
     * Actualiza el nombre del usuario en el perfil.
     * 
     * @param nombre Nuevo nombre para el usuario
     * @param callback Interfaz para retornar el resultado asíncronamente
     */
    void actualizarNombre(String nombre, OnActualizarNombreCallback callback);
    
    /**
     * Actualiza la moneda preferida del usuario.
     * 
     * @param moneda Código de moneda (ej: "EUR", "USD", "GBP")
     * @param callback Interfaz para retornar el resultado asíncronamente
     */
    void actualizarMoneda(String moneda, OnMonedaCallback callback);
    
    /**
     * Restablece todos los datos del usuario para comenzar como usuario nuevo.
     * 
     * <p>Elimina todos los gastos, wishlist y otros datos asociados al
     * usuario actual, permitiendo configurar un nuevo perfil desde cero.</p>
     */
    void resetForNewUser();

    /**
     * Callback para operaciones que retornan datos del usuario.
     */
    interface OnUsuarioCallback {
        /**
         * Callback invocado cuando la operación es exitosa.
         * 
         * @param usuario Entidad con los datos del usuario
         */
        void onSuccess(UsuarioEntity usuario);
        
        /**
         * Callback invocado cuando ocurre un error en la operación.
         */
        void onError();
    }

    /**
     * Callback para operaciones relacionadas con el presupuesto.
     */
    interface OnPresupuestoCallback {
        /**
         * Callback invocado cuando la operación es exitosa.
         * 
         * @param presupuesto Valor del presupuesto
         */
        void onSuccess(double presupuesto);
        
        /**
         * Callback invocado cuando ocurre un error en la operación.
         */
        void onError();
    }

    /**
     * Callback para operaciones de actualización de nombre.
     */
    interface OnActualizarNombreCallback {
        /**
         * Callback invocado cuando la actualización fue exitosa.
         */
        void onSuccess();
        
        /**
         * Callback invocado cuando ocurre un error en la operación.
         */
        void onError();
    }

    /**
     * Callback para operaciones relacionadas con la moneda.
     */
    interface OnMonedaCallback {
        /**
         * Callback invocado cuando la operación es exitosa.
         * 
         * @param moneda Código de moneda actualizado
         */
        void onSuccess(String moneda);
        
        /**
         * Callback invocado cuando ocurre un error en la operación.
         */
        void onError();
    }
}

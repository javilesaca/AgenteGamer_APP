package com.miapp.agentegamer.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.miapp.agentegamer.data.model.UsuarioEntity;
import com.miapp.agentegamer.domain.repository.UserRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repositorio para gestionar la información del usuario autenticado.
 * <p>
 * Esta clase actua como intermediario entre la aplicación y Firebase Firestore
 * para todas las operaciones relacionadas con datos de usuario: perfil,
 * presupuesto mensual, moneda preferida, etc.
 * <p>
 * Implementa el patrón Singleton (anotado con {@link Singleton}) para garantizar
 * que solo exista una instancia en toda la aplicación, ya que mantiene listeners
 * activos a documentos de Firestore que necesitan persistencia.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Obtener datos del perfil de usuario desde Firestore</li>
 *   <li>Gestionar presupuesto mensual del usuario</li>
 *   <li>Gestionar preferencia de moneda</li>
 *   <li>Proporcionar LiveData reactivo para cambios en tiempo real</li>
 * </ul>
 *
 * @see UsuarioEntity
 * @see FirebaseFirestore
 */
@Singleton
public class UserRepositoryImpl implements UserRepository {

    /** Instancia de Firebase Authentication */
    private final FirebaseAuth auth;
    
    /** Instancia de Firebase Firestore */
    private final FirebaseFirestore db;
    
    /** LiveData para el presupuesto mensual del usuario */
    private final MutableLiveData<Double> presupuestoLiveData = new MutableLiveData<>();
    
    /** LiveData para la moneda preferida del usuario */
    private MutableLiveData<String> monedaLiveData = new MutableLiveData<>("");
    
    /** Listener para cambios en el documento de presupuesto */
    private ListenerRegistration presupuestoListener;
    
    /** Listener para cambios en el documento de moneda */
    private ListenerRegistration monedaListener;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param auth Instancia de FirebaseAuth
     * @param db   Instancia de FirebaseFirestore
     */
    @Inject
    public UserRepositoryImpl(FirebaseAuth auth, FirebaseFirestore db) {
        this.auth = auth;
        this.db = db;
    }

    /**
     * Obtiene los datos completos del usuario desde Firestore.
     * <p>
     * Realiza una consulta al documento del usuario en la colección "users"
     * y retorna un objeto {@link UsuarioEntity} con toda la información
     * del perfil.
     *
     * @param callback Interfaz para recibir el resultado (éxito o error)
     */
    @Override
    public void obtenerUsuario(OnUsuarioCallback callback) {

        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            callback.onError();
            return;
        }

        db.collection("users")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        UsuarioEntity usuario = doc.toObject(UsuarioEntity.class);
                        callback.onSuccess(usuario);

                    } else {
                        callback.onError();
                    }
                })
                .addOnFailureListener(e -> callback.onError());
    }

    /**
     * Obtiene el presupuesto mensual del usuario.
     * <p>
     * Recupera el campo "presupuestoMensual" del documento del usuario.
     * Si no existe, retorna el valor por defecto de 100.0.
     *
     * @param callback Interfaz para recibir el resultado
     */
    @Override
    public void obtenerPresupuesto(OnPresupuestoCallback callback) {

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onError();
            return;
        }

        db.collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Double presupuesto = doc.getDouble("presupuestoMensual");
                        callback.onSuccess(presupuesto != null ? presupuesto : 100.0);
                    } else {
                        callback.onError();
                    }
                })
                .addOnFailureListener(e -> callback.onError());
    }

    /**
     * Actualiza el presupuesto mensual del usuario.
     *
     * @param presupuesto Nuevo valor del presupuesto mensual
     * @param callback    Interfaz para recibir el resultado
     */
    @Override
    public void actualizarPresupuesto(double presupuesto, OnPresupuestoCallback callback) {

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onError();
            return;
        }

        db.collection("users")
                .document(user.getUid())
                .update("presupuestoMensual", presupuesto)
                .addOnSuccessListener(unused -> callback.onSuccess(presupuesto))
                .addOnFailureListener(e -> callback.onError());
    }

    /**
     * Actualiza el nombre del usuario.
     *
     * @param nombre   Nuevo nombre a establecer
     * @param callback Interfaz para recibir el resultado
     */
    @Override
    public void actualizarNombre(String nombre, OnActualizarNombreCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onError();
            return;
        }
        
        db.collection("users")
                .document(user.getUid())
                .update("nombre", nombre)
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError());
    }

    /**
     * Actualiza la moneda preferida del usuario.
     *
     * @param moneda   Código de moneda (ej: "EUR", "USD", "MXN")
     * @param callback Interfaz para recibir el resultado
     */
    @Override
    public void actualizarMoneda(String moneda, OnMonedaCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onError();
            return;
        }

        db.collection("users")
                .document(user.getUid())
                .update("moneda", moneda)
                .addOnSuccessListener(unused -> callback.onSuccess(moneda))
                .addOnFailureListener(e -> callback.onError());
    }

    /**
     * Reinicia el estado del repositorio para un nuevo usuario.
     * <p>
     * Remueve los listeners activos y resetea los valores de LiveData.
     * Debe llamarse cuando el usuario cierra sesión o cuando se cambia
     * de usuario para evitar fugas de memoria y datos stale.
     */
    @Override
    public void resetForNewUser() {
        // Remove old snapshot listeners
        if (presupuestoListener != null) {
            presupuestoListener.remove();
            presupuestoListener = null;
        }
        if (monedaListener != null) {
            monedaListener.remove();
            monedaListener = null;
        }
        // Reset LiveData values
        presupuestoLiveData.setValue(null);
        monedaLiveData.setValue(null);
    }

    /**
     * Obtiene un LiveData reactivo del presupuesto mensual.
     * <p>
     * Establece un listener en tiempo real al documento del usuario en Firestore.
     * Cualquier cambio en el presupuesto se refleja automáticamente en el
     * LiveData retornado. El valor por defecto es 100.0 si no hay datos.
     *
     * @return LiveData que emite el presupuesto actual del usuario
     */
    @Override
    public LiveData<Double> getPresupuestoLiveData() {

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            presupuestoLiveData.setValue(100.0);
            return presupuestoLiveData;
        }

        // Remove previous listener if exists
        if (presupuestoListener != null) {
            presupuestoListener.remove();
        }

        presupuestoListener = db.collection("users").document(user.getUid())
                .addSnapshotListener((doc, error) -> {

                    if (error !=null || doc == null || !doc.exists()) {
                        presupuestoLiveData.setValue(100.0);
                        return;
                    }

                    // Intentar leer presupuestoMensual primero, luego presupuesto
                    Double presupuestoMensual = doc.getDouble("presupuestoMensual");
                    Double presupuesto = doc.getDouble("presupuesto");

                    // Usar presupuestoMensual si existe, sino presupuesto, sino 100
                    Double valorFinal = presupuestoMensual != null ? presupuestoMensual :
                                       (presupuesto != null ? presupuesto : 100.0);

                    presupuestoLiveData.setValue(valorFinal);
                });

        return presupuestoLiveData;
    }

    /**
     * Obtiene un LiveData reactivo de la moneda preferida.
     * <p>
     * Establece un listener en tiempo real al documento del usuario en Firestore.
     * Cualquier cambio en la moneda se refleja automáticamente en el
     * LiveData retornado. El valor por defecto es "EUR" si no hay datos.
     *
     * @return LiveData que emite la moneda actual del usuario
     */
    @Override
    public LiveData<String> getMonedaLiveData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            monedaLiveData.setValue("EUR");
            return monedaLiveData;
        }

        // Remove previous listener if exists
        if (monedaListener != null) {
            monedaListener.remove();
        }

        monedaListener = db.collection("users").document(user.getUid())
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null || snapshot == null || !snapshot.exists()) {
                        monedaLiveData.setValue("EUR");
                        return;
                    }
                    String moneda = snapshot.getString("moneda");
                    monedaLiveData.setValue(moneda != null && !moneda.isEmpty() ? moneda : "EUR");
                });

        return monedaLiveData;
    }
}

package com.miapp.agentegamer.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miapp.agentegamer.data.model.UsuarioEntity;
import com.miapp.agentegamer.domain.repository.UserRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Data source that handle user information persistence operations.
 */
@Singleton
public class UserRepositoryImpl implements UserRepository {

    private final FirebaseAuth auth;
    private final FirebaseFirestore db;
    private final MutableLiveData<Double> presupuestoLiveData = new MutableLiveData<>();

    @Inject
    public UserRepositoryImpl(FirebaseAuth auth, FirebaseFirestore db) {
        this.auth = auth;
        this.db = db;
    }

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

    @Override
    public LiveData<Double> getPresupuestoLiveData() {

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            presupuestoLiveData.setValue(100.0);
            return presupuestoLiveData;
        }

        db.collection("users").document(user.getUid())
                .addSnapshotListener((doc, error) -> {

                    if (error !=null || doc == null || !doc.exists()) {
                        presupuestoLiveData.setValue(100.0);
                        return;
                    }

                    Double presupuesto = doc.getDouble("presupuestoMensual");
                    presupuestoLiveData.setValue(
                            presupuesto != null ? presupuesto : 100.0
                    );
                });

        return presupuestoLiveData;
    }
}
package com.miapp.agentegamer.data.repository;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.miapp.agentegamer.data.local.dao.LanzamientoDao;
import com.miapp.agentegamer.data.local.entity.LanzamientoEntity;
import com.miapp.agentegamer.domain.repository.LanzamientoRepository;

import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

public class LanzamientoRepositoryImpl implements LanzamientoRepository {

    private final LanzamientoDao lanzamientoDao;

    @Inject
    public LanzamientoRepositoryImpl(LanzamientoDao lanzamientoDao) {
        this.lanzamientoDao = lanzamientoDao;
    }

    private String getCurrentUserId() {
        return FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "";
    }

    @Override
    public LiveData<List<LanzamientoEntity>> getProximosLanzamientos(long hoy) {
        return lanzamientoDao.getProximosLanzamientos(getCurrentUserId(), hoy);
    }

    @Override
    public void insertar(LanzamientoEntity lanzamiento) {
        Executors.newSingleThreadExecutor().execute(() -> {
            lanzamiento.userId = getCurrentUserId();
            lanzamientoDao.insertar(lanzamiento);
        });
    }

    @Override
    public void borrarTodos() {
        Executors.newSingleThreadExecutor().execute(() -> lanzamientoDao.borrarTodos(getCurrentUserId()));
    }
}

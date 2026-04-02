package com.miapp.agentegamer.data.repository;

import androidx.lifecycle.LiveData;

import com.miapp.agentegamer.data.local.dao.WishlistDao;
import com.miapp.agentegamer.data.local.entity.WishlistEntity;
import com.miapp.agentegamer.domain.repository.WishlistRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

public class WishlistRepositoryImpl implements WishlistRepository {

    private final WishlistDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Inject
    public WishlistRepositoryImpl(WishlistDao dao) {
        this.dao = dao;
    }

    @Override
    public void insertar(WishlistEntity juego) {
        executor.execute(() -> dao.insert(juego));
    }

    @Override
    public LiveData<List<WishlistEntity>> getWishlist() {
        return dao.getWishlist();
    }

    @Override
    public void actualizar(WishlistEntity juego) {
        executor.execute(() -> dao.actualizar(juego));
    }

    @Override
    public void borrar(WishlistEntity juego) {
        executor.execute(() -> dao.borrar(juego));
    }

    @Override
    public List<WishlistEntity> getWishlistSync() {
        return dao.getWishlistSync();
    }

    @Override
    public Double getTotalGastado() {
        return dao.getTotalGastado();
    }
}

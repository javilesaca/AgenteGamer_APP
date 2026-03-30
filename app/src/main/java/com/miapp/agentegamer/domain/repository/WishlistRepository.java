package com.miapp.agentegamer.domain.repository;

import androidx.lifecycle.LiveData;

import com.miapp.agentegamer.data.local.entity.WishlistEntity;

import java.util.List;

public interface WishlistRepository {
    void insertar(WishlistEntity juego);
    LiveData<List<WishlistEntity>> getWishlist();
    void actualizar(WishlistEntity juego);
    void borrar(WishlistEntity juego);
    List<WishlistEntity> getWishlistSync();
    Double getTotalGastado();
}

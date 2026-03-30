package com.miapp.agentegamer.ui.wishlist;

import com.miapp.agentegamer.data.local.entity.WishlistEntity;

public class WishlistItemUI {

    private final WishlistEntity juego;
    private final String evaluacion;

    public WishlistItemUI(WishlistEntity juego, String evaluacion) {
        this.juego = juego;
        this.evaluacion = evaluacion;
    }

    public WishlistEntity getJuego() {
        return juego;
    }

    public String getEvaluacion() {
        return evaluacion;
    }
}

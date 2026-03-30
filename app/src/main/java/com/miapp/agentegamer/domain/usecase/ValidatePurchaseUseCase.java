package com.miapp.agentegamer.domain.usecase;

import com.miapp.agentegamer.data.local.entity.WishlistEntity;
import com.miapp.agentegamer.domain.model.SistemaFinanciero;

import javax.inject.Inject;

public class ValidatePurchaseUseCase {

    @Inject
    public ValidatePurchaseUseCase() {
    }

    public String validate(WishlistEntity juego, double presupuesto, double gastoActual) {
        SistemaFinanciero sistema = new SistemaFinanciero(presupuesto);
        return sistema.evaluarCompra(juego.getPrecioEstimado(), gastoActual);
    }
}

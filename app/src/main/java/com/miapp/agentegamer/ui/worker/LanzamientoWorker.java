package com.miapp.agentegamer.ui.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.miapp.agentegamer.data.local.entity.WishlistEntity;
import com.miapp.agentegamer.domain.repository.WishlistRepository;
import com.miapp.agentegamer.util.FechaUtils;
import com.miapp.agentegamer.util.NotificationHelper;

import java.util.List;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;

@HiltWorker
public class LanzamientoWorker extends Worker {

    private final WishlistRepository repo;

    @AssistedInject
    public LanzamientoWorker(
            @Assisted @NonNull Context context,
            @Assisted @NonNull WorkerParameters params,
            WishlistRepository wishlistRepository) {
        super(context, params);
        this.repo = wishlistRepository;
    }

    @NonNull
    @Override
    public Result doWork() {

        List<WishlistEntity> juegos = repo.getWishlistSync();
        
        // Verificar null para evitar NPE
        if (juegos == null) {
            return Result.success();
        }

        for (WishlistEntity juego: juegos) {
            if (juego == null) continue;
            if(salePronto(juego)) {
                lanzarNotificacion(juego);
            }
        }
        return Result.success();
    }

    private void lanzarNotificacion(WishlistEntity juego) {
        String titulo = "Próximo lanzamiento";
        String texto = juego.getNombre() + " sale pronto";

        NotificationHelper.mostrar(getApplicationContext(), titulo, texto);
    }

    private boolean salePronto(WishlistEntity juego) {
        if (juego.getFechaLanzamiento() == null)
            return false;

        long dias = FechaUtils.diasHasta(juego.getFechaLanzamiento());
        return dias >= 0 && dias <= 7;
    }
}

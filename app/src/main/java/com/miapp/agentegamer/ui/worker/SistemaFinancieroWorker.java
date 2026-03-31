package com.miapp.agentegamer.ui.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.miapp.agentegamer.data.local.entity.WishlistEntity;
import com.miapp.agentegamer.domain.repository.GastoRepository;
import com.miapp.agentegamer.domain.repository.UserRepository;
import com.miapp.agentegamer.domain.repository.WishlistRepository;
import com.miapp.agentegamer.domain.usecase.ValidatePurchaseUseCase;
import com.miapp.agentegamer.util.FechaUtils;
import com.miapp.agentegamer.util.NotificationHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;

@HiltWorker
public class SistemaFinancieroWorker extends Worker {

    private final WishlistRepository wishlistRepo;
    private final GastoRepository gastoRepo;
    private final UserRepository userRepository;
    private final ValidatePurchaseUseCase validatePurchaseUseCase;

    @AssistedInject
    public SistemaFinancieroWorker(
            @Assisted @NonNull Context context,
            @Assisted @NonNull WorkerParameters params,
            WishlistRepository wishlistRepo,
            GastoRepository gastoRepo,
            UserRepository userRepository,
            ValidatePurchaseUseCase validatePurchaseUseCase) {
        super(context, params);
        this.wishlistRepo = wishlistRepo;
        this.gastoRepo = gastoRepo;
        this.userRepository = userRepository;
        this.validatePurchaseUseCase = validatePurchaseUseCase;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            List<WishlistEntity> wishlist = wishlistRepo.getWishlistSync();

            CompletableFuture<Double> presupuestoFuture = new CompletableFuture<>();
            CompletableFuture<Double> totalGastadoFuture = new CompletableFuture<>();

            userRepository.obtenerPresupuesto(new UserRepository.OnPresupuestoCallback() {
                @Override
                public void onSuccess(double presupuesto) {
                    presupuestoFuture.complete(presupuesto);
                }

                @Override
                public void onError() {
                    presupuestoFuture.completeExceptionally(new RuntimeException("Failed to fetch presupuesto"));
                }
            });

            gastoRepo.getTotalGastadoMesSync(totalGastado -> {
                try {
                    totalGastadoFuture.complete(totalGastado);
                } catch (Exception e) {
                    totalGastadoFuture.completeExceptionally(e);
                }
            });

            Double presupuesto = presupuestoFuture.get(30, TimeUnit.SECONDS);
            Double totalGastado = totalGastadoFuture.get(30, TimeUnit.SECONDS);

            if (presupuesto == null) {
                return Result.failure();
            }

            for (WishlistEntity juego : wishlist) {
                String evaluacion = validatePurchaseUseCase.validate(juego, presupuesto, totalGastado);

                if (!evaluacion.equals("✅ Compra recomendada")) {
                    lanzarNotificacionNoComprar(juego);
                    break;
                }
            }

            avisarLanzamientosProximos(wishlist);

            return Result.success();
        } catch (TimeoutException e) {
            return Result.retry();
        } catch (Exception e) {
            return Result.failure();
        }
    }

    private void avisarLanzamientosProximos(List<WishlistEntity> wishlist) {
        for (WishlistEntity juego : wishlist) {
            if (juego.getFechaLanzamiento() == null) continue;

            long dias = FechaUtils.diasHasta(juego.getFechaLanzamiento());

            if (dias > 0 && dias <= 7) {
                NotificationHelper.mostrar(
                        getApplicationContext(),
                        "Próximo lanzamiento",
                        juego.getNombre() + " sale en " + dias + " días"
                );
            }
        }
    }

    private void lanzarNotificacionNoComprar(WishlistEntity juego) {
        NotificationHelper.mostrar(
                getApplicationContext(),
                "No es buen momento para comprar ",
                juego.getNombre() + " (" + juego.getPrecioEstimado() +
                        " €), supera tu presupuesto actual"
        );
    }
}

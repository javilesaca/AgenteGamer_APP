package com.miapp.agentegamer.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

import com.miapp.agentegamer.domain.model.SistemaFinanciero;
import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.data.local.entity.LanzamientoEntity;
import com.miapp.agentegamer.data.local.entity.WishlistEntity;
import com.miapp.agentegamer.domain.repository.GastoRepository;
import com.miapp.agentegamer.domain.repository.LanzamientoRepository;
import com.miapp.agentegamer.domain.repository.UserRepository;
import com.miapp.agentegamer.domain.repository.WishlistRepository;
import com.miapp.agentegamer.ui.wishlist.WishlistItemUI;
import com.miapp.agentegamer.util.FechaUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@HiltViewModel
public class WishlistViewModel extends AndroidViewModel {

    private final WishlistRepository repo;
    private final UserRepository userRepository;
    private final MediatorLiveData<List<WishlistItemUI>> wishlistEvaluada = new MediatorLiveData<>();
    private final LanzamientoRepository repoLanzamiento;
    private GastoRepository gastoRepo;
    private double gastoActual = 0.0;

    @Inject
    public WishlistViewModel(@NonNull Application app, WishlistRepository repo,
            UserRepository userRepository, LanzamientoRepository repoLanzamiento,
            GastoRepository gastoRepo) {
        super(app);

        this.repo = repo;
        this.repoLanzamiento = repoLanzamiento;
        this.userRepository = userRepository;
        this.gastoRepo = gastoRepo;

        LiveData<List<WishlistEntity>> wishListSource = repo.getWishlist();
        LiveData<Double> presupuestoSource = userRepository.getPresupuestoLiveData();
        LiveData<Double> gastoSource = gastoRepo.getGastoMesActual();

        wishlistEvaluada.addSource(wishListSource, lista ->
                recalcularEvaluacion(lista, presupuestoSource.getValue(), gastoSource.getValue()));

        wishlistEvaluada.addSource(presupuestoSource, presupuesto ->
                recalcularEvaluacion(wishListSource.getValue(), presupuesto, gastoSource.getValue()));

        wishlistEvaluada.addSource(gastoSource, gasto ->
                recalcularEvaluacion(wishListSource.getValue(), presupuestoSource.getValue(), gasto));
    }

    public void insertar(WishlistEntity juego) {
        repo.insertar(juego);
    }

    public void borrar(WishlistEntity juego) { repo.borrar(juego);}

    public LiveData<Double> getGastoMes() {
        return gastoRepo.getGastoMesActual();
    }

    public LiveData<List<WishlistItemUI>> getWishList() { return wishlistEvaluada; }

    public void actualizar(WishlistEntity juego) {
        repo.actualizar(juego);
    }

    public void comprarJuego(WishlistEntity juego, double precioFinal) {

        Date hoy = new Date();
        Date fechaLanzamiento = FechaUtils.parseFecha(juego.getFechaLanzamiento());

        //Si es un lanzamiento se guarda en lanzamientos
        if (fechaLanzamiento != null && fechaLanzamiento.after(hoy)) {

            LanzamientoEntity lanzamiento = new LanzamientoEntity(
                    juego.getGameId(),
                    juego.getNombre(),
                    fechaLanzamiento.getTime(),
                    precioFinal,
                    "", "", 0
            );
            repoLanzamiento.insertar(lanzamiento);
        } else {

            //Crear gasto desde el juego
            GastoEntity gasto = new GastoEntity(
                    juego.getNombre(),
                    precioFinal,
                    FechaUtils.ahoraTimestamp()
            );
            //Insertar en Gastos
            gastoRepo.insertarGasto(gasto);
            //Eliminar de whishlist
            repo.borrar(juego);
        }
    }

    private void recalcularEvaluacion(
            List<WishlistEntity> lista,
            Double presupuesto,
            Double gastoActual
    ) {

        if (lista == null || presupuesto == null || gastoActual == null) {
            wishlistEvaluada.setValue(new ArrayList<>());
            return;
        }

        SistemaFinanciero sistema = new SistemaFinanciero(presupuesto);

        List<WishlistItemUI> listaUI = new ArrayList<>();

        for (WishlistEntity juego : lista) {

            String evaluacion = sistema.evaluarCompra(
                    juego.getPrecioEstimado(),
                    gastoActual
            );

            listaUI.add(new WishlistItemUI(juego, evaluacion));
        }

        wishlistEvaluada.setValue(listaUI);
    }
}

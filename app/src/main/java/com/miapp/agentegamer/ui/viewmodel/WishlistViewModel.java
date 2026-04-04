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

/**
 * WishlistViewModel
 * -----------------
 * ViewModel que gestiona el estado y los datos de la lista de deseos (wishlist)
 * del usuario. Utiliza el patrón MVVM y mediator para combinar múltiples fuentes de datos.
 * 
 * Funcionalidades:
 * - Gestiona la lista de deseos desde el repositorio
 * - Evalúa cada juego según el presupuesto actual y los gastos del mes
 * - Provee la wishlist con evaluaciones financieras (recomendada/ajustada/no recomendada)
 * - Permite comprar juegos (mover a gastos o lanzamientos)
 * - Observa cambios en presupuesto y gastos para recalcular evaluaciones en tiempo real
 * - Provee la moneda actual del usuario
 * 
 * @see WishlistRepository
 * @see SistemaFinanciero
 * @see WishlistItemUI
 */
@HiltViewModel
public class WishlistViewModel extends AndroidViewModel {

    // Repositorio de wishlist para acceder a los datos
    private final WishlistRepository repo;
    // Repositorio de usuario para obtener presupuesto y moneda
    private final UserRepository userRepository;
    // MediatorLiveData que combina wishlist con evaluaciones financieras
    private final MediatorLiveData<List<WishlistItemUI>> wishlistEvaluada = new MediatorLiveData<>();
    // Repositorio de lanzamientos para guardar juegos comprados que son lanzamientos futuros
    private final LanzamientoRepository repoLanzamiento;
    // Repositorio de gastos para crear gastos y obtener el gasto actual del mes
    private GastoRepository gastoRepo;
    // Gasto actual del mes para calcular evaluaciones
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

    /**
     * Inserta un nuevo juego en la wishlist.
     * 
     * @param juego Entidad del juego a agregar a la wishlist
     */
    public void insertar(WishlistEntity juego) {
        repo.insertar(juego);
    }

    /**
     * Elimina un juego de la wishlist.
     * 
     * @param juego Entidad del juego a eliminar
     */
    public void borrar(WishlistEntity juego) { repo.borrar(juego);}

    /**
     * Retorna el LiveData del gasto del mes actual.
     * 
     * @return LiveData con el gasto total del mes
     */
    public LiveData<Double> getGastoMes() {
        return gastoRepo.getGastoMesActual();
    }

    /**
     * Retorna el LiveData de la wishlist evaluada financieramente.
     * Cada elemento incluye la evaluación (recomendada/ajustada/no recomendada).
     * 
     * @return LiveData con la lista de wishlist evaluada
     */
    public LiveData<List<WishlistItemUI>> getWishList() { return wishlistEvaluada; }

    /**
     * Retorna el LiveData de la moneda actual del usuario.
     * 
     * @return LiveData con el código de moneda (EUR, USD, GBP)
     */
    public LiveData<String> getMonedaLiveData() { return userRepository.getMonedaLiveData(); }

    /**
     * Actualiza un juego existente en la wishlist.
     * 
     * @param juego Entidad del juego a actualizar
     */
    public void actualizar(WishlistEntity juego) {
        repo.actualizar(juego);
    }

    /**
     * Procesa la compra de un juego de la wishlist.
     * Si el juego es un lanzamiento futuro, lo guarda en Lanzamientos.
     * Si ya está lanzado, crea un Gasto y elimina el juego de la wishlist.
     * 
     * @param juego Entidad del juego a comprar
     * @param precioFinal Precio final real de la compra
     */
    public void comprarJuego(WishlistEntity juego, double precioFinal) {

        Date hoy = new Date();
        Date fechaLanzamiento = FechaUtils.parseFecha(juego.getFechaLanzamiento());

        //Si es un lanzamiento se guarda en lanzamientos
        if (fechaLanzamiento != null && fechaLanzamiento.after(hoy)) {

            LanzamientoEntity lanzamiento = new LanzamientoEntity(
                    null,
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
                    null,
                    juego.getNombre(),
                    precioFinal,
                    FechaUtils.ahoraTimestamp(),
                    juego.getImagenUrl()
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

        if (lista == null) {
            wishlistEvaluada.setValue(new ArrayList<>());
            return;
        }

        // Defaults si alguna fuente aún no emitió
        double presup = presupuesto != null ? presupuesto : 150.0;
        double gasto = gastoActual != null ? gastoActual : 0.0;

        SistemaFinanciero sistema = new SistemaFinanciero(presup);

        List<WishlistItemUI> listaUI = new ArrayList<>();

        for (WishlistEntity juego : lista) {

            String evaluacion = sistema.evaluarCompra(
                    juego.getPrecioEstimado(),
                    gasto
            );

            listaUI.add(new WishlistItemUI(juego, evaluacion));
        }

        wishlistEvaluada.setValue(listaUI);
    }
}

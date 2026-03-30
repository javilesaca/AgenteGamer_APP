package com.miapp.agentegamer.ui.viewmodel;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.Observer;

import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.domain.model.Gasto;
import com.miapp.agentegamer.domain.model.SistemaFinanciero;
import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.domain.repository.GastoRepository;
import com.miapp.agentegamer.ui.model.EstadoFinancieroUI;

import java.util.ArrayList;
import java.util.List;

@HiltViewModel
public class GastoViewModel extends AndroidViewModel {

    private final GastoRepository repository;
    private final LiveData<List<GastoEntity>> listaGastos;
    protected SistemaFinanciero sistemaFinanciero;
    private final MutableLiveData<EstadoFinancieroUI> estadoUI = new MutableLiveData<>();
    private final Observer<List<GastoEntity>> gastosObserver;

    @Inject
    public GastoViewModel(@NonNull Application application, GastoRepository repository) {
        super(application);
        this.repository = repository;

        listaGastos = repository.obtenerGastos();
        gastosObserver = gastos -> recalcularEstado(gastos);
        listaGastos.observeForever(gastosObserver);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (listaGastos != null && gastosObserver != null) {
            listaGastos.removeObserver(gastosObserver);
        }
    }

    //=========================
    // INYECCIÓN DEL AGENTE (FIREBASE)
    //=========================
    public void setSistemaFinanciero(SistemaFinanciero sistema) {
        this.sistemaFinanciero = sistema;
        recalcularEstado(listaGastos.getValue());
    }

    //===========================
    // OBSERVABLES
    //===========================
    public LiveData<List<GastoEntity>> getListaGastos() {
        return listaGastos;
    }

    public LiveData<List<Gasto>> getListaGastosDomain() {
        return Transformations.map(listaGastos, entities -> toDomainGastos(entities));
    }

    public LiveData<EstadoFinancieroUI> getEstadoUI() {
        return estadoUI;
    }

    //============================
    // LÓGICA
    //============================
    private void recalcularEstado(List<GastoEntity> gastos) {

        if (sistemaFinanciero == null || gastos == null) return;

        List<Gasto> domainGastos = toDomainGastos(gastos);

        double total = sistemaFinanciero.calcularTotalGastos(domainGastos);
        double porcentaje = sistemaFinanciero.calcularPorcentajeGastado(domainGastos);

        SistemaFinanciero.EstadoFinanciero estado = sistemaFinanciero.obtenerEstado(total);

        String mensaje = sistemaFinanciero.generarRecomendacion(domainGastos);

        int color = switch (estado) {
            case VERDE -> R.color.estado_verde;
            case AMARILLO -> R.color.estado_amarillo;
            case ROJO -> R.color.estado_rojo;
        };

        estadoUI.setValue( new EstadoFinancieroUI(estado, mensaje, color, porcentaje));

    }

    private List<Gasto> toDomainGastos(List<GastoEntity> entities) {
        if (entities == null) return new ArrayList<>();
        List<Gasto> result = new ArrayList<>();
        for (GastoEntity entity : entities) {
            if (entity != null) {
                result.add(new Gasto(
                        String.valueOf(entity.getId()),
                        entity.getNombreJuego(),
                        entity.getPrecio()
                ));
            }
        }
        return result;
    }

    //=============================
    // CRUD
    //=============================
    public void insertar(GastoEntity gasto) {
        repository.insertarGasto(gasto);
    }

    public void actualizar(GastoEntity gasto) {
        repository.actualizarGasto(gasto);
    }

    public void borrar(GastoEntity gasto) {
        repository.borrarGasto(gasto);
    }

    //Función solo para dev
    public void borrarTodosLosGastos() {
        repository.borrarTodosLosGastos();
    }

}

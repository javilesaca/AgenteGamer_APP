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
import com.miapp.agentegamer.domain.model.MonthlyExpense;
import com.miapp.agentegamer.data.local.dao.GastoDao.MonthlyTotal;
import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.domain.repository.GastoRepository;
import com.miapp.agentegamer.ui.model.EstadoFinancieroUI;
import com.miapp.agentegamer.util.FinancialTrendHelper;
import com.miapp.agentegamer.util.FinancialTrendHelper.TrendResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@HiltViewModel
public class GastoViewModel extends AndroidViewModel {

    private final GastoRepository repository;
    private final LiveData<List<GastoEntity>> listaGastos;
    protected SistemaFinanciero sistemaFinanciero;
    private final MutableLiveData<EstadoFinancieroUI> estadoUI = new MutableLiveData<>();
    private final Observer<List<GastoEntity>> gastosObserver;
    
    // Nuevos LiveData para Dashboard
    private final MutableLiveData<List<MonthlyExpense>> monthlyExpenses = new MutableLiveData<>();
    private final MutableLiveData<TrendResult> trendResult = new MutableLiveData<>();
    private final LiveData<List<GastoEntity>> recentGastos;
    private final ExecutorService executorService;

    @Inject
    public GastoViewModel(@NonNull Application application, GastoRepository repository) {
        super(application);
        this.repository = repository;
        this.executorService = Executors.newSingleThreadExecutor();

        listaGastos = repository.obtenerGastos();
        gastosObserver = gastos -> {
            recalcularEstado(gastos);
            loadDashboardData(); // Recargar datos del dashboard cuando cambian gastos
        };
        listaGastos.observeForever(gastosObserver);
        
        // Gastos recientes (últimos 5)
        recentGastos = repository.getRecentGastos(5);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (listaGastos != null && gastosObserver != null) {
            listaGastos.removeObserver(gastosObserver);
        }
        executorService.shutdown();
    }

    //=========================
    // INYECCIÓN DEL AGENTE (FIREBASE)
    //=========================
    public void setSistemaFinanciero(SistemaFinanciero sistema) {
        this.sistemaFinanciero = sistema;
        recalcularEstado(listaGastos.getValue());
        loadDashboardData();
    }

    //===========================
    // OBSERVABLES EXISTENTES
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

    //===========================
    // NUEVOS OBSERVABLES PARA DASHBOARD
    //===========================
    
    /**
     * Gastos mensuales para gráfico de tendencias.
     */
    public LiveData<List<MonthlyExpense>> getMonthlyExpenses() {
        return monthlyExpenses;
    }

    /**
     * Resultado de tendencia (vs mes anterior).
     */
    public LiveData<TrendResult> getTrendResult() {
        return trendResult;
    }

    /**
     * Últimos gastos para card de recientes.
     */
    public LiveData<List<GastoEntity>> getRecentGastos() {
        return recentGastos;
    }

    //============================
    // LÓGICA EXISTENTE
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

    //============================
    // NUEVA LÓGICA PARA DASHBOARD
    //============================

    /**
     * Carga datos del dashboard: tendencias mensuales y cálculo de trend.
     */
    private void loadDashboardData() {
        if (sistemaFinanciero == null) return;

        executorService.execute(() -> {
            double presupuesto = sistemaFinanciero.getPresupuestoMensual();
            
            // Obtener totales de últimos 3 meses
            List<MonthlyTotal> rawTotals = repository.getMonthlyTotalsSync(3);
            List<MonthlyExpense> expenses = new ArrayList<>();
            
            for (MonthlyTotal mt : rawTotals) {
                // Parsear mes de formato "YYYY-MM"
                String[] parts = mt.mes.split("-");
                if (parts.length == 2) {
                    int month = Integer.parseInt(parts[1]) - 1; // 0-indexed
                    int year = Integer.parseInt(parts[0]);
                    expenses.add(new MonthlyExpense(month, year, mt.total, presupuesto));
                }
            }
            
            monthlyExpenses.postValue(expenses);
            
            // Calcular tendencia si hay al menos 2 meses
            if (expenses.size() >= 2) {
                MonthlyExpense current = expenses.get(expenses.size() - 1);
                MonthlyExpense previous = expenses.get(expenses.size() - 2);
                TrendResult trend = FinancialTrendHelper.calculateTrend(
                    current.getTotal(), 
                    previous.getTotal()
                );
                trendResult.postValue(trend);
            } else if (expenses.size() == 1) {
                trendResult.postValue(new TrendResult(0, FinancialTrendHelper.TrendDirection.STABLE));
            }
        });
    }

    /**
     * Obtiene el string de recomendación para el dashboard.
     */
    public String getRecomendacionDashboard() {
        if (sistemaFinanciero == null || estadoUI.getValue() == null) {
            return "Configura tu presupuesto en Ajustes.";
        }
        
        EstadoFinancieroUI estado = estadoUI.getValue();
        double total = listaGastos.getValue() != null ? 
            calcularTotal(listaGastos.getValue()) : 0;
        
        return FinancialTrendHelper.generateRecommendation(
            estado.getEstado(),
            sistemaFinanciero.getPresupuestoMensual(),
            total
        );
    }

    private double calcularTotal(List<GastoEntity> gastos) {
        double total = 0;
        for (GastoEntity g : gastos) {
            if (g != null) total += g.getPrecio();
        }
        return total;
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

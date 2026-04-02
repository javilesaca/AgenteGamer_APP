package com.miapp.agentegamer.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.miapp.agentegamer.data.local.dao.GastoDao;
import com.miapp.agentegamer.data.local.dao.GastoDao.MonthlyTotal;
import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.domain.repository.GastoRepository;
import com.miapp.agentegamer.util.PeriodoFinancieroUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

public class GastoRepositoryImpl implements GastoRepository {

    private final GastoDao gastoDao;
    private final ExecutorService executorService;

    @Inject
    public GastoRepositoryImpl(GastoDao gastoDao, ExecutorService executorService) {
        this.gastoDao = gastoDao;
        this.executorService = executorService;
    }

    @Override
    public LiveData<List<GastoEntity>> obtenerGastos() {
        return gastoDao.getAllGastos();
    }

    @Override
    public void insertarGasto(GastoEntity gasto) {
        executorService.execute(() -> gastoDao.insertGasto(gasto));
    }

    @Override
    public void actualizarGasto(GastoEntity gasto) {
        executorService.execute(() -> gastoDao.updateGasto(gasto));
    }

    @Override
    public void borrarGasto(GastoEntity gasto) {
        executorService.execute(() -> gastoDao.deleteGasto(gasto));
    }

    @Override
    public void borrarTodosLosGastos() {
        executorService.execute(() -> gastoDao.deleteAll());
    }

    @Override
    public LiveData<Double> getGastoMesActual() {
        int mes = PeriodoFinancieroUtils.getMesActual();
        int anio = PeriodoFinancieroUtils.getAnioActual();

        return gastoDao.getGastoTotalMes(mes, anio);
    }

    @Override
    public void getTotalGastadoMesSync(OnTotalGastadoCallback callback) {
        executorService.execute(() -> {
            int mes = PeriodoFinancieroUtils.getMesActual();
            int anio = PeriodoFinancieroUtils.getAnioActual();
            double total = gastoDao.getTotalGastadoMes(mes, anio);
            
            android.util.Log.d("DEBUG_GASTO", "Mes: " + mes + " | Anio: " + anio + " | Total: " + total);
            
            callback.onSuccess(total);
        });
    }

    // ==================== IMPLEMENTACIÓN NUEVOS MÉTODOS DASHBOARD ====================

    @Override
    public List<MonthlyTotal> getMonthlyTotalsSync(int months) {
        // Calcular startDate: N meses atrás desde el primer día del mes actual
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.MONTH, -(months - 1)); // -1 porque incluimos el mes actual
        long startDate = cal.getTimeInMillis();

        return gastoDao.getMonthlyTotals(startDate, months);
    }

    @Override
    public LiveData<List<GastoEntity>> getRecentGastos(int limit) {
        return gastoDao.getRecentGastos(limit);
    }

    @Override
    public LiveData<Double> getTotalForDateRange(long startDate, long endDate) {
        return gastoDao.getTotalForDateRange(startDate, endDate);
    }

    @Override
    public double getTotalForDateRangeSync(long startDate, long endDate) {
        return gastoDao.getTotalForDateRangeSync(startDate, endDate);
    }
}

package com.miapp.agentegamer.data.repository;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.miapp.agentegamer.data.local.dao.GastoDao;
import com.miapp.agentegamer.data.local.dao.GastoDao.MonthlyTotal;
import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.domain.repository.GastoRepository;
import com.miapp.agentegamer.util.PeriodoFinancieroUtils;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

public class GastoRepositoryImpl implements GastoRepository {

    private final GastoDao gastoDao;
    private final ExecutorService executorService;
    private final String testUserId;

    @Inject
    public GastoRepositoryImpl(GastoDao gastoDao, ExecutorService executorService) {
        this(gastoDao, executorService, null);
    }

    // Constructor para tests unitarios (inyecta userId fijo)
    public GastoRepositoryImpl(GastoDao gastoDao, ExecutorService executorService, String testUserId) {
        this.gastoDao = gastoDao;
        this.executorService = executorService;
        this.testUserId = testUserId;
    }

    private String getCurrentUserId() {
        if (testUserId != null) {
            return testUserId;
        }
        return FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "";
    }

    @Override
    public LiveData<List<GastoEntity>> obtenerGastos() {
        return gastoDao.getAllGastos(getCurrentUserId());
    }

    @Override
    public void insertarGasto(GastoEntity gasto) {
        executorService.execute(() -> {
            gasto.setUserId(getCurrentUserId());
            gastoDao.insertGasto(gasto);
        });
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
        executorService.execute(() -> gastoDao.deleteAll(getCurrentUserId()));
    }

    @Override
    public LiveData<Double> getGastoMesActual() {
        int mes = PeriodoFinancieroUtils.getMesActual();
        int anio = PeriodoFinancieroUtils.getAnioActual();
        return gastoDao.getGastoTotalMes(getCurrentUserId(), mes, anio);
    }

    @Override
    public void getTotalGastadoMesSync(OnTotalGastadoCallback callback) {
        executorService.execute(() -> {
            int mes = PeriodoFinancieroUtils.getMesActual();
            int anio = PeriodoFinancieroUtils.getAnioActual();
            double total = gastoDao.getTotalGastadoMes(getCurrentUserId(), mes, anio);
            callback.onSuccess(total);
        });
    }

    @Override
    public List<MonthlyTotal> getMonthlyTotalsSync(int months) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.MONTH, -(months - 1));
        long startDate = cal.getTimeInMillis();

        return gastoDao.getMonthlyTotals(getCurrentUserId(), startDate, months);
    }

    @Override
    public LiveData<List<GastoEntity>> getRecentGastos(int limit) {
        return gastoDao.getRecentGastos(getCurrentUserId(), limit);
    }

    @Override
    public LiveData<Double> getTotalForDateRange(long startDate, long endDate) {
        return gastoDao.getTotalForDateRange(getCurrentUserId(), startDate, endDate);
    }

    @Override
    public double getTotalForDateRangeSync(long startDate, long endDate) {
        return gastoDao.getTotalForDateRangeSync(getCurrentUserId(), startDate, endDate);
    }
}

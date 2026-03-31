package com.miapp.agentegamer.data.repository;

import androidx.lifecycle.LiveData;

import com.miapp.agentegamer.data.local.dao.GastoDao;
import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.domain.repository.GastoRepository;
import com.miapp.agentegamer.util.PeriodoFinancieroUtils;

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
            
            // DEBUG: Log para verificar el total
            android.util.Log.d("DEBUG_GASTO", "Mes: " + mes + " | Anio: " + anio + " | Total: " + total);
            
            callback.onSuccess(total);
        });
    }
}

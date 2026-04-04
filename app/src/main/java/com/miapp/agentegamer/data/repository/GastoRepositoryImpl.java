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

/**
 * Implementación del repositorio de gastos que actúa como intermediario
 * entre los ViewModels y la capa de persistencia local (Room).
 * <p>
 * Esta clase implementa la interfaz {@link GastoRepository} y gestiona todas
 * las operaciones CRUD (Create, Read, Update, Delete) sobre la tabla de gastos.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Abstraer el acceso a datos de gastos desde la UI</li>
 *   <li>Gestionar la identificación de usuario para datos multiusuario</li>
 *   <li>Ejecutar operaciones de escritura en hilos secundarios</li>
 *   <li>Proporcionar métricas financieras (totales mensuales, por rango de fechas)</li>
 * </ul>
 * <p>
 * Todas las operaciones de escritura se ejecutan en un hilo secundario
 * mediante {@link ExecutorService} para no bloquear el hilo principal (UI).
 *
 * @see GastoEntity
 * @see GastoDao
 */
public class GastoRepositoryImpl implements GastoRepository {

    /** DAO para operaciones de base de datos de gastos */
    private final GastoDao gastoDao;
    
    /** Executor para ejecutar operaciones en hilos secundarios */
    private final ExecutorService executorService;
    private final String testUserId;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param gastoDao         DAO para operaciones de gastos
     * @param executorService  Executor para operaciones asíncronas
     */
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

    /**
     * Obtiene el ID del usuario actualmente autenticado.
     * <p>
     * Utiliza FirebaseAuth para determinar el usuario actual y retorna
     * su UID. Si no hay usuario autenticado, retorna string vacío.
     *
     * @return UID del usuario actual, o "" si no hay sesión activa
     */
    private String getCurrentUserId() {
        if (testUserId != null) {
            return testUserId;
        }
        return FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "";
    }

    /**
     * Obtiene todos los gastos del usuario actual.
     *
     * @return LiveData con la lista de todos los gastos del usuario
     */
    @Override
    public LiveData<List<GastoEntity>> obtenerGastos() {
        return gastoDao.getAllGastos(getCurrentUserId());
    }

    /**
     * Inserta un nuevo gasto en la base de datos.
     * <p>
     * La operación se ejecuta de forma asíncrona en un hilo secundario.
     * Antes de insertar, se asigna el userId del gasto al usuario actual.
     *
     * @param gasto Gasto a insertar
     */
    @Override
    public void insertarGasto(GastoEntity gasto) {
        executorService.execute(() -> {
            gasto.setUserId(getCurrentUserId());
            gastoDao.insertGasto(gasto);
        });
    }

    /**
     * Actualiza un gasto existente.
     * <p>
     * La operación se ejecuta de forma asíncrona en un hilo secundario.
     *
     * @param gasto Gasto con los datos actualizados
     */
    @Override
    public void actualizarGasto(GastoEntity gasto) {
        executorService.execute(() -> gastoDao.updateGasto(gasto));
    }

    /**
     * Borra un gasto de la base de datos.
     * <p>
     * La operación se ejecuta de forma asíncrona en un hilo secundario.
     *
     * @param gasto Gasto a borrar
     */
    @Override
    public void borrarGasto(GastoEntity gasto) {
        executorService.execute(() -> gastoDao.deleteGasto(gasto));
    }

    /**
     * Borra todos los gastos del usuario actual.
     * <p>
     * La operación se ejecuta de forma asíncrona en un hilo secundario.
     */
    @Override
    public void borrarTodosLosGastos() {
        executorService.execute(() -> gastoDao.deleteAll(getCurrentUserId()));
    }

    /**
     * Obtiene el total de gastos del mes actual.
     * <p>
     * Calcula la suma de todos los gastos del usuario en el mes y año actuales.
     * El cálculo se basa en el período financiero configurable.
     *
     * @return LiveData con el total de gastos del mes actual
     */
    @Override
    public LiveData<Double> getGastoMesActual() {
        int mes = PeriodoFinancieroUtils.getMesActual();
        int anio = PeriodoFinancieroUtils.getAnioActual();
        return gastoDao.getGastoTotalMes(getCurrentUserId(), mes, anio);
    }

    /**
     * Obtiene el total de gastos del mes actual de forma síncrona.
     * <p>
     * Utilizado cuando se necesita el valor inmediatamente en lugar de
     * observar cambios. El callback recibe el resultado en el hilo secundario.
     *
     * @param callback Interfaz para recibir el resultado del cálculo
     */
    @Override
    public void getTotalGastadoMesSync(OnTotalGastadoCallback callback) {
        executorService.execute(() -> {
            int mes = PeriodoFinancieroUtils.getMesActual();
            int anio = PeriodoFinancieroUtils.getAnioActual();
            double total = gastoDao.getTotalGastadoMes(getCurrentUserId(), mes, anio);
            callback.onSuccess(total);
        });
    }

    /**
     * Obtiene los totales mensuales de los últimos N meses.
     * <p>
     * Genera un historial de gastos por mes para gráficos o estadísticas.
     * El cálculo incluye el mes actual y los meses anteriores especificados.
     *
     * @param months Número de meses hacia atrás a incluir
     * @return Lista de MonthlyTotal con los totales de cada mes
     */
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

    /**
     * Obtiene los gastos más recientes del usuario.
     *
     * @param limit Número máximo de gastos a retornar
     * @return LiveData con la lista de gastos recientes
     */
    @Override
    public LiveData<List<GastoEntity>> getRecentGastos(int limit) {
        return gastoDao.getRecentGastos(getCurrentUserId(), limit);
    }

    /**
     * Obtiene el total de gastos en un rango de fechas.
     *
     * @param startDate Timestamp de inicio del rango (milisegundos)
     * @param endDate   Timestamp de fin del rango (milisegundos)
     * @return LiveData con el total de gastos en el rango
     */
    @Override
    public LiveData<Double> getTotalForDateRange(long startDate, long endDate) {
        return gastoDao.getTotalForDateRange(getCurrentUserId(), startDate, endDate);
    }

    /**
     * Obtiene el total de gastos en un rango de fechas de forma síncrona.
     *
     * @param startDate Timestamp de inicio del rango (milisegundos)
     * @param endDate   Timestamp de fin del rango (milisegundos)
     * @return         Total de gastos en el rango
     */
    @Override
    public double getTotalForDateRangeSync(long startDate, long endDate) {
        return gastoDao.getTotalForDateRangeSync(getCurrentUserId(), startDate, endDate);
    }
}

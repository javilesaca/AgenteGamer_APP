package com.miapp.agentegamer.data.repository;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.miapp.agentegamer.data.local.dao.GastoDao;
import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.domain.repository.GastoRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para GastoRepositoryImpl.
 * Verifica que el repository delegue correctamente al DAO y al ExecutorService.
 */
public class GastoRepositoryImplTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GastoDao mockDao;

    @Mock
    private ExecutorService mockExecutor;

    private GastoRepositoryImpl repository;
    private AutoCloseable closeable;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        repository = new GastoRepositoryImpl(mockDao, mockExecutor, "test-user-id");
    }

    @After
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void obtenerGastos_returnsDaoLiveData() {
        // Arrange
        MutableLiveData<List<GastoEntity>> expectedLiveData = new MutableLiveData<>();
        when(mockDao.getAllGastos(anyString())).thenReturn(expectedLiveData);

        // Act
        LiveData<List<GastoEntity>> result = repository.obtenerGastos();

        // Assert
        assertSame("El repository debe devolver exactamente el LiveData del DAO",
                expectedLiveData, result);
        verify(mockDao).getAllGastos(anyString());
    }

    @Test
    public void insertarGasto_executesDaoInsertInBackground() {
        // Arrange
        GastoEntity gasto = createTestGastoEntity("Test Game", 59.99);
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        // Act
        repository.insertarGasto(gasto);

        // Assert
        verify(mockExecutor).execute(runnableCaptor.capture());
        
        // Ejecutar el runnable capturado para verificar que llama al DAO
        runnableCaptor.getValue().run();
        verify(mockDao).insertGasto(gasto);
    }

    @Test
    public void actualizarGasto_executesDaoUpdateInBackground() {
        // Arrange
        GastoEntity gasto = createTestGastoEntity("Updated Game", 79.99);
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        // Act
        repository.actualizarGasto(gasto);

        // Assert
        verify(mockExecutor).execute(runnableCaptor.capture());
        runnableCaptor.getValue().run();
        verify(mockDao).updateGasto(gasto);
    }

    @Test
    public void borrarGasto_executesDaoDeleteInBackground() {
        // Arrange
        GastoEntity gasto = createTestGastoEntity("Delete Game", 29.99);
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        // Act
        repository.borrarGasto(gasto);

        // Assert
        verify(mockExecutor).execute(runnableCaptor.capture());
        runnableCaptor.getValue().run();
        verify(mockDao).deleteGasto(gasto);
    }

    @Test
    public void borrarTodosLosGastos_executesDaoDeleteAllInBackground() {
        // Arrange
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        // Act
        repository.borrarTodosLosGastos();

        // Assert
        verify(mockExecutor).execute(runnableCaptor.capture());
        runnableCaptor.getValue().run();
        verify(mockDao).deleteAll(anyString());
    }

    @Test
    public void getGastoMesActual_returnsDaoLiveDataForCurrentMonth() {
        // Arrange
        MutableLiveData<Double> expectedLiveData = new MutableLiveData<>(150.0);
        when(mockDao.getGastoTotalMes(anyString(), anyInt(), anyInt())).thenReturn(expectedLiveData);

        // Act
        LiveData<Double> result = repository.getGastoMesActual();

        // Assert
        assertSame("El repository debe devolver el LiveData del DAO para el mes actual",
                expectedLiveData, result);
        verify(mockDao).getGastoTotalMes(anyString(), anyInt(), anyInt());
        // No podemos verificar los valores exactos de mes/año porque dependen de PeriodoFinancieroUtils
    }

    @Test
    public void getTotalGastadoMesSync_executesCallbackWithTotal() {
        // Arrange
        double expectedTotal = 200.50;
        when(mockDao.getTotalGastadoMes(anyString(), anyInt(), anyInt())).thenReturn(expectedTotal);
        
        GastoRepository.OnTotalGastadoCallback mockCallback = mock(GastoRepository.OnTotalGastadoCallback.class);
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        // Act
        repository.getTotalGastadoMesSync(mockCallback);

        // Assert
        verify(mockExecutor).execute(runnableCaptor.capture());
        
        // Ejecutar el runnable capturado
        runnableCaptor.getValue().run();
        
        // Verificar que se llamó al callback con el total correcto
        verify(mockCallback).onSuccess(expectedTotal);
    }

    @Test
    public void getTotalGastadoMesSync_callsDaoWithCorrectParameters() {
        // Arrange
        when(mockDao.getTotalGastadoMes(anyString(), anyInt(), anyInt())).thenReturn(0.0);
        GastoRepository.OnTotalGastadoCallback mockCallback = mock(GastoRepository.OnTotalGastadoCallback.class);
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        // Act
        repository.getTotalGastadoMesSync(mockCallback);

        // Assert
        verify(mockExecutor).execute(runnableCaptor.capture());
        
        // Verificar que se llama al DAO (no podemos verificar los parámetros exactos
        // porque dependen de PeriodoFinancieroUtils)
        runnableCaptor.getValue().run();
        verify(mockDao).getTotalGastadoMes(anyString(), anyInt(), anyInt());
    }

    /**
     * Método helper para crear GastoEntity de prueba.
     * Nota: El constructor de GastoEntity calcula mes y anio automáticamente desde la fecha.
     */
    private GastoEntity createTestGastoEntity(String nombre, double precio) {
        // Usar timestamp fijo para tests consistentes
        long testTimestamp = 1672531200000L; // 1 de enero de 2023
        return new GastoEntity(null, nombre, precio, testTimestamp, null);
    }
}
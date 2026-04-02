package com.miapp.agentegamer.ui.viewmodel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.domain.model.SistemaFinanciero;
import com.miapp.agentegamer.domain.repository.GastoRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para GastoViewModel.
 * Verifica la lógica de negocio y el manejo de LiveData.
 */
public class GastoViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GastoRepository mockRepository;

    @Mock
    private Application mockApplication;

    private MutableLiveData<List<GastoEntity>> mockListaGastos;
    private MutableLiveData<List<GastoEntity>> mockRecentGastos;

    private GastoViewModel viewModel;
    private AutoCloseable closeable;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        mockListaGastos = new MutableLiveData<>();
        mockRecentGastos = new MutableLiveData<>();

        when(mockRepository.obtenerGastos()).thenReturn(mockListaGastos);
        when(mockRepository.getRecentGastos(5)).thenReturn(mockRecentGastos);

        viewModel = new GastoViewModel(mockApplication, mockRepository);
    }

    @After
    public void tearDown() throws Exception {
        viewModel.onCleared();
        closeable.close();
    }

    // ================================
    // OBSERVABLES
    // ================================

    @Test
    public void getListaGastos_returnsNonNullLiveData() {
        assertNotNull("getListaGastos() no debe retornar null", viewModel.getListaGastos());
    }

    @Test
    public void getEstadoUI_returnsNonNullLiveData() {
        assertNotNull("getEstadoUI() no debe retornar null", viewModel.getEstadoUI());
    }

    // ================================
    // setSistemaFinanciero
    // ================================

    @Test
    public void setSistemaFinanciero_withGastos_updatesEstadoUI() {
        // Arrange
        SistemaFinanciero sistema = new SistemaFinanciero(200.0);
        List<GastoEntity> gastos = new ArrayList<>();
        GastoEntity gasto = new GastoEntity("Zelda", 49.99, System.currentTimeMillis(), null);
        gasto.setId(1);
        gastos.add(gasto);

        mockListaGastos.setValue(gastos);

        // Act
        viewModel.setSistemaFinanciero(sistema);

        // Assert
        assertNotNull("estadoUI debe tener valor después de setSistemaFinanciero",
                viewModel.getEstadoUI().getValue());
    }

    @Test
    public void setSistemaFinanciero_withEmptyGastos_setsEstadoUI() {
        // Arrange
        SistemaFinanciero sistema = new SistemaFinanciero(200.0);
        mockListaGastos.setValue(new ArrayList<>());

        // Act
        viewModel.setSistemaFinanciero(sistema);

        // Assert
        assertNotNull("estadoUI debe tener valor con lista vacía",
                viewModel.getEstadoUI().getValue());
    }

    // ================================
    // CRUD
    // ================================

    @Test
    public void insertar_callsRepositoryInsertarGasto() {
        // Arrange
        GastoEntity gasto = new GastoEntity("Mario", 59.99, System.currentTimeMillis(), null);

        // Act
        viewModel.insertar(gasto);

        // Assert
        verify(mockRepository).insertarGasto(gasto);
    }

    @Test
    public void actualizar_callsRepositoryActualizarGasto() {
        // Arrange
        GastoEntity gasto = new GastoEntity("Halo", 69.99, System.currentTimeMillis(), null);

        // Act
        viewModel.actualizar(gasto);

        // Assert
        verify(mockRepository).actualizarGasto(gasto);
    }

    @Test
    public void borrar_callsRepositoryBorrarGasto() {
        // Arrange
        GastoEntity gasto = new GastoEntity("FIFA", 49.99, System.currentTimeMillis(), null);

        // Act
        viewModel.borrar(gasto);

        // Assert
        verify(mockRepository).borrarGasto(gasto);
    }

    @Test
    public void borrarTodosLosGastos_callsRepositoryMethod() {
        // Act
        viewModel.borrarTodosLosGastos();

        // Assert
        verify(mockRepository).borrarTodosLosGastos();
    }

    // ================================
    // EDGE CASES
    // ================================

    @Test
    public void insertar_withNull_callsRepository() {
        // Act
        viewModel.insertar(null);

        // Assert
        verify(mockRepository).insertarGasto(null);
    }

    @Test
    public void borrar_withNull_callsRepository() {
        // Act
        viewModel.borrar(null);

        // Assert
        verify(mockRepository).borrarGasto(null);
    }

    // ================================
    // ONCLEARED
    // ================================

    @Test
    public void onCleared_doesNotThrowExceptions() {
        // Act
        viewModel.onCleared();

        // Assert
        assertTrue("onCleared() debe ejecutarse sin errores", true);
    }
}

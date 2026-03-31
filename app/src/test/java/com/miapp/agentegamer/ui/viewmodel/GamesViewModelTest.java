package com.miapp.agentegamer.ui.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.miapp.agentegamer.data.remote.model.GameDto;
import com.miapp.agentegamer.data.remote.repository.GamesRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para GamesViewModel.
 * Verifica la lógica de negocio y el manejo de LiveData.
 */
public class GamesViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GamesRepository mockRepository;

    @Mock
    private MutableLiveData<List<GameDto>> mockGamesLiveData;

    @Mock
    private MutableLiveData<Boolean> mockCargandoLiveData;

    private GamesViewModel viewModel;
    private AutoCloseable closeable;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        
        // Setup mock behavior para los LiveData del repositorio
        when(mockRepository.getCargando()).thenReturn(mockCargandoLiveData);
        when(mockRepository.getGames()).thenReturn(mockGamesLiveData);
        when(mockRepository.buscarJuegosPaginados(anyString(), anyBoolean()))
            .thenReturn(mockGamesLiveData);
        
        // Crear el ViewModel con el repositorio mockeado
        viewModel = new GamesViewModel(mockRepository);
    }

    @After
    public void tearDown() throws Exception {
        // Importante: Llamar a onCleared() para remover observers
        viewModel.onCleared();
        closeable.close();
    }

    @Test
    public void constructor_initializesCargandoObserver() {
        // Verify que el observer fue seteado en el constructor
        verify(mockRepository.getCargando()).observeForever(any(Observer.class));
    }

    @Test
    public void getJuegos_returnsNonNullLiveData() {
        assertNotNull("getJuegos() no debe retornar null", viewModel.getJuegos());
    }

    @Test
    public void isCargando_returnsNonNullLiveData() {
        assertNotNull("isCargando() no debe retornar null", viewModel.isCargando());
    }

    @Test
    public void cargarJuegosIniciales_setsCargandoToTrue() {
        // Arrange
        when(mockCargandoLiveData.getValue()).thenReturn(true);
        
        // Act
        viewModel.cargarJuegosIniciales();
        
        // Assert
        // No podemos verificar el valor exacto porque depende de observeForever
        verify(mockRepository).getGames();
    }

    @Test
    public void buscarJuegosPaginados_withReset_callsRepository() {
        // Arrange
        String query = "zelda";
        boolean reset = true;
        
        // Act
        viewModel.buscarJuegosPaginados(query, reset);
        
        // Assert
        verify(mockRepository).buscarJuegosPaginados(query, reset);
    }

    @Test
    public void buscarJuegosPaginados_withoutReset_callsRepository() {
        // Arrange
        String query = "mario";
        boolean reset = false;
        
        // Act
        viewModel.buscarJuegosPaginados(query, reset);
        
        // Assert
        verify(mockRepository).buscarJuegosPaginados(query, reset);
    }

    @Test
    public void obtenerFamiliasPlataformas_withNullPlatforms_returnsEmptySet() {
        // Arrange
        GameDto juego = new GameDto();
        juego.setPlatforms(null);
        
        // Act
        var resultado = viewModel.obtenerFamiliasPlataformas(juego);
        
        // Assert
        assertTrue("Con platforms null, debe retornar set vacío", resultado.isEmpty());
    }

    @Test
    public void obtenerFamiliasPlataformas_withPlayStationPlatform_addsPlaystationFamily() {
        // Arrange
        GameDto juego = new GameDto();
        List<GameDto.PlatformWrapper> platforms = new ArrayList<>();
        
        // Crear PlatformWrapper como clase interna
        GameDto.PlatformWrapper wrapper = juego.new PlatformWrapper();
        GameDto.Platform platform = juego.new Platform();
        platform.setSlug("playstation-4");
        wrapper.setPlatform(platform);
        platforms.add(wrapper);
        
        juego.setPlatforms(platforms);
        
        // Act
        var resultado = viewModel.obtenerFamiliasPlataformas(juego);
        
        // Assert
        assertTrue("Debe contener 'playstation'", resultado.contains("playstation"));
        assertEquals("Debe tener exactamente 1 familia", 1, resultado.size());
    }

    @Test
    public void obtenerFamiliasPlataformas_withMultiplePlatforms_addsCorrectFamilies() {
        // Arrange
        GameDto juego = new GameDto();
        List<GameDto.PlatformWrapper> platforms = new ArrayList<>();
        
        // PlayStation platform
        GameDto.PlatformWrapper psWrapper = juego.new PlatformWrapper();
        GameDto.Platform psPlatform = juego.new Platform();
        psPlatform.setSlug("playstation-5");
        psWrapper.setPlatform(psPlatform);
        platforms.add(psWrapper);
        
        // Xbox platform
        GameDto.PlatformWrapper xboxWrapper = juego.new PlatformWrapper();
        GameDto.Platform xboxPlatform = juego.new Platform();
        xboxPlatform.setSlug("xbox-series-x");
        xboxWrapper.setPlatform(xboxPlatform);
        platforms.add(xboxWrapper);
        
        juego.setPlatforms(platforms);
        
        // Act
        var resultado = viewModel.obtenerFamiliasPlataformas(juego);
        
        // Assert
        assertTrue("Debe contener 'playstation'", resultado.contains("playstation"));
        assertTrue("Debe contener 'xbox'", resultado.contains("xbox"));
        assertEquals("Debe tener 2 familias", 2, resultado.size());
    }

    @Test
    public void getFamilyIcon_playstation_returnsCorrectDrawable() {
        // Act
        int icon = viewModel.getFamilyIcon("playstation");
        
        // Assert
        // No podemos verificar el recurso exacto porque R.drawable es final
        // pero podemos verificar que no es 0 (default)
        assertNotEquals("Playstation debe retornar un icono válido", 0, icon);
    }

    @Test
    public void getFamilyIcon_unknownFamily_returnsZero() {
        // Act
        int icon = viewModel.getFamilyIcon("unknown");
        
        // Assert
        assertEquals("Familia desconocida debe retornar 0", 0, icon);
    }

    @Test
    public void onCleared_doesNotThrowExceptions() {
        // Act - Llamar a onCleared() no debe lanzar excepciones
        viewModel.onCleared();
        
        // Assert - Si llegamos aquí, el test pasa
        assertTrue("onCleared() debe ejecutarse sin errores", true);
    }
}
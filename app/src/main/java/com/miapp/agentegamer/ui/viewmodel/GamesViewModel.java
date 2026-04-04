package com.miapp.agentegamer.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.domain.model.GameInfo;
import com.miapp.agentegamer.domain.model.SistemaFinanciero;
import com.miapp.agentegamer.data.remote.model.GameDto;
import com.miapp.agentegamer.data.remote.repository.GamesRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * GamesViewModel
 * --------------
 * ViewModel que gestiona los datos del catálogo de juegos obtenidos desde la API.
 * Utiliza el patrón MVVM para separar la lógica de negocio de la UI.
 * 
 * Funcionalidades:
 * - Carga juegos iniciales o recientes desde la API de RAWG
 * - Procesa cada juego para calcular precio estimado basado en rating
 * - Busca juegos con paginación infinita
 * - Obtiene familias de plataformas (PlayStation, Xbox, Nintendo, PC)
 * - Provee estado de carga para la UI
 * 
 * @see GamesRepository
 * @see GameDto
 * @see SistemaFinanciero
 */
@HiltViewModel
public class GamesViewModel extends ViewModel {

    // Repositorio para acceder a la API de juegos
    private final GamesRepository repository;
    // LiveData de la lista de juegos
    private final MutableLiveData<List<GameDto>> juegos = new MutableLiveData<>(new ArrayList<>());
    // LiveData del estado de carga
    private final MutableLiveData<Boolean> cargando = new MutableLiveData<>();
    // Presupuesto del usuario para calcular precios estimados
    private double presupuestoUsuario = 100;

    // LiveData y observer para juegos iniciales
    private LiveData<List<GameDto>> juegosInicialesLiveData;
    private Observer<List<GameDto>> juegosInicialesObserver;

    // LiveData y observer para juegos recientes
    private LiveData<List<GameDto>> juegosRecientesLiveData;
    private Observer<List<GameDto>> juegosRecientesObserver;

    // LiveData y observer para búsqueda paginada
    private LiveData<List<GameDto>> buscarPaginadosLiveData;
    private Observer<List<GameDto>> buscarPaginadosObserver;

    // Observer para el estado de carga del repositorio
    private Observer<Boolean> repoCargandoObserver;

    @Inject
    public GamesViewModel(GamesRepository repository) {
        this.repository = repository;

        repoCargandoObserver = cargando::setValue;
        repository.getCargando().observeForever(repoCargandoObserver);
    }

    /**
     * Retorna el LiveData de la lista de juegos.
     * La UI observa este LiveData para actualizar la lista de juegos.
     * 
     * @return LiveData con la lista de juegos actual
     */
    public LiveData<List<GameDto>> getJuegos() {
        return juegos;
    }

    /**
     * Carga los juegos iniciales desde la API de RAWG.
     * Establece el estado de carga, observa el repositorio y procesa la lista.
     */
    public void cargarJuegosIniciales() {

        cargando.setValue(true);

        juegosInicialesLiveData = repository.getGames();

        juegosInicialesObserver = lista -> {
            juegos.setValue(procesarLista(lista));
            cargando.setValue(false);
        };

        juegosInicialesLiveData.observeForever(juegosInicialesObserver);
    }

    /**
     * Carga los juegos recientemente lanzados desde la API.
     * Similar a cargarJuegosIniciales pero para juegos recientes.
     */
    public void cargarJuegosRecientes() {

        cargando.setValue(true);

        juegosRecientesLiveData = repository.getRecentlyReleasedGames();

        juegosRecientesObserver = lista -> {
            juegos.setValue(procesarLista(lista));
            cargando.setValue(false);
        };

        juegosRecientesLiveData.observeForever(juegosRecientesObserver);
    }

    /**
     * Procesa una lista de juegos para agregar información adicional:
     * - Calcula el precio estimado basado en el rating y presupuesto del usuario
     * - Genera un texto con las plataformas del juego
     * 
     * @param lista Lista de juegos a procesar
     * @return Lista de juegos procesada con información adicional
     */
    private List<GameDto> procesarLista(List<GameDto> lista) {

        if (lista == null) return Collections.emptyList();

        SistemaFinanciero sistema = new SistemaFinanciero(presupuestoUsuario);

        for (GameDto juego: lista) {

            GameInfo gameInfo = new GameInfo(juego.getName(), juego.getRating());
            double precio = sistema.estimarPrecio(gameInfo);
            juego.setPrecioEstimado(precio);

            if (juego.getPlatforms() != null) {

                StringBuilder plataformas = new StringBuilder();

                for (GameDto.PlatformWrapper wrapper : juego.getPlatforms()) {
                    if (wrapper.getPlatform() != null) {
                        plataformas.append(wrapper.getPlatform().getName()).append(" . ");
                    }
                }

                juego.setPlataformasTexto(plataformas.toString());
            }
        }

        return lista;
    }

    /**
     * Busca juegos de forma paginada según una consulta.
     * Si reset es true, reemplaza la lista actual; si es false, añade más resultados.
     * 
     * @param query Texto de búsqueda
     * @param reset true para reemplazar la lista, false para añadir
     */
    public void buscarJuegosPaginados(String query, boolean reset) {

        cargando.setValue(true);

        buscarPaginadosLiveData = repository.buscarJuegosPaginados(query, reset);

        buscarPaginadosObserver = lista -> {

            if (lista == null) {
                cargando.setValue(false);
                return;
            }

            List<GameDto> procesada = procesarLista(lista);

            if (reset) {
                juegos.setValue(procesada);
            } else {
                List<GameDto> actual = juegos.getValue();
                if (actual == null) actual = new ArrayList<>();

                actual.addAll(procesada);
                juegos.setValue(actual);
            }

            cargando.setValue(false);
        };

        buscarPaginadosLiveData.observeForever(buscarPaginadosObserver);
    }

    /**
     * Obtiene las familias únicas de plataformas de un juego.
     * Consolida plataformas similares (ej: PS4, PS5 -> playstation).
     * 
     * @param juego Juego del que obtener las familias de plataformas
     * @return Conjunto de familias de plataformas únicas
     */
    public Set<String> obtenerFamiliasPlataformas(GameDto juego) {

        Set<String> familias = new HashSet<>();

        if (juego.getPlatforms() == null) return familias;

        for (GameDto.PlatformWrapper wrapper : juego.getPlatforms()) {

            if (wrapper.getPlatform() == null) continue;

            String slug = wrapper.getPlatform().getSlug();

            if (slug == null) continue;
            //Anidamos las plataformas para evitar iconos iguales ..Ej: PS4, PS5..etc
            if (slug.contains("playstation")) {
                familias.add("playstation");
            } else if (slug.contains("xbox")) {
                familias.add("xbox");
            } else if (slug.contains("nintendo")) {
                familias.add("nintendo");
            } else if (slug.contains("pc")) {
                familias.add("pc");
            }
        }

        return familias;
    }

    public int getFamilyIcon(String family) {

        switch (family) {
            case "playstation":
                return R.drawable.playstation_logo_colour;

            case "xbox":
                return R.drawable.xbox;

            case "nintendo":
                return R.drawable.nintendo_blue_logo;

            case "pc" :
                return R.drawable.pcmag_idepwrpbfb_0;

            default:
                return 0;
        }
    }

    public LiveData<Boolean> isCargando() {
        return cargando;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (juegosInicialesLiveData != null && juegosInicialesObserver != null) {
            juegosInicialesLiveData.removeObserver(juegosInicialesObserver);
        }
        if (juegosRecientesLiveData != null && juegosRecientesObserver != null) {
            juegosRecientesLiveData.removeObserver(juegosRecientesObserver);
        }
        if (buscarPaginadosLiveData != null && buscarPaginadosObserver != null) {
            buscarPaginadosLiveData.removeObserver(buscarPaginadosObserver);
        }
        if (repoCargandoObserver != null) {
            repository.getCargando().removeObserver(repoCargandoObserver);
        }
    }
}

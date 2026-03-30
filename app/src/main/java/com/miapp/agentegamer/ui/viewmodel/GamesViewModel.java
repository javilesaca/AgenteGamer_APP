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

@HiltViewModel
public class GamesViewModel extends ViewModel {

    private final GamesRepository repository;
    private final MutableLiveData<List<GameDto>> juegos = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> cargando = new MutableLiveData<>();
    private double presupuestoUsuario = 100;

    private LiveData<List<GameDto>> juegosInicialesLiveData;
    private Observer<List<GameDto>> juegosInicialesObserver;

    private LiveData<List<GameDto>> buscarPaginadosLiveData;
    private Observer<List<GameDto>> buscarPaginadosObserver;

    private Observer<Boolean> repoCargandoObserver;

    @Inject
    public GamesViewModel(GamesRepository repository) {
        this.repository = repository;

        repoCargandoObserver = cargando::setValue;
        repository.getCargando().observeForever(repoCargandoObserver);
    }

    public LiveData<List<GameDto>> getJuegos() {
        return juegos;
    }

    public void cargarJuegosIniciales() {

        cargando.setValue(true);

        juegosInicialesLiveData = repository.getGames();

        juegosInicialesObserver = lista -> {
            juegos.setValue(procesarLista(lista));
            cargando.setValue(false);
        };

        juegosInicialesLiveData.observeForever(juegosInicialesObserver);
    }

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
        if (buscarPaginadosLiveData != null && buscarPaginadosObserver != null) {
            buscarPaginadosLiveData.removeObserver(buscarPaginadosObserver);
        }
        if (repoCargandoObserver != null) {
            repository.getCargando().removeObserver(repoCargandoObserver);
        }
    }
}

package com.miapp.agentegamer.ui.games;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.miapp.agentegamer.AgenteGamerApplication;
import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.local.entity.WishlistEntity;
import com.miapp.agentegamer.data.remote.model.GameDto;
import com.miapp.agentegamer.ui.common.BaseNavActivity;
import com.miapp.agentegamer.ui.viewmodel.GamesViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import com.miapp.agentegamer.ui.viewmodel.WishlistViewModel;

import java.util.List;

/**
 * ListaJuegosActivity
 * -------------------
 * Pantalla principal de exploración del catálogo de juegos.
 * Muestra juegos en un grid con búsqueda en tiempo real y paginación infinita.
 * 
 * Características:
 * - Grid de 2 columnas con portadas de juegos
 * - Barra de búsqueda con debounce (500ms) para evitar llamadas excesivas
 * - Paginación infinita al hacer scroll
 * - Precarga de juegos desde SplashActivity para mejor rendimiento
 * - Al hacer clic en un juego, se agrega a la wishlist con precio estimado
 * - Navegación inferior para acceder a otras secciones
 * 
 * @see JuegosAdapter
 * @see GamesViewModel
 * @see WishlistViewModel
 */
@AndroidEntryPoint
public class ListaJuegosActivity extends BaseNavActivity {

    // ViewModel para gestionar datos de juegos desde la API
    private GamesViewModel viewModel;
    // RecyclerView para mostrar el grid de juegos
    private RecyclerView recyclerView;
    // Adapter para el RecyclerView
    private JuegosAdapter adapter;
    // ViewModel para gestionar la wishlist
    private WishlistViewModel wishlistViewModel;
    // Consulta actual de búsqueda
    private String queryActual="";
    // Moneda actual del usuario
    private String moneda = "EUR";
    // Flag para evitar múltiples cargas simultáneas
    private boolean cargando = false;
    // Layout mostrado cuando no hay resultados
    private View emptyLayout;
    // ProgressBar mostrado durante la carga
    private ProgressBar progressBar;
    // Handler para ejecutar código en el hilo principal
    private final Handler handler = new Handler(Looper.getMainLooper());
    // Runnable para el debounce de búsqueda
    private Runnable searchRunnable;
    // Delay de debounce en milisegundos
    private static final long DEBOUNCE_DELAY = 500; //ms

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa las vistas, configura el RecyclerView, establece la navegación inferior,
     * carga datos pre-cargados o desde la API, configura la búsqueda con debounce
     * y la paginación infinita.
     * 
     * @param savedInstanceState Estado guardado de la actividad (puede ser null)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_juegos);

        emptyLayout = findViewById(R.id.layoutEmpty);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerJuegos);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new JuegosAdapter();
        adapter.setMoneda(moneda);
        recyclerView.setAdapter(adapter);

        setupBottomNavigation(R.id.nav_juegos);

        viewModel = new ViewModelProvider(this).get(GamesViewModel.class);
        wishlistViewModel = new ViewModelProvider(this).get(WishlistViewModel.class);

        // Observe currency changes
        wishlistViewModel.getMonedaLiveData().observe(this, currency -> {
            moneda = currency != null ? currency : "EUR";
            adapter.setMoneda(moneda);
        });
        TextInputEditText searchView = findViewById(R.id.searchView);

        // Try to use preloaded games from splash screen
        AgenteGamerApplication app = (AgenteGamerApplication) getApplication();
        List<GameDto> preloadedGames = app.getPreloadedGames();

        if (preloadedGames != null && !preloadedGames.isEmpty()) {
            // Use preloaded data - no need to make API call
            adapter.setLista(preloadedGames);
            recyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else {
            // Load games normally via ViewModel
            viewModel.cargarJuegosRecientes();
        }

        adapter.setOnJuegoClickListener((juego, precioEstimado) -> {
            WishlistEntity entity = new WishlistEntity(
                    null,
                    juego.getId(),
                    juego.getName(),
                    juego.getReleaseDate(),
                    juego.getImageUrl(),
                    juego.getPlataformasTexto(),
                    precioEstimado
            );
            wishlistViewModel.insertar(entity);
            Toast.makeText(this, getString(R.string.added_to_wishlist, precioEstimado), Toast.LENGTH_SHORT).show();
        });

        viewModel.getJuegos().observe(this, juegos -> {

            progressBar.setVisibility(View.GONE);

            if (juegos == null || juegos.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            } else {
                emptyLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.setLista(juegos);
            }

            cargando = false;
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> {
                    String newText = s.toString().trim();
                    if (newText.length() < 3) {
                        return;
                    }

                    queryActual = newText;
                    cargando = true;

                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);

                    viewModel.buscarJuegosPaginados(newText, true);
                };

                handler.postDelayed(searchRunnable, DEBOUNCE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);

                LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();

                if (!cargando &&
                        lm != null &&
                        lm.findLastVisibleItemPosition() >= adapter.getItemCount() - 3) {

                    cargando = true;

                    viewModel.buscarJuegosPaginados(
                            queryActual,
                            false
                    );
                }
            }
        });

    }

    /**
     * Libera recursos cuando la actividad se destruye.
     * Cancela cualquier búsqueda pendientemente para evitar memory leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (searchRunnable != null) {
            handler.removeCallbacks(searchRunnable);
        }

    }
}


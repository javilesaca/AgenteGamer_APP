package com.miapp.agentegamer.ui.games;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.local.entity.WishlistEntity;
import com.miapp.agentegamer.ui.viewmodel.GamesViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import com.miapp.agentegamer.ui.viewmodel.WishlistViewModel;

@AndroidEntryPoint
public class ListaJuegosActivity extends AppCompatActivity {

    private GamesViewModel viewModel;
    private RecyclerView recyclerView;
    private JuegosAdapter adapter;
    private WishlistViewModel wishlistViewModel;
    private String queryActual="";
    private String moneda = "EUR";
    private boolean cargando = false;
    private View emptyLayout;
    private ProgressBar progressBar;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private static final long DEBOUNCE_DELAY = 500; //ms

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

        viewModel = new ViewModelProvider(this).get(GamesViewModel.class);
        wishlistViewModel = new ViewModelProvider(this).get(WishlistViewModel.class);

        // Observe currency changes
        wishlistViewModel.getMonedaLiveData().observe(this, currency -> {
            moneda = currency != null ? currency : "EUR";
            adapter.setMoneda(moneda);
        });
        SearchView searchView = findViewById(R.id.searchView);

        // Cargar juegos recién lanzados al abrir la pantalla
        viewModel.cargarJuegosRecientes();

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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> {

                    if (newText.length() < 3) {
                        return;
                    }

                    queryActual = newText;
                    cargando =true;

                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);

                    viewModel.buscarJuegosPaginados(newText, true);
                };

                handler.postDelayed(searchRunnable, DEBOUNCE_DELAY);

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (searchRunnable != null) {
            handler.removeCallbacks(searchRunnable);
        }

    }
}


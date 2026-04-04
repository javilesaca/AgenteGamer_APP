package com.miapp.agentegamer.ui.wishlist;

import android.os.Bundle;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.ui.common.BaseNavActivity;
import com.miapp.agentegamer.util.MoneyUtils;
import com.miapp.agentegamer.ui.wishlist.dialogs.DialogDetalleJuegoFragment;
import com.miapp.agentegamer.ui.wishlist.dialogs.DialogEditarPrecioFragment;
import dagger.hilt.android.AndroidEntryPoint;
import com.miapp.agentegamer.ui.viewmodel.WishlistViewModel;

/**
 * ListaWishlistActivity
 * ---------------------
 * Pantalla que muestra la lista de deseos del usuario.
 * Lista todos los juegos que el usuario ha marcado para comprar,
 * con su precio estimado, evaluación financiera y acciones disponibles.
 * 
 * Características:
 * - Muestra lista de juegos en wishlist con imagen, nombre, precio y evaluación
 * - Calcula y muestra el costo total estimado de la wishlist
 * - Al hacer clic en un juego, muestra el diálogo de detalle
 * - Permite eliminar juegos de la wishlist
 * - Navegación inferior para acceder a otras secciones
 * 
 * @see WishlistAdapter
 * @see DialogDetalleJuegoFragment
 * @see WishlistViewModel
 */
@AndroidEntryPoint
public class ListaWishlistActivity extends BaseNavActivity {

    // ViewModel para gestionar datos de la wishlist
    private WishlistViewModel viewModel;
    // Adapter para el RecyclerView
    private WishlistAdapter adapter;
    // TextView para mostrar el costo total de la wishlist
    private TextView tvTotalCosto;
    // Moneda actual del usuario
    private String moneda = "EUR";


    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa el ViewModel, configura el RecyclerView con su adapter,
     * establece la navegación inferior y los listeners para los diálogos
     * y acciones de eliminación.
     * 
     * @param savedInstanceState Estado guardado de la actividad (puede ser null)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_wishlist);

        viewModel = new ViewModelProvider(this).get(WishlistViewModel.class);

        viewModel.getMonedaLiveData().observe(this, currency -> {
            moneda = currency != null ? currency : "EUR";
        });

        tvTotalCosto = findViewById(R.id.tvTotalCosto);

        RecyclerView recyclerView = findViewById(R.id.recyclerWishlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new WishlistAdapter();
        recyclerView.setAdapter(adapter);

        setupBottomNavigation(R.id.nav_wishlist);

        adapter.setOnItemClickListener(juego -> {
            DialogDetalleJuegoFragment.newInstance(juego, moneda).show(getSupportFragmentManager(), "detalleJuego");
        });

        adapter.setOnEliminarClickListener(juego -> {
            viewModel.borrar(juego);
        });

        // ViewModel from ViewModelProvider
        viewModel.getWishList().observe(this, lista -> {
            adapter.setLista(lista, moneda);

            // Calculate and display total estimated cost
            double total = 0;
            for (WishlistItemUI item : lista) {
                total += item.getJuego().getPrecioEstimado();
            }
            tvTotalCosto.setText(MoneyUtils.format(total, moneda));
        });
    }

}

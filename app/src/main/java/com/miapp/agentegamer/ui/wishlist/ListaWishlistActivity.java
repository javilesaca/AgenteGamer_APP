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

@AndroidEntryPoint
public class ListaWishlistActivity extends BaseNavActivity {

    private WishlistViewModel viewModel;
    private WishlistAdapter adapter;
    private TextView tvTotalCosto;
    private String moneda = "EUR";


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

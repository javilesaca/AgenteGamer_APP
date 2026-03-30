package com.miapp.agentegamer.ui.wishlist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.ui.wishlist.dialogs.DialogDetalleJuegoFragment;
import com.miapp.agentegamer.ui.wishlist.dialogs.DialogEditarPrecioFragment;
import dagger.hilt.android.AndroidEntryPoint;
import com.miapp.agentegamer.ui.viewmodel.WishlistViewModel;

@AndroidEntryPoint
public class ListaWishlistActivity extends AppCompatActivity {

    private WishlistViewModel viewModel;
    private WishlistAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_wishlist);

        viewModel = new ViewModelProvider(this).get(WishlistViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerWishlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new WishlistAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(juego -> {
            DialogDetalleJuegoFragment.newInstance(juego).show(getSupportFragmentManager(), "detalleJuego");
        });

        adapter.setOnEliminarClickListener(juego -> {
            viewModel.borrar(juego);
        });

        // ViewModel from ViewModelProvider
        viewModel.getWishList().observe(this, adapter::setLista);
    }

}

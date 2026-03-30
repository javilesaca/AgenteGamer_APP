package com.miapp.agentegamer.ui.wishlist.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.local.entity.WishlistEntity;
import com.miapp.agentegamer.ui.viewmodel.WishlistViewModel;

public class DialogConfirmarCompraFragment extends DialogFragment {

    private static final String ARG_JUEGO = "arg_juego";
    private static final String ARG_PRECIO = "arg_precio";

    private WishlistEntity juego;
    private double precioFinal;
    private WishlistViewModel viewModel;

    public static DialogConfirmarCompraFragment newInstance(WishlistEntity juego, double precioFinal) {
        DialogConfirmarCompraFragment fragment= new DialogConfirmarCompraFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_JUEGO, juego);
        args.putDouble(ARG_PRECIO, precioFinal);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            juego = (WishlistEntity) getArguments().getSerializable(ARG_JUEGO);
            precioFinal = getArguments().getDouble(ARG_PRECIO);
        }

        viewModel = new ViewModelProvider(requireActivity()).get(WishlistViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

          AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Confirmar compra")
                .setMessage( "¿Deseas comprar:\n\n" + juego.getNombre() + "\n\nPrecio: " + precioFinal + " €")
                .setPositiveButton("Comprar", (d,w) -> {
                    viewModel.comprarJuego(juego, precioFinal);
                })
                .setNegativeButton("Cancelar", null)
                .create();
          dialog.setOnShowListener(d -> {
              dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                      .setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));

              dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                      .setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
          });

          return dialog;

    }
}

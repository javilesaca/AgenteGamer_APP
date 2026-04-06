package com.miapp.agentegamer.ui.wishlist.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.miapp.agentegamer.R;
import com.miapp.agentegamer.util.MoneyUtils;
import com.miapp.agentegamer.data.local.entity.WishlistEntity;
import com.miapp.agentegamer.ui.viewmodel.WishlistViewModel;

/**
 * DialogDetalleJuegoFragment
 * --------------------------
 * DialogFragment que muestra los detalles de un juego de la wishlist.
 * Permite al usuario editar la plataforma, precio, comprar el juego o cancel.
 * 
 * Características:
 * - Muestra imagen, nombre y precio estimado del juego
 * - Spinner para seleccionar la plataforma
 * - Botón para editar el precio
 * - Botón para confirmar la compra
 * - Botón para cerrar el diálogo
 * - Actualiza automáticamente la wishlist al cambiar la plataforma
 * 
 * @see DialogFragment
 * @see WishlistEntity
 * @see WishlistViewModel
 * @see DialogEditarPrecioFragment
 * @see DialogConfirmarCompraFragment
 */
public class DialogDetalleJuegoFragment extends DialogFragment {

    private static final String ARG_JUEGO = "juego";

    private WishlistEntity juego;
    private WishlistViewModel wishlistViewModel;
    private boolean isInitialLoad = true;

    public static DialogDetalleJuegoFragment newInstance(WishlistEntity juego, String moneda) {
        DialogDetalleJuegoFragment fragment = new DialogDetalleJuegoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_JUEGO, juego);
        args.putString("moneda", moneda);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        juego = (WishlistEntity) getArguments().getSerializable(ARG_JUEGO);
        wishlistViewModel = new ViewModelProvider(requireActivity()).get(WishlistViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String moneda = getArguments() != null ? getArguments().getString("moneda", "EUR") : "EUR";

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_detalle_juego, null);

        ImageView img = view.findViewById(R.id.imgJuego);
        TextView nombre = view.findViewById(R.id.tvNombreJuego);
        TextView precio = view.findViewById(R.id.tvPrecio);

        Button btnEditar = view.findViewById(R.id.btnEditar);
        Button btnComprar = view.findViewById(R.id.btnComprar);
        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        Spinner spinner = view.findViewById(R.id.spinnerPlataforma);

        nombre.setText(juego.getNombre());
        precio.setText("Precio estimado: " + MoneyUtils.format(juego.getPrecioEstimado(), moneda));

        Glide.with(requireContext()).load(juego.getImagenUrl()).into(img);

        btnEditar.setOnClickListener(v -> {
            String plataformaSeleccionada = spinner.getSelectedItem().toString();
            juego.setPlataforma(plataformaSeleccionada);
            wishlistViewModel.actualizar(juego);

            DialogEditarPrecioFragment dialog = DialogEditarPrecioFragment.newInstance(juego);

            dialog.setOnPrecioEditadoListener(() -> {
                dismiss();
            });

            dialog.show(getParentFragmentManager(), "editarPrecio");
        });

        btnComprar.setOnClickListener(v -> {
            DialogConfirmarCompraFragment.newInstance(juego, juego.getPrecioEstimado(), moneda).show(getParentFragmentManager(), "confirmarCompra");
            dismiss();
        });

        btnCancelar.setOnClickListener( v ->
            dismiss());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.plataformas_array,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (juego.getPlataforma() != null) {
            int position = adapter.getPosition(juego.getPlataforma());
            if (position >= 0) {
                spinner.setSelection(position);
            }
        }
        isInitialLoad = false;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInitialLoad) return;
                String plataformaSeleccionada = parent.getItemAtPosition(position).toString();
                juego.setPlataforma(plataformaSeleccionada);
                wishlistViewModel.actualizar(juego);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return new AlertDialog.Builder(requireContext()).setView(view).create();

    }
}

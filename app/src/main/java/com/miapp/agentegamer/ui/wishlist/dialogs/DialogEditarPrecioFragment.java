package com.miapp.agentegamer.ui.wishlist.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.local.entity.WishlistEntity;
import com.miapp.agentegamer.ui.viewmodel.WishlistViewModel;

public class DialogEditarPrecioFragment extends DialogFragment {

    private static final String ARG_JUEGO = "juego";

    private WishlistEntity juego;
    private WishlistViewModel viewModel;

    private OnPrecioEditadoListener listener;

    public static DialogEditarPrecioFragment newInstance(WishlistEntity juego){
        DialogEditarPrecioFragment fragment = new DialogEditarPrecioFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_JUEGO, juego);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnPrecioEditadoListener {
        void onPrecioEditado();
    }

    public void setOnPrecioEditadoListener(OnPrecioEditadoListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        juego = (WishlistEntity) getArguments().getSerializable(ARG_JUEGO);
        viewModel = new ViewModelProvider(requireActivity()).get(WishlistViewModel.class);

        EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setText(String.valueOf(juego.getPrecioEstimado()));

        AlertDialog dialog =  new AlertDialog.Builder(requireContext())
                .setTitle("Editar precio")
                .setMessage("Precio estimado para " + juego.getNombre())
                .setView(input)
                .setPositiveButton("Guardar", null)
                .setNegativeButton("Cancelar", null).create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String texto = input.getText().toString().trim();
                if (TextUtils.isEmpty(texto)) {
                    input.setError("Ingresa un precio");
                    return;
                }
                try {
                    double nuevoPrecio = Double.parseDouble(texto);
                    juego.setPrecioEstimado(nuevoPrecio);
                    viewModel.actualizar(juego);
                    if (listener != null) {
                        listener.onPrecioEditado();
                    }
                    dismiss();
                } catch (NumberFormatException e) {
                    input.setError("Precio inválido");
                }
            });
        });

        return dialog;
    }


}

package com.miapp.agentegamer.ui.gastos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.util.ImageLoader;
import com.miapp.agentegamer.util.MoneyUtils;

import java.util.ArrayList;
import java.util.List;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {

    private List<GastoEntity> lista = new ArrayList<>();

    // DiffUtil callback for efficient updates
    private static final DiffUtil.ItemCallback<GastoEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<GastoEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull GastoEntity oldItem, @NonNull GastoEntity newItem) {
            // Compare by ID (assuming GastoEntity has getId())
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull GastoEntity oldItem, @NonNull GastoEntity newItem) {
            // Compare by content (nombre and precio)
            return oldItem.getNombreJuego().equals(newItem.getNombreJuego()) &&
                    Double.compare(oldItem.getPrecio(), newItem.getPrecio()) == 0;
        }
    };

    public GastoAdapter() {
        // Empty constructor
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gasto, parent, false);
        return new GastoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        if (position < 0 || position >= lista.size()) {
            return;
        }
        GastoEntity g = lista.get(position);
        if (g == null) {
            return;
        }
        holder.textConcepto.setText(g.getNombreJuego());
        holder.textCantidad.setText(MoneyUtils.format(g.getPrecio()));
        ImageLoader.load(holder.ivIcon, g.getImagenUrl());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setLista(List<GastoEntity> nuevaLista) {
        // Make a defensive copy
        List<GastoEntity> copiaSegura = new ArrayList<>(nuevaLista);
        // Calculate diff
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new GastoDiffCallback(this.lista, copiaSegura));
        // Update internal list
        this.lista = copiaSegura;
        // Dispatch updates
        diffResult.dispatchUpdatesTo(this);
    }

    // Simple DiffUtil.Callback implementation
    private static class GastoDiffCallback extends DiffUtil.Callback {
        private final List<GastoEntity> oldList;
        private final List<GastoEntity> newList;

        GastoDiffCallback(List<GastoEntity> oldList, List<GastoEntity> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            GastoEntity oldItem = oldList.get(oldItemPosition);
            GastoEntity newItem = newList.get(newItemPosition);
            return oldItem.getNombreJuego().equals(newItem.getNombreJuego()) &&
                    Double.compare(oldItem.getPrecio(), newItem.getPrecio()) == 0;
        }
    }

    static class GastoViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;
        TextView textConcepto, textCantidad;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            textConcepto = itemView.findViewById(R.id.textConcepto);
            textCantidad = itemView.findViewById(R.id.textCantidad);
        }
    }
}


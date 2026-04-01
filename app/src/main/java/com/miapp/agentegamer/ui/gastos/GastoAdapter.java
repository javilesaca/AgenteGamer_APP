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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {

    private List<GastoEntity> lista = new ArrayList<>();
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("d 'de' MMMM yyyy", new Locale("es", "ES"));

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
        holder.tvNombre.setText(g.getNombreJuego());
        holder.tvPrecio.setText(MoneyUtils.format(g.getPrecio()));
        holder.tvFecha.setText(DATE_FORMAT.format(new Date(g.getFecha())));
        ImageLoader.load(holder.ivCover, g.getImagenUrl());
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

        ImageView ivCover;
        TextView tvNombre, tvFecha, tvPrecio;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}

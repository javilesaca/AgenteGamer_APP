package com.miapp.agentegamer.ui.lanzamientos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.local.entity.LanzamientoEntity;
import com.miapp.agentegamer.util.FechaUtils;
import com.miapp.agentegamer.util.MoneyUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LanzamientosAdapter extends RecyclerView.Adapter<LanzamientosAdapter.ViewHolder> {

    private List<LanzamientoEntity> lista = new ArrayList<>();

    // DiffUtil callback for efficient updates
    private static final DiffUtil.ItemCallback<LanzamientoEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<LanzamientoEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull LanzamientoEntity oldItem, @NonNull LanzamientoEntity newItem) {
            // Compare by gameId
            return oldItem.getGameId() == newItem.getGameId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull LanzamientoEntity oldItem, @NonNull LanzamientoEntity newItem) {
            // Compare by content (nombre, precio, fecha)
            return oldItem.getNombre().equals(newItem.getNombre()) &&
                    Double.compare(oldItem.getPrecioEstimado(), newItem.getPrecioEstimado()) == 0 &&
                    oldItem.getFechaLanzamiento() == newItem.getFechaLanzamiento();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lanzamiento, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        LanzamientoEntity l = lista.get(pos);
        h.bind(l);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setLista(List<LanzamientoEntity> nueva) {
        // Make a defensive copy
        List<LanzamientoEntity> copiaSegura = new ArrayList<>(nueva);
        // Calculate diff
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new LanzamientosDiffCallback(this.lista, copiaSegura));
        // Update internal list
        this.lista = copiaSegura;
        // Dispatch updates
        diffResult.dispatchUpdatesTo(this);
    }

    // Simple DiffUtil.Callback implementation
    private static class LanzamientosDiffCallback extends DiffUtil.Callback {
        private final List<LanzamientoEntity> oldList;
        private final List<LanzamientoEntity> newList;

        LanzamientosDiffCallback(List<LanzamientoEntity> oldList, List<LanzamientoEntity> newList) {
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
            return oldList.get(oldItemPosition).getGameId() == newList.get(newItemPosition).getGameId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            LanzamientoEntity oldItem = oldList.get(oldItemPosition);
            LanzamientoEntity newItem = newList.get(newItemPosition);
            return oldItem.getNombre().equals(newItem.getNombre()) &&
                    Double.compare(oldItem.getPrecioEstimado(), newItem.getPrecioEstimado()) == 0 &&
                    oldItem.getFechaLanzamiento() == newItem.getFechaLanzamiento();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, precio, dias, fecha;

        ViewHolder(View v) {
            super(v);
            nombre = v.findViewById(R.id.tvNombre);
            precio = v.findViewById(R.id.tvPrecio);
            dias = v.findViewById(R.id.tvDias);
            fecha = v.findViewById(R.id.tvFecha);
        }

        void bind(LanzamientoEntity l) {
            nombre.setText(l.getNombre());
            precio.setText(itemView.getContext().getString(R.string.precio_estimado, l.getPrecioEstimado()));

            long diasHasta = FechaUtils.diasHasta(l.getFechaLanzamiento());
            if (diasHasta == 0) {
                dias.setText(itemView.getContext().getString(R.string.lanzamiento_hoy));
            } else {
                dias.setText(itemView.getContext().getString(R.string.lanzamiento_en_dias, diasHasta));
            }

            // Format and display the release date
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            fecha.setText(sdf.format(new Date(l.getFechaLanzamiento())));
        }
    }
}
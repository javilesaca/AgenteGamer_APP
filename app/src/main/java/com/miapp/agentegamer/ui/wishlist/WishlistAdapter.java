package com.miapp.agentegamer.ui.wishlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.local.entity.WishlistEntity;
import com.miapp.agentegamer.util.MoneyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(WishlistEntity juego);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    private List<WishlistItemUI> lista = new ArrayList<>();
    private String moneda;

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wishlist, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {

        WishlistItemUI itemUI = lista.get(position);
        WishlistEntity juego = itemUI.getJuego();

        holder.nombre.setText(juego.getNombre());
        holder.precioValue.setText(MoneyUtils.format(juego.getPrecioEstimado(), moneda));
        holder.recomendacion.setText(itemUI.getEvaluacion());

        Glide.with(holder.itemView.getContext())
                        .load(juego.getImagenUrl())
                                .placeholder(R.drawable.ic_placeholder)
                                        .error(R.drawable.ic_placeholder)
                                                .into(holder.imgJuego);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(itemUI.getJuego());
            }
        });
        holder.btnEliminar.setOnClickListener(v -> {
            if (listenerEliminar != null) {
                listenerEliminar.onEliminarClick(juego);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setLista(List<WishlistItemUI> nuevalista, String moneda) {
        this.moneda = moneda;
        // Make a defensive copy
        List<WishlistItemUI> copiaSegura = new ArrayList<>(nuevalista);
        // Calculate diff
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new WishlistDiffCallback(this.lista, copiaSegura));
        // Update internal list
        this.lista = copiaSegura;
        // Dispatch updates
        diffResult.dispatchUpdatesTo(this);
    }

    // Simple DiffUtil.Callback implementation
    private static class WishlistDiffCallback extends DiffUtil.Callback {
        private final List<WishlistItemUI> oldList;
        private final List<WishlistItemUI> newList;

        WishlistDiffCallback(List<WishlistItemUI> oldList, List<WishlistItemUI> newList) {
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
            return oldList.get(oldItemPosition).getJuego().getGameId() == 
                   newList.get(newItemPosition).getJuego().getGameId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            WishlistItemUI oldItem = oldList.get(oldItemPosition);
            WishlistItemUI newItem = newList.get(newItemPosition);
            return oldItem.getJuego().getNombre().equals(newItem.getJuego().getNombre()) &&
                    Double.compare(oldItem.getJuego().getPrecioEstimado(), newItem.getJuego().getPrecioEstimado()) == 0 &&
                    Objects.equals(oldItem.getEvaluacion(), newItem.getEvaluacion());
        }
    }

    static class WishlistViewHolder extends RecyclerView.ViewHolder{
        TextView nombre, precioValue, recomendacion;
        ImageButton btnEliminar;
        ImageView imgJuego;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombreJuego);
            precioValue = itemView.findViewById(R.id.tvPrecioValue);
            recomendacion = itemView.findViewById(R.id.tvRecomendacion);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            imgJuego = itemView.findViewById(R.id.imgJuego);

        }
    }

    public interface OnEliminarClickListener {
        void onEliminarClick(WishlistEntity juego);
    }
    private OnEliminarClickListener listenerEliminar;

    public void setOnEliminarClickListener(OnEliminarClickListener listener) {
        this.listenerEliminar = listener;
    }



 }

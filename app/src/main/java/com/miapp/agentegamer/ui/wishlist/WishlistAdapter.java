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

/**
 * WishlistAdapter
 * ---------------
 * Adapter de RecyclerView para mostrar la lista de deseos del usuario.
 * Utiliza DiffUtil para actualizaciones eficientes de la lista.
 * 
 * Cada elemento muestra: imagen del juego, nombre, precio estimado,
 * evaluación financiera (recomendada/ajustada/no recomendada) y botón de eliminación.
 * 
 * @see WishlistItemUI
 * @see WishlistEntity
 * @see DiffUtil
 */
public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    /**
     * Interfaz para manejar eventos de clic en un elemento de la wishlist.
     */
    public interface OnItemClickListener {
        /**
         * Se llama cuando el usuario hace clic en un juego de la wishlist.
         * 
         * @param juego Entidad del juego clickeado
         */
        void onItemClick(WishlistEntity juego);
    }

    // Listener para clics en elementos
    private OnItemClickListener listener;
    
    // Lista de elementos de la wishlist con evaluación financiera
    private List<WishlistItemUI> lista = new ArrayList<>();
    // Moneda actual para formatear precios
    private String moneda;

    // DiffUtil callback for efficient updates
    private static final DiffUtil.ItemCallback<WishlistItemUI> DIFF_CALLBACK = new DiffUtil.ItemCallback<WishlistItemUI>() {
        @Override
        public boolean areItemsTheSame(@NonNull WishlistItemUI oldItem, @NonNull WishlistItemUI newItem) {
            // Compare by game ID
            return oldItem.getJuego().getGameId() == newItem.getJuego().getGameId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull WishlistItemUI oldItem, @NonNull WishlistItemUI newItem) {
            // Compare by content (nombre, precio, evaluacion)
            return oldItem.getJuego().getNombre().equals(newItem.getJuego().getNombre()) &&
                    Double.compare(oldItem.getJuego().getPrecioEstimado(), newItem.getJuego().getPrecioEstimado()) == 0 &&
                    Objects.equals(oldItem.getEvaluacion(), newItem.getEvaluacion());
        }
    };

    /**
     * Establece el listener para clics en elementos.
     * 
     * @param listener Listener a establecer
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Crea un nuevo ViewHolder para el RecyclerView.
     * Infla el layout del item de wishlist.
     * 
     * @param parent Vista padre del ViewHolder
     * @param viewType Tipo de vista
     * @return Nuevo WishlistViewHolder con la vista inflada
     */
    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wishlist, parent, false);
        return new WishlistViewHolder(view);
    }

    /**
     * Asocia los datos de un elemento de wishlist a su ViewHolder.
     * Asigna nombre, precio formateado, evaluación, imagen y configura los listeners.
     * 
     * @param holder ViewHolder que contendrá los datos
     * @param position Posición del elemento en la lista
     */
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

    /**
     * Retorna el número de elementos en la lista de wishlist.
     * 
     * @return Cantidad de elementos en la lista
     */
    @Override
    public int getItemCount() {
        return lista.size();
    }

    /**
     * Actualiza la lista de elementos del adapter.
     * Utiliza DiffUtil para actualizar solo los elementos que cambian.
     * 
     * @param nuevalista Nueva lista de elementos a mostrar
     * @param moneda Código de moneda para formatear precios
     */
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

    /**
     * ViewHolder para los elementos de la wishlist en el RecyclerView.
     * Contiene referencias a: nombre, precio (label y valor), recomendación,
     * badge de precio accesible, botón de eliminar e imagen del juego.
     */
    static class WishlistViewHolder extends RecyclerView.ViewHolder{
        TextView nombre, precioLabel, precioValue, recomendacion, affordableBadge;
        ImageButton btnEliminar;
        ImageView imgJuego;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombreJuego);
            precioLabel = itemView.findViewById(R.id.tvPrecioLabel);
            precioValue = itemView.findViewById(R.id.tvPrecioValue);
            recomendacion = itemView.findViewById(R.id.tvRecomendacion);
            affordableBadge = itemView.findViewById(R.id.tvAffordableBadge);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            imgJuego = itemView.findViewById(R.id.imgJuego);

        }
    }

    /**
     * Interfaz para manejar eventos de clic en el botón de eliminar.
     */
    public interface OnEliminarClickListener {
        /**
         * Se llama cuando el usuario hace clic en el botón de eliminar de un juego.
         * 
         * @param juego Entidad del juego a eliminar
         */
        void onEliminarClick(WishlistEntity juego);
    }
    // Listener para el botón de eliminación
    private OnEliminarClickListener listenerEliminar;

    /**
     * Establece el listener para el botón de eliminar.
     * 
     * @param listener Listener a establecer
     */
    public void setOnEliminarClickListener(OnEliminarClickListener listener) {
        this.listenerEliminar = listener;
    }



 }

package com.miapp.agentegamer.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.util.ImageLoader;
import com.miapp.agentegamer.util.MoneyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * UltimosGastosAdapter
 * --------------------
 * Adapter de RecyclerView para mostrar los últimos gastos del usuario
 * en un formato horizontal en el Dashboard de MainActivity.
 * 
 * Características:
 * - Muestra los últimos gastos en un RecyclerView horizontal
 * - Cada elemento muestra: imagen del juego, nombre, precio y fecha relativa
 * - Formatea la fecha como tiempo relativo ("Hace 2 días", "Ayer", etc.)
 * - Listener para manejar clics en los gastos
 * 
 * @see GastoEntity
 * @see MoneyUtils
 * @see ImageLoader
 */
public class UltimosGastosAdapter extends RecyclerView.Adapter<UltimosGastosAdapter.ViewHolder> {

    // Lista de gastos a mostrar
    private List<GastoEntity> gastos = new ArrayList<>();
    // Listener para clics en gastos
    private OnGastoClickListener listener;
    // Moneda actual para formatear precios
    private String moneda;

    /**
     * Interfaz para manejar eventos de clic en un gasto.
     */
    public interface OnGastoClickListener {
        /**
         * Se llama cuando el usuario hace clic en un gasto.
         * 
         * @param gasto Gasto clickeado
         */
        void onGastoClick(GastoEntity gasto);
    }

    /**
     * Establece el listener para clics en gastos.
     * 
     * @param listener Listener a establecer
     */
    public void setOnGastoClickListener(OnGastoClickListener listener) {
        this.listener = listener;
    }

    /**
     * Actualiza la lista de gastos del adapter.
     * 
     * @param gastos Nueva lista de gastos a mostrar
     */
    public void setGastos(List<GastoEntity> gastos) {
        this.gastos = gastos != null ? gastos : new ArrayList<>();
        notifyDataSetChanged();
    }

    /**
     * Establece la moneda para formatear los precios.
     * 
     * @param moneda Código de moneda (EUR, USD, GBP)
     */
    public void setMoneda(String moneda) {
        this.moneda = moneda;
        notifyDataSetChanged();
    }

    /**
     * Crea un nuevo ViewHolder para el RecyclerView.
     * 
     * @param parent Vista padre del ViewHolder
     * @param viewType Tipo de vista
     * @return Nuevo ViewHolder con la vista inflada
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ultimo_gasto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GastoEntity gasto = gastos.get(position);
        holder.bind(gasto);
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }

    /**
     * ViewHolder para los elementos de último gasto.
     * Muestra imagen del juego, nombre, precio y fecha relativa.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivGameImage;
        private final TextView tvNombreJuego;
        private final TextView tvPrecio;
        private final TextView tvFechaRelativa;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGameImage = itemView.findViewById(R.id.ivGameImage);
            tvNombreJuego = itemView.findViewById(R.id.tvNombreJuego);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvFechaRelativa = itemView.findViewById(R.id.tvFechaRelativa);
        }

        void bind(GastoEntity gasto) {
            tvNombreJuego.setText(gasto.getNombreJuego());
            tvPrecio.setText(MoneyUtils.format(gasto.getPrecio(), moneda));
            tvFechaRelativa.setText(getFechaRelativa(gasto.getFecha()));
            ImageLoader.load(ivGameImage, gasto.getImagenUrl());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onGastoClick(gasto);
                }
            });
        }

        private String getFechaRelativa(long timestamp) {
            if (timestamp == 0) return "Fecha desconocida";

            long diff = System.currentTimeMillis() - timestamp;
            
            if (diff < TimeUnit.MINUTES.toMillis(1)) {
                return "Ahora mismo";
            } else if (diff < TimeUnit.HOURS.toMillis(1)) {
                long mins = TimeUnit.MILLISECONDS.toMinutes(diff);
                return mins + " min atrás";
            } else if (diff < TimeUnit.DAYS.toMillis(1)) {
                long hours = TimeUnit.MILLISECONDS.toHours(diff);
                return hours + "h atrás";
            } else if (diff < TimeUnit.DAYS.toMillis(7)) {
                long days = TimeUnit.MILLISECONDS.toDays(diff);
                return days == 1 ? "Ayer" : "Hace " + days + " días";
            } else if (diff < TimeUnit.DAYS.toMillis(30)) {
                long weeks = TimeUnit.MILLISECONDS.toDays(diff) / 7;
                return weeks == 1 ? "Hace 1 semana" : "Hace " + weeks + " semanas";
            } else {
                long months = TimeUnit.MILLISECONDS.toDays(diff) / 30;
                return months == 1 ? "Hace 1 mes" : "Hace " + months + " meses";
            }
        }
    }
}

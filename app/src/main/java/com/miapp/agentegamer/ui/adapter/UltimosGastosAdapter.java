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
 * Adapter para el RecyclerView horizontal de últimos gastos en el Dashboard.
 */
public class UltimosGastosAdapter extends RecyclerView.Adapter<UltimosGastosAdapter.ViewHolder> {

    private List<GastoEntity> gastos = new ArrayList<>();
    private OnGastoClickListener listener;
    private String moneda;

    public interface OnGastoClickListener {
        void onGastoClick(GastoEntity gasto);
    }

    public void setOnGastoClickListener(OnGastoClickListener listener) {
        this.listener = listener;
    }

    public void setGastos(List<GastoEntity> gastos) {
        this.gastos = gastos != null ? gastos : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

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

package com.miapp.agentegamer.ui.lanzamientos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.local.entity.LanzamientoEntity;
import com.miapp.agentegamer.util.FechaUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanzamientosAdapter extends RecyclerView.Adapter<LanzamientosAdapter.ViewHolder> {

    private List<LanzamientoEntity> lista = new ArrayList<>();

    // DiffUtil callback for efficient updates
    private static final DiffUtil.ItemCallback<LanzamientoEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<LanzamientoEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull LanzamientoEntity oldItem, @NonNull LanzamientoEntity newItem) {
            return oldItem.getGameId() == newItem.getGameId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull LanzamientoEntity oldItem, @NonNull LanzamientoEntity newItem) {
            return oldItem.getNombre().equals(newItem.getNombre()) &&
                    Double.compare(oldItem.getPrecioEstimado(), newItem.getPrecioEstimado()) == 0 &&
                    oldItem.getFechaLanzamiento() == newItem.getFechaLanzamiento() &&
                    oldItem.getImageUrl().equals(newItem.getImageUrl());
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
        List<LanzamientoEntity> copiaSegura = new ArrayList<>(nueva);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new LanzamientosDiffCallback(this.lista, copiaSegura));
        this.lista = copiaSegura;
        diffResult.dispatchUpdatesTo(this);
    }

    private static class LanzamientosDiffCallback extends DiffUtil.Callback {
        private final List<LanzamientoEntity> oldList;
        private final List<LanzamientoEntity> newList;

        LanzamientosDiffCallback(List<LanzamientoEntity> oldList, List<LanzamientoEntity> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() { return oldList.size(); }

        @Override
        public int getNewListSize() { return newList.size(); }

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
                    oldItem.getFechaLanzamiento() == newItem.getFechaLanzamiento() &&
                    oldItem.getImageUrl().equals(newItem.getImageUrl());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvNombre, tvFecha, tvDias, tvRating;
        LinearLayout platformsRow;

        ViewHolder(View v) {
            super(v);
            ivCover = v.findViewById(R.id.ivCover);
            tvNombre = v.findViewById(R.id.tvNombre);
            tvFecha = v.findViewById(R.id.tvFecha);
            tvDias = v.findViewById(R.id.tvDias);
            tvRating = v.findViewById(R.id.tvRating);
            platformsRow = v.findViewById(R.id.platformsRow);
        }

        void bind(LanzamientoEntity l) {
            // Cover image
            if (l.getImageUrl() != null && !l.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(l.getImageUrl())
                        .centerCrop()
                        .placeholder(R.drawable.bg_image_placeholder)
                        .into(ivCover);
            } else {
                ivCover.setImageResource(R.drawable.bg_image_placeholder);
            }

            // Name
            tvNombre.setText(l.getNombre());

            // Release date
            SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es"));
            tvFecha.setText(sdf.format(new java.util.Date(l.getFechaLanzamiento())));

            // Days badge
            long diasHasta = FechaUtils.diasHasta(l.getFechaLanzamiento());
            if (diasHasta == 0) {
                tvDias.setText(itemView.getContext().getString(R.string.lanzamiento_hoy));
            } else {
                tvDias.setText(itemView.getContext().getString(R.string.lanzamiento_en_dias, diasHasta));
            }

            // Rating (only if > 0)
            if (l.getRating() > 0) {
                tvRating.setText(String.format(Locale.getDefault(), "★ %.1f", l.getRating()));
                tvRating.setVisibility(View.VISIBLE);
            } else {
                tvRating.setVisibility(View.GONE);
            }

            // Platforms
            platformsRow.removeAllViews();
            if (l.getPlataformas() != null && !l.getPlataformas().isEmpty()) {
                platformsRow.setVisibility(View.VISIBLE);
                String[] platforms = l.getPlataformas().split(", ");
                for (String platform : platforms) {
                    TextView tv = new TextView(itemView.getContext());
                    tv.setText(platform.trim());
                    tv.setTextSize(11);
                    tv.setTextColor(itemView.getContext().getColor(R.color.text_hint));
                    tv.setPadding(12, 6, 12, 6);
                    tv.setBackgroundResource(R.drawable.bg_badge_green);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 8, 0);
                    tv.setLayoutParams(params);
                    platformsRow.addView(tv);
                }
            } else {
                platformsRow.setVisibility(View.GONE);
            }
        }
    }
}

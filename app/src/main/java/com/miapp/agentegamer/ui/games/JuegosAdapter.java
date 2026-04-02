package com.miapp.agentegamer.ui.games;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.remote.model.GameDto;
import com.miapp.agentegamer.ui.common.TouchFeedback;
import com.miapp.agentegamer.util.ImageLoader;
import com.miapp.agentegamer.util.MoneyUtils;
import com.miapp.agentegamer.util.PlatformUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JuegosAdapter extends RecyclerView.Adapter<JuegosAdapter.JuegoViewHolder> {

    private List<GameDto> lista = new ArrayList<>();
    private OnJuegoClickListener listener;
    private String moneda;

    public interface OnJuegoClickListener {
        void onJuegoClick(GameDto juego, double precioEstimado);
    }

    public void setOnJuegoClickListener(OnJuegoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public JuegoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_stack, parent, false);
        return new JuegoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull JuegoViewHolder holder, int position) {

        GameDto juego = lista.get(position);


        // Texto
        holder.nombre.setText(juego.getName());

        String info = "⭐ " + juego.getRating() + " / " + MoneyUtils.format(juego.getPrecioEstimado(), moneda);

        holder.subtitulo.setText(info);

        // Imagen (portada)
        ImageLoader.load(holder.cover, juego.getImageUrl());

        // Micro-interacción táctil
        TouchFeedback.apply(holder.root);

        // Click
        holder.root.setOnClickListener(v -> {
            if (listener != null) {
                listener.onJuegoClick(juego, juego.getPrecioEstimado());
            }
        });

        //Plataformas
        holder.platforms.removeAllViews();

        Set<String> familias = PlatformUtils.getPlatformFamilies(juego);

        int size = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20,
                holder.itemView.getResources().getDisplayMetrics()
        );

        for (String family : familias) {

            int iconRes = PlatformUtils.getFamilyIcon(family);

            if (iconRes != 0) {

                ImageView icon = new ImageView(holder.itemView.getContext());

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(size, size);

                params.setMarginEnd(12);
                icon.setLayoutParams(params);

                icon.setImageResource(iconRes);
                icon.setAlpha(0.9f);

                icon.setBackgroundResource(R.drawable.bg_platform_badge);
                icon.setPadding(8,8,8,8);

                holder.platforms.addView(icon);
            }
        }
    }


    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setLista(List<GameDto> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    private int getPlatformIcon(String slug) {

        switch (slug) {
            case "pc":
                return R.drawable.pcmag_idepwrpbfb_0;

            case "playstation5":
            case "playstation4":
            case "playstation":
                return R.drawable.playstation_logo_colour;

            case "xbox-series-x":
            case "xbox-one":
            case "xbox":
                return R.drawable.xbox;

            case "nintendo-switch":
            case "nintendo":
                return R.drawable.nintendo_blue_logo;

            default:
                return 0;
        }
    }


    static class JuegoViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, subtitulo;
        View root;
        ImageView cover;
        LinearLayout platforms;

        public  JuegoViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            cover = itemView.findViewById(R.id.ivCover);
            nombre = itemView.findViewById(R.id.tvNombreJuego);
            subtitulo = itemView.findViewById(R.id.tvSubtitulo);
            platforms = itemView.findViewById(R.id.layoutPlatforms);

        }
    }
}

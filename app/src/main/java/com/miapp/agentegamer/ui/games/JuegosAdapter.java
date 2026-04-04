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

/**
 * JuegosAdapter
 * -------------
 * Adapter de RecyclerView para mostrar una lista de juegos del catálogo.
 * Muestra juegos en formato grid con imagen, nombre, rating y plataformas.
 * 
 * Características:
 * - Muestra la portada del juego, nombre, rating y precio estimado
 * - Iconos de plataformas agrupados por familia (PlayStation, Xbox, Nintendo, PC)
 * - Aplicación de efecto táctil (TouchFeedback) en cada elemento
 * - Listener para manejar clics en los juegos (agregar a wishlist)
 * 
 * @see GameDto
 * @see OnJuegoClickListener
 * @see TouchFeedback
 */
public class JuegosAdapter extends RecyclerView.Adapter<JuegosAdapter.JuegoViewHolder> {

    // Lista de juegos que se muestra
    private List<GameDto> lista = new ArrayList<>();
    // Listener para manejar clics en juegos
    private OnJuegoClickListener listener;
    // Moneda actual para formatear precios
    private String moneda;

    /**
     * Interfaz para manejar eventos de clic en un juego.
     */
    public interface OnJuegoClickListener {
        /**
         * Se llama cuando el usuario hace clic en un juego.
         * 
         * @param juego Juego seleccionado
         * @param precioEstimado Precio estimado del juego
         */
        void onJuegoClick(GameDto juego, double precioEstimado);
    }

    /**
     * Establece el listener para clics en juegos.
     * 
     * @param listener Listener a establecer
     */
    public void setOnJuegoClickListener(OnJuegoClickListener listener) {
        this.listener = listener;
    }

    /**
     * Crea un nuevo ViewHolder para el RecyclerView.
     * Infla el layout del item de juego.
     * 
     * @param parent Vista padre del ViewHolder
     * @param viewType Tipo de vista
     * @return Nuevo JuegoViewHolder con la vista inflada
     */
    @NonNull
    @Override
    public JuegoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_stack, parent, false);
        return new JuegoViewHolder(vista);
    }

    /**
     * Asocia los datos de un juego a su ViewHolder.
     * Asigna el nombre, imagen, rating, precio y genera los iconos de plataformas.
     * 
     * @param holder ViewHolder que contendrá los datos
     * @param position Posición del elemento en la lista
     */
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
    }


    /**
     * Retorna el número de elementos en la lista de juegos.
     * 
     * @return Cantidad de juegos en la lista
     */
    @Override
    public int getItemCount() {
        return lista.size();
    }

    /**
     * Actualiza la lista de juegos del adapter y refresca el RecyclerView.
     * 
     * @param nuevaLista Nueva lista de juegos a mostrar
     */
    public void setLista(List<GameDto> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    /**
     * Establece la moneda para formatear los precios de los juegos.
     * 
     * @param moneda Código de moneda (EUR, USD, GBP)
     */
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    /**
     * Obtiene el recurso drawable del icono de una plataforma específica.
     * Método legado mantenido por compatibilidad (usar PlatformUtils preferiblemente).
     * 
     * @param slug Slug de la plataforma (pc, playstation5, xbox-one, etc.)
     * @return ID del recurso drawable del icono, o 0 si no se encuentra
     */
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

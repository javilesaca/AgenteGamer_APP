package com.miapp.agentegamer.util;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.remote.model.GameDto;

import java.util.HashSet;
import java.util.Set;

/**
 * PlatformUtils
 * -------------
 * Utilidad para el manejo de plataformas de videojuegos.
 * Proporciona métodos estáticos para obtener familias de plataformas
 * e iconos correspondientes.
 * 
 * Características:
 * - Consolida plataformas similares en familias (PS4+PS5 = playstation)
 * - Provee iconos drawable para cada familia de plataforma
 * - Extraída de GamesViewModel para evitar crear instancias de ViewModel en adapters
 * 
 * Familias soportadas:
 * - playstation (PS4, PS5, PS3, PS Vita, etc.)
 * - xbox (Xbox One, Xbox Series X, Xbox 360, etc.)
 * - nintendo (Switch, Wii U, 3DS, etc.)
 * - pc (Windows, Linux, Mac)
 * 
 * @see GameDto
 * @see android.widget.ImageView
 */
public final class PlatformUtils {

    // Constructor privado para evitar instanciación (clase de utilería)
    private PlatformUtils() {
        // Utility class - no instantiation
    }

    /**
     * Obtiene las familias únicas de plataformas de un juego.
     * Consolida plataformas similares en familias para evitar iconos duplicados.
     * Por ejemplo: PS4, PS5 -> playstation (un solo icono).
     * 
     * @param juego Juego del que obtener las familias de plataformas
     * @return Conjunto de familias de plataformas únicas (sin duplicados)
     */
    public static Set<String> getPlatformFamilies(GameDto juego) {
        Set<String> familias = new HashSet<>();

        if (juego == null || juego.getPlatforms() == null) {
            return familias;
        }

        for (GameDto.PlatformWrapper wrapper : juego.getPlatforms()) {
            if (wrapper.getPlatform() == null) continue;

            String slug = wrapper.getPlatform().getSlug();
            if (slug == null) continue;

            // Consolidate platforms to avoid duplicate icons
            if (slug.contains("playstation")) {
                familias.add("playstation");
            } else if (slug.contains("xbox")) {
                familias.add("xbox");
            } else if (slug.contains("nintendo")) {
                familias.add("nintendo");
            } else if (slug.contains("pc")) {
                familias.add("pc");
            }
        }

        return familias;
    }

    /**
     * Obtiene el ID del recurso drawable del icono de una familia de plataformas.
     * 
     * @param family Familia de plataforma (playstation, xbox, nintendo, pc)
     * @return ID del recurso drawable del icono, o 0 si no se reconoce la familia
     */
    public static int getFamilyIcon(String family) {
        if (family == null) return 0;

        switch (family) {
            case "playstation":
                return R.drawable.playstation_logo_colour;
            case "xbox":
                return R.drawable.xbox;
            case "nintendo":
                return R.drawable.nintendo_blue_logo;
            case "pc":
                return R.drawable.pcmag_idepwrpbfb_0;
            default:
                return 0;
        }
    }
}

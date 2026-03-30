package com.miapp.agentegamer.util;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.remote.model.GameDto;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for platform handling.
 * Extracted from GamesViewModel to avoid creating ViewModel instances in adapters.
 */
public final class PlatformUtils {

    private PlatformUtils() {
        // Utility class - no instantiation
    }

    /**
     * Get unique platform families from a game.
     * Consolidates similar platforms (e.g., PS4, PS5 -> playstation)
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
     * Get the drawable resource ID for a platform family icon.
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

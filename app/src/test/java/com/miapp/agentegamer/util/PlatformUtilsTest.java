package com.miapp.agentegamer.util;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import com.miapp.agentegamer.data.remote.model.GameDto;
import com.miapp.agentegamer.data.remote.model.GameDto.Platform;
import com.miapp.agentegamer.data.remote.model.GameDto.PlatformWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Unit tests for PlatformUtils
 */
public class PlatformUtilsTest {

    @Test
    public void testGetPlatformFamilies_withPlaystation_returnsPlaystation() {
        // Arrange
        GameDto game = createGameWithPlatforms("playstation5", "playstation4");

        // Act
        Set<String> families = PlatformUtils.getPlatformFamilies(game);

        // Assert
        assertEquals(1, families.size());
        assertTrue(families.contains("playstation"));
    }

    @Test
    public void testGetPlatformFamilies_withXbox_returnsXbox() {
        // Arrange
        GameDto game = createGameWithPlatforms("xbox-series-x", "xbox-one");

        // Act
        Set<String> families = PlatformUtils.getPlatformFamilies(game);

        // Assert
        assertEquals(1, families.size());
        assertTrue(families.contains("xbox"));
    }

    @Test
    public void testGetPlatformFamilies_withNintendo_returnsNintendo() {
        // Arrange
        GameDto game = createGameWithPlatforms("nintendo-switch");

        // Act
        Set<String> families = PlatformUtils.getPlatformFamilies(game);

        // Assert
        assertEquals(1, families.size());
        assertTrue(families.contains("nintendo"));
    }

    @Test
    public void testGetPlatformFamilies_withPC_returnsPC() {
        // Arrange
        GameDto game = createGameWithPlatforms("pc", "linux", "mac");

        // Act
        Set<String> families = PlatformUtils.getPlatformFamilies(game);

        // Assert
        assertEquals(1, families.size());
        assertTrue(families.contains("pc"));
    }

    @Test
    public void testGetPlatformFamilies_withMultiplePlatforms_returnsUniqueFamilies() {
        // Arrange
        GameDto game = createGameWithPlatforms("playstation5", "xbox-series-x", "nintendo-switch", "pc");

        // Act
        Set<String> families = PlatformUtils.getPlatformFamilies(game);

        // Assert
        assertEquals(4, families.size());
        assertTrue(families.contains("playstation"));
        assertTrue(families.contains("xbox"));
        assertTrue(families.contains("nintendo"));
        assertTrue(families.contains("pc"));
    }

    @Test
    public void testGetPlatformFamilies_withNullGame_returnsEmptySet() {
        // Act
        Set<String> families = PlatformUtils.getPlatformFamilies(null);

        // Assert
        assertTrue(families.isEmpty());
    }

    @Test
    public void testGetPlatformFamilies_withNullPlatforms_returnsEmptySet() {
        // Arrange
        GameDto game = new GameDto();
        game.setName("Test Game");
        game.setPlatforms(null);

        // Act
        Set<String> families = PlatformUtils.getPlatformFamilies(game);

        // Assert
        assertTrue(families.isEmpty());
    }

    @Test
    public void testGetFamilyIcon_playstation_returnsDrawable() {
        // Act
        int icon = PlatformUtils.getFamilyIcon("playstation");

        // Assert
        assertTrue(icon != 0);
    }

    @Test
    public void testGetFamilyIcon_xbox_returnsDrawable() {
        // Act
        int icon = PlatformUtils.getFamilyIcon("xbox");

        // Assert
        assertTrue(icon != 0);
    }

    @Test
    public void testGetFamilyIcon_nintendo_returnsDrawable() {
        // Act
        int icon = PlatformUtils.getFamilyIcon("nintendo");

        // Assert
        assertTrue(icon != 0);
    }

    @Test
    public void testGetFamilyIcon_pc_returnsDrawable() {
        // Act
        int icon = PlatformUtils.getFamilyIcon("pc");

        // Assert
        assertTrue(icon != 0);
    }

    @Test
    public void testGetFamilyIcon_unknown_returnsZero() {
        // Act
        int icon = PlatformUtils.getFamilyIcon("unknown-platform");

        // Assert
        assertEquals(0, icon);
    }

    @Test
    public void testGetFamilyIcon_null_returnsZero() {
        // Act
        int icon = PlatformUtils.getFamilyIcon(null);

        // Assert
        assertEquals(0, icon);
    }

    /**
     * Helper method to create a GameDto with specified platform slugs
     */
    private GameDto createGameWithPlatforms(String... platformSlugs) {
        GameDto game = new GameDto();
        game.setName("Test Game");

        List<GameDto.PlatformWrapper> platforms = new ArrayList<>();
        for (String slug : platformSlugs) {
            GameDto.PlatformWrapper wrapper = game.new PlatformWrapper();
            GameDto.Platform platform = game.new Platform();
            platform.setSlug(slug);
            wrapper.setPlatform(platform);
            platforms.add(wrapper);
        }
        game.setPlatforms(platforms);

        return game;
    }
}

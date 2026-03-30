package com.miapp.agentegamer.data.local.entity;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for WishlistEntity
 * 
 * This tests the constructor parameter order fix:
 * Previously precioEstimado and plataforma were swapped causing data corruption.
 */
public class WishlistEntityTest {

    @Test
    public void testConstructor_assignsFieldsCorrectly() {
        // Act - Using the correct parameter order: plataforma before precioEstimado
        // (gameId, nombre, fechaLanzamiento, imagenUrl, plataforma, precioEstimado)
        WishlistEntity entity = new WishlistEntity(
            123,                    // gameId
            "The Legend of Zelda",  // nombre
            "2024-05-12",           // fechaLanzamiento
            "https://example.com/img.png",  // imagenUrl
            "Nintendo Switch",       // plataforma
            69.99                   // precioEstimado
        );

        // Assert - Verify each field is assigned to the correct parameter
        assertEquals(123, entity.getGameId());
        assertEquals("The Legend of Zelda", entity.getNombre());
        assertEquals("2024-05-12", entity.getFechaLanzamiento());
        assertEquals("https://example.com/img.png", entity.getImagenUrl());
        assertEquals("Nintendo Switch", entity.getPlataforma());
        assertEquals(69.99, entity.getPrecioEstimado(), 0.001);
    }

    @Test
    public void testConstructor_precioEstimadoAndPlataformaNotSwapped() {
        // This test specifically verifies the bug fix:
        // Before the fix, constructor params were: (..., precioEstimado, plataforma)
        // After the fix, constructor params are: (..., plataforma, precioEstimado)
        
        // Using distinct values to detect swap
        WishlistEntity entity = new WishlistEntity(
            1,          // gameId
            "Game",     // nombre
            "2024-01-01",  // fechaLanzamiento
            "url",      // imagenUrl
            "PS5",      // plataforma - should be 59.99
            59.99       // precioEstimado - should be "PS5"
        );

        // Verify plataforma is "PS5" (not 59.99)
        assertEquals("PS5", entity.getPlataforma());
        
        // Verify precioEstimado is 59.99 (not "PS5")
        assertEquals(59.99, entity.getPrecioEstimado(), 0.001);
    }

    @Test
    public void testConstructor_withZeroPrecio() {
        // Arrange & Act
        WishlistEntity entity = new WishlistEntity(
            1, "Free Game", "2024-01-01", "url", "PC", 0.0
        );

        // Assert
        assertEquals("PC", entity.getPlataforma());
        assertEquals(0.0, entity.getPrecioEstimado(), 0.001);
    }

    @Test
    public void testConstructor_withEmptyStrings() {
        // Arrange & Act
        WishlistEntity entity = new WishlistEntity(
            1, "", "", "", "", 0.0
        );

        // Assert
        assertEquals("", entity.getNombre());
        assertEquals("", entity.getFechaLanzamiento());
        assertEquals("", entity.getImagenUrl());
        assertEquals("", entity.getPlataforma());
        assertEquals(0.0, entity.getPrecioEstimado(), 0.001);
    }

    @Test
    public void testSetPlataforma() {
        // Arrange
        WishlistEntity entity = new WishlistEntity(
            1, "Game", "2024-01-01", "url", "PS4", 29.99
        );

        // Act
        entity.setPlataforma("PS5");

        // Assert
        assertEquals("PS5", entity.getPlataforma());
    }

    @Test
    public void testGetPlataforma() {
        // Arrange
        WishlistEntity entity = new WishlistEntity(
            42, "My Game", "2024-06-15", "image.jpg", "Xbox Series X", 49.99
        );

        // Act & Assert
        assertEquals("Xbox Series X", entity.getPlataforma());
    }

    @Test
    public void testGameId_remainsConsistent() {
        // Arrange
        WishlistEntity entity = new WishlistEntity(
            999, "Game", "2024-01-01", "url", "PC", 19.99
        );

        // Assert
        assertEquals(999, entity.getGameId());
    }

    @Test
    public void testSerializable() {
        // WishlistEntity implements Serializable
        // This test verifies basic instantiation
        WishlistEntity entity = new WishlistEntity(
            1, "Test", "2024-01-01", "url", "PC", 10.0
        );
        
        assertNotNull(entity);
    }
}

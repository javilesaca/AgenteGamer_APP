package com.miapp.agentegamer.data.local.entity;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Unit tests for GastoEntity
 */
public class GastoEntityTest {

    @Test
    public void testConstructor_calculatesMesAnioFromFecha() {
        // Arrange - Create a timestamp for January 15, 2024
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2024, Calendar.JANUARY, 15, 12, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long timestamp = cal.getTimeInMillis();

        // Act
        GastoEntity gasto = new GastoEntity("Test Game", 59.99, timestamp);

        // Assert
        assertEquals(1, gasto.getMes());  // January = 0 in Calendar, so +1 = 1
        assertEquals(2024, gasto.getAnio());
    }

    @Test
    public void testConstructor_calculatesDecemberCorrectly() {
        // Arrange - Create a timestamp for December 25, 2024
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2024, Calendar.DECEMBER, 25, 12, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long timestamp = cal.getTimeInMillis();

        // Act
        GastoEntity gasto = new GastoEntity("Test Game", 59.99, timestamp);

        // Assert
        assertEquals(12, gasto.getMes());  // December = 11 in Calendar, so +1 = 12
        assertEquals(2024, gasto.getAnio());
    }

    @Test
    public void testSetFecha_updatesMesAnio() {
        // Arrange
        Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal1.set(2024, Calendar.JANUARY, 15, 12, 0, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        long timestamp1 = cal1.getTimeInMillis();

        GastoEntity gasto = new GastoEntity("Test Game", 59.99, timestamp1);
        assertEquals(1, gasto.getMes());
        assertEquals(2024, gasto.getAnio());

        // Act - Update to a different month/year
        Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal2.set(2025, Calendar.JUNE, 20, 12, 0, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        long timestamp2 = cal2.getTimeInMillis();

        gasto.setFecha(timestamp2);

        // Assert - mes and anio should be updated
        assertEquals(6, gasto.getMes());  // June = 5 in Calendar, so +1 = 6
        assertEquals(2025, gasto.getAnio());
        assertEquals(timestamp2, gasto.getFecha());
    }

    @Test
    public void testSetFecha_preservesNombreAndPrecio() {
        // Arrange
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2024, Calendar.JANUARY, 15, 12, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long timestamp = cal.getTimeInMillis();

        GastoEntity gasto = new GastoEntity("My Game", 49.99, timestamp);

        // Act
        Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal2.set(2024, Calendar.DECEMBER, 31, 23, 59, 59);
        cal2.set(Calendar.MILLISECOND, 999);
        long newTimestamp = cal2.getTimeInMillis();

        gasto.setFecha(newTimestamp);

        // Assert
        assertEquals("My Game", gasto.getNombreJuego());
        assertEquals(49.99, gasto.getPrecio(), 0.001);
    }

    @Test
    public void testSetFecha_crossYearBoundary() {
        // Arrange - December 31, 2024
        Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal1.set(2024, Calendar.DECEMBER, 31, 23, 59, 59);
        cal1.set(Calendar.MILLISECOND, 999);
        long timestamp1 = cal1.getTimeInMillis();

        GastoEntity gasto = new GastoEntity("Game", 10.0, timestamp1);
        assertEquals(12, gasto.getMes());
        assertEquals(2024, gasto.getAnio());

        // Act - January 1, 2025
        Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal2.set(2025, Calendar.JANUARY, 1, 0, 0, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        long timestamp2 = cal2.getTimeInMillis();

        gasto.setFecha(timestamp2);

        // Assert
        assertEquals(1, gasto.getMes());
        assertEquals(2025, gasto.getAnio());
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2024, Calendar.MARCH, 10, 12, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long timestamp = cal.getTimeInMillis();

        GastoEntity gasto = new GastoEntity("Game Title", 29.99, timestamp);

        // Assert initial values
        assertEquals("Game Title", gasto.getNombreJuego());
        assertEquals(29.99, gasto.getPrecio(), 0.001);
        assertEquals(timestamp, gasto.getFecha());
        assertEquals(3, gasto.getMes());
        assertEquals(2024, gasto.getAnio());

        // Act - modify via setters
        gasto.setNombreJuego("Updated Game");
        gasto.setPrecio(39.99);

        // Assert modified values
        assertEquals("Updated Game", gasto.getNombreJuego());
        assertEquals(39.99, gasto.getPrecio(), 0.001);
    }

    @Test
    public void testSetMesAndSetAnio() {
        // Arrange
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2024, Calendar.JANUARY, 1, 12, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long timestamp = cal.getTimeInMillis();

        GastoEntity gasto = new GastoEntity("Game", 10.0, timestamp);

        // Act
        gasto.setMes(7);
        gasto.setAnio(2026);

        // Assert
        assertEquals(7, gasto.getMes());
        assertEquals(2026, gasto.getAnio());
    }
}

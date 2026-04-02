package com.miapp.agentegamer.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class MoneyUtilsTest {
    @Test
    public void testFormat_defaultPositivo() {
        String resultado = MoneyUtils.format(100.0);
        assertTrue(resultado.contains("€"));
    }

    @Test
    public void testFormat_defaultCero() {
        String resultado = MoneyUtils.format(0.0);
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }

    @Test
    public void testFormat_eurExplicito() {
        String resultado = MoneyUtils.format(250.0, "EUR");
        assertTrue(resultado.contains("€"));
    }

    @Test
    public void testFormat_usd() {
        String resultado = MoneyUtils.format(100.0, "USD");
        assertTrue(resultado.contains("US$") || resultado.contains("$"));
    }

    @Test
    public void testFormat_gbp() {
        String resultado = MoneyUtils.format(100.0, "GBP");
        assertTrue(resultado.contains("£") || resultado.contains("GBP"));
    }

    @Test
    public void testFormat_currencyNull() {
        String resultado = MoneyUtils.format(50.0, null);
        assertTrue(resultado.contains("€"));
    }

    @Test
    public void testFormat_currencyVacio() {
        String resultado = MoneyUtils.format(50.0, "");
        assertTrue(resultado.contains("€"));
    }

    @Test
    public void testFormat_currencyInvalido() {
        String resultado = MoneyUtils.format(50.0, "INVALID");
        assertTrue(resultado.contains("€"));
    }

    @Test
    public void testFormat_negativo() {
        String resultado = MoneyUtils.format(-75.0);
        assertTrue(resultado.contains("-"));
        assertTrue(resultado.contains("€"));
    }
}

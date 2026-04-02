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

    // ================= CONVERSIÓN DE MONEDA =================

    @Test
    public void testConvertirDesdeEUR_aEUR_retornaMontoOriginal() {
        double resultado = MoneyUtils.convertirDesdeEUR(100.0, "EUR");
        assertEquals(100.0, resultado, 0.01);
    }

    @Test
    public void testConvertirDesdeEUR_aUSD_convierteCorrectamente() {
        double resultado = MoneyUtils.convertirDesdeEUR(100.0, "USD");
        // 100 EUR * 1.08 = 108 USD
        assertEquals(108.0, resultado, 0.01);
    }

    @Test
    public void testConvertirDesdeEUR_aGBP_convierteCorrectamente() {
        double resultado = MoneyUtils.convertirDesdeEUR(100.0, "GBP");
        // 100 EUR * 0.86 = 86 GBP
        assertEquals(86.0, resultado, 0.01);
    }

    @Test
    public void testConvertirDesdeEUR_monedaNull_retornaOriginal() {
        double resultado = MoneyUtils.convertirDesdeEUR(50.0, null);
        assertEquals(50.0, resultado, 0.01);
    }

    @Test
    public void testConvertirDesdeEUR_monedaVacia_retornaOriginal() {
        double resultado = MoneyUtils.convertirDesdeEUR(50.0, "");
        assertEquals(50.0, resultado, 0.01);
    }

    @Test
    public void testConvertirDesdeEUR_monedaDesconocida_retornaOriginal() {
        double resultado = MoneyUtils.convertirDesdeEUR(50.0, "JPY");
        assertEquals(50.0, resultado, 0.01);
    }

    @Test
    public void testFormat_convierteYFormatea_USD() {
        // 49.99 EUR * 1.08 = 53.99 USD
        String resultado = MoneyUtils.format(49.99, "USD");
        // Should contain USD amount (53.99...) and $ symbol
        assertTrue("Debe contener símbolo $", resultado.contains("$") || resultado.contains("US$"));
        // Should NOT be 49.99 — must be converted
        assertFalse("No debe mostrar el monto EUR original", resultado.contains("49,99"));
    }

    @Test
    public void testFormat_convierteYFormatea_GBP() {
        // 100 EUR * 0.86 = 86 GBP
        String resultado = MoneyUtils.format(100.0, "GBP");
        assertTrue("Debe contener símbolo £ o GBP", resultado.contains("£") || resultado.contains("GBP"));
        // 86.00 should appear, not 100.00
        assertFalse("No debe mostrar el monto EUR original", resultado.contains("100,00"));
    }

    @Test
    public void testFormat_EUR_noConvierte() {
        // EUR should show original amount without conversion
        String resultado = MoneyUtils.format(49.99, "EUR");
        assertTrue("Debe contener €", resultado.contains("€"));
        assertTrue("Debe mostrar monto original", resultado.contains("49,99"));
    }
}

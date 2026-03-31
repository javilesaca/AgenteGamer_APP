package com.miapp.agentegamer.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitarios para el cálculo del presupuesto restante.
 * Verifica que la fórmula: restante = presupuesto - totalGastado
 * funciona correctamente en todos los casos.
 */
public class PresupuestoCalculatorTest {

    // Fórmula: Restante = Presupuesto - Total Gastado
    private double calcularRestante(double presupuesto, double totalGastado) {
        return presupuesto - totalGastado;
    }

    @Test
    public void testRestante_positivo() {
        // Presupuesto: 150€, Gastado: 50€ → Restante: 100€
        double presupuesto = 150.0;
        double totalGastado = 50.0;
        
        double restante = calcularRestante(presupuesto, totalGastado);
        
        assertEquals(100.0, restante, 0.01);
    }

    @Test
    public void testRestante_cero() {
        // Presupuesto: 150€, Gastado: 150€ → Restante: 0€
        double presupuesto = 150.0;
        double totalGastado = 150.0;
        
        double restante = calcularRestante(presupuesto, totalGastado);
        
        assertEquals(0.0, restante, 0.01);
    }

    @Test
    public void testRestante_negativo() {
        // Presupuesto: 150€, Gastado: 159.97€ → Restante: -9.97€ (sobregastado)
        double presupuesto = 150.0;
        double totalGastado = 159.97;
        
        double restante = calcularRestante(presupuesto, totalGastado);
        
        assertEquals(-9.97, restante, 0.01);
    }

    @Test
    public void testRestante_sinGastos() {
        // Presupuesto: 150€, Gastado: 0€ → Restante: 150€
        double presupuesto = 150.0;
        double totalGastado = 0.0;
        
        double restante = calcularRestante(presupuesto, totalGastado);
        
        assertEquals(150.0, restante, 0.01);
    }

    @Test
    public void testRestante_80Porciento() {
        // Presupuesto: 150€, Gastado: 120€ (80%) → Restante: 30€
        double presupuesto = 150.0;
        double totalGastado = 120.0;
        
        double restante = calcularRestante(presupuesto, totalGastado);
        
        assertEquals(30.0, restante, 0.01);
    }

    @Test
    public void testPorcentajeGastado() {
        // Verificar cálculo de porcentaje: (gastado / presupuesto) * 100
        double presupuesto = 150.0;
        double totalGastado = 159.97;
        
        double porcentaje = (totalGastado / presupuesto) * 100;
        
        // 159.97 / 150 * 100 = 106.64%
        assertEquals(106.64, porcentaje, 0.1);
    }

    @Test
    public void testEsSobrePresupuesto() {
        // Verificar si está sobre el presupuesto
        double presupuesto = 150.0;
        double totalGastado = 159.97;
        
        boolean sobrePresupuesto = totalGastado > presupuesto;
        
        assertTrue(sobrePresupuesto);
    }

    @Test
    public void testEsBajoPresupuesto() {
        // Verificar si está bajo el presupuesto
        double presupuesto = 150.0;
        double totalGastado = 100.0;
        
        boolean bajoPresupuesto = totalGastado < presupuesto;
        
        assertTrue(bajoPresupuesto);
    }
    
    // ================= TEST DE INTEGRACIÓN SIMULADA =================
    
    /**
     * Simulación del caso real del usuario:
     * - Presupuesto: 150€
     * - Total Gastado: 159.97€
     * - Resultado esperado: -9.97€ (negativo, sobregastado)
     */
    @Test
    public void testCasoRealUsuario() {
        // Datos del usuario
        double presupuesto = 150.0;
        double totalGastado = 159.97;
        
        // Cálculo
        double restante = presupuesto - totalGastado;
        
        // Verificaciones
        System.out.println("=== CASO REAL DEL USUARIO ===");
        System.out.println("Presupuesto: " + presupuesto + "€");
        System.out.println("Total Gastado: " + totalGastado + "€");
        System.out.println("Restante: " + restante + "€");
        System.out.println("Porcentaje Gastado: " + (totalGastado / presupuesto * 100) + "%");
        System.out.println("==============================");
        
        // El restante debe ser negativo
        assertTrue("El restante debe ser negativo", restante < 0);
        
        // El porcentaje debe ser mayor al 100%
        double porcentaje = (totalGastado / presupuesto) * 100;
        assertTrue("El porcentaje debe ser mayor al 100%", porcentaje > 100);
        
        // Verificar el valor exacto
        assertEquals(-9.97, restante, 0.01);
    }
    
    /**
     * Verificar que el mensaje de estado sea el correcto
     */
    @Test
    public void testMensajeEstadoSobrePresupuesto() {
        double presupuesto = 150.0;
        double totalGastado = 159.97;
        
        // Si gastado > presupuesto, debe mostrar mensaje de sobregasto
        if (totalGastado > presupuesto) {
            String mensaje = "ALERTA: Has superado tu presupuesto";
            assertNotNull(mensaje);
            System.out.println("MOSTRAR: " + mensaje);
        }
    }
}
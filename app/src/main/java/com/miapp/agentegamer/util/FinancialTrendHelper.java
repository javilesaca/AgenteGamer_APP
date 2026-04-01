package com.miapp.agentegamer.util;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.domain.model.SistemaFinanciero;

/**
 * Helper para cálculos de tendencias financieras.
 * Centraliza la lógica de análisis de gastos y generación de recomendaciones.
 */
public class FinancialTrendHelper {

    /**
     * Dirección de la tendencia financiera.
     */
    public enum TrendDirection {
        UP,     // Gastos aumentaron (mal)
        DOWN,   // Gastos disminuyeron (bueno)
        STABLE  // Sin cambio significativo
    }

    /**
     * Resultado de un cálculo de tendencia.
     */
    public static class TrendResult {
        public final double percentage;
        public final TrendDirection direction;
        public final int colorResId;
        public final String symbol;

        public TrendResult(double percentage, TrendDirection direction) {
            this.percentage = percentage;
            this.direction = direction;
            this.colorResId = direction == TrendDirection.UP ? R.color.estado_rojo :
                             (direction == TrendDirection.DOWN ? R.color.estado_verde : R.color.estado_neutro);
            this.symbol = direction == TrendDirection.UP ? "↑" :
                         (direction == TrendDirection.DOWN ? "↓" : "→");
        }
    }

    /**
     * Calcula la tendencia entre dos períodos.
     * 
     * @param current  Gasto del período actual
     * @param previous Gasto del período anterior
     * @return TrendResult con dirección, porcentaje y color
     */
    public static TrendResult calculateTrend(double current, double previous) {
        if (previous == 0) {
            return new TrendResult(0, TrendDirection.STABLE);
        }

        double change = ((current - previous) / previous) * 100;
        
        // Umbral del 5% para considerar cambio significativo
        TrendDirection direction;
        if (change > 5) {
            direction = TrendDirection.UP;
        } else if (change < -5) {
            direction = TrendDirection.DOWN;
        } else {
            direction = TrendDirection.STABLE;
        }

        return new TrendResult(Math.abs(change), direction);
    }

    /**
     * Genera recomendación contextual según el estado financiero.
     * 
     * @param estado     Estado financiero actual
     * @param presupuesto Presupuesto mensual
     * @param gastado    Total gastado este mes
     * @return String con recomendación
     */
    public static String generateRecommendation(
            SistemaFinanciero.EstadoFinanciero estado,
            double presupuesto,
            double gastado) {

        double restante = Math.max(0, presupuesto - gastado);
        double precioPromedioJuego = 50.0; // Precio promedio estimado
        double juegosPosibles = restante / precioPromedioJuego;

        switch (estado) {
            case VERDE:
                if (juegosPosibles >= 2) {
                    return String.format("✅ ¡Buena racha! Puedes permitirte ~%.0f juegos este mes.", 
                            Math.floor(juegosPosibles));
                } else if (juegosPosibles >= 1) {
                    return "✅ Tienes margen para 1 juego cómodamente.";
                } else {
                    return "✅ Presupuesto bajo control, aunque justo.";
                }
                
            case AMARILLO:
                if (juegosPosibles >= 1) {
                    return "⚠️ Cuidado: tienes para ~1 juego. Considera esperar ofertas.";
                } else {
                    return "⚠️ Atención: casi sin margen. Mejor esperar ofertas.";
                }
                
            case ROJO:
                double porcentajeUsado = (gastado / presupuesto) * 100;
                if (porcentajeUsado >= 100) {
                    return "❌ Presupuesto agotado. Espera al próximo mes.";
                } else {
                    return "❌ Zona de peligro. Posponer compras no urgentes.";
                }
                
            default:
                return "📊 Revisa tu estado financiero.";
        }
    }

    /**
     * Obtiene el label corto de un mes.
     * 
     * @param month Número de mes (0-11)
     * @return Label corto ("Ene", "Feb", etc.)
     */
    public static String getMonthLabel(int month) {
        String[] months = {"Ene", "Feb", "Mar", "Abr", "May", "Jun",
                          "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
        return months[month % 12];
    }

    /**
     * Genera texto descriptivo para la tendencia.
     * 
     * @param result Resultado de la tendencia
     * @return Texto descriptivo
     */
    public static String getTrendDescription(TrendResult result) {
        if (result.percentage < 1) {
            return "Sin cambios significativos";
        }
        
        String pct = String.format("%.1f%%", result.percentage);
        
        switch (result.direction) {
            case UP:
                return "Gastos subieron " + pct + " vs mes anterior";
            case DOWN:
                return "Gastos bajaron " + pct + " vs mes anterior";
            default:
                return "Estable vs mes anterior";
        }
    }
}

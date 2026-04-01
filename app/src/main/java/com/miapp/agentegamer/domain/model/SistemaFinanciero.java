package com.miapp.agentegamer.domain.model;

import java.util.List;

public class SistemaFinanciero {

    private static final double PORCENTAJE_UMBRAL_VERDE = 0.5;
    private static final double PORCENTAJE_UMBRAL_AMARILLO = 0.8;
    private static final double PORCENTAJE_EVALUAR_RECOMENDADO = 0.3;
    private static final double PORCENTAJE_EVALUAR_AJUSTADO = 0.6;
    private static final double PRECIO_ALTO = 69.99;
    private static final double PRECIO_MEDIO_ALTO = 59.99;
    private static final double PRECIO_MEDIO = 49.99;
    private static final double PRECIO_BAJO = 39.99;
    private static final double RATING_ALTO = 4.5;
    private static final double RATING_MEDIO_ALTO = 4.0;
    private static final double RATING_MEDIO = 3.5;

    private final double presupuestoMensual;

    public SistemaFinanciero(double presupuestoMensual) {
        if (presupuestoMensual <= 0) {
            throw new IllegalArgumentException("El presupuesto mensual debe ser mayor que 0");
        }
        this.presupuestoMensual = presupuestoMensual;
    }

    public double calcularTotalGastos(List<Gasto> gastos) {
        if (gastos == null) return 0;
        double total = 0;
        for (Gasto gasto : gastos) {
            if (gasto != null) {
                total += gasto.getPrecio();
            }
        }
        return total;
    }

    public double calcularPresupuestoRestante(List<Gasto> gastos) {
        return presupuestoMensual - calcularTotalGastos(gastos);
    }

    public double calcularPorcentajeGastado(List<Gasto> gastos) {
        if (presupuestoMensual <= 0) return 0;
        return (calcularTotalGastos(gastos) / presupuestoMensual * 100);
    }

    public String generarRecomendacion(List<Gasto> gastos) {
        double total = calcularTotalGastos(gastos);
        EstadoFinanciero estado = obtenerEstado(total);

        // Caso especial: presupuesto completamente consumido o excedido
        if (total >= presupuestoMensual) {
            if (total > presupuestoMensual) {
                return "⚠️ ALERTA: Has superado tu presupuesto mensual. Estás en números rojos.";
            } else {
                return "⚠️ Has consumido el 100% de tu presupuesto. Mejor espera al próximo mes.";
            }
        }

        return switch (estado) {
            case VERDE ->
                    "✅ Tenemos un buen control de gastos. Podemos permitirnos nuevas compras.";
            case AMARILLO ->
                    "⚠️ Atención: estamos consumiendo gran parte del presupuesto.";
            case ROJO ->
                    "❌ Alerta: hemos superado el 80% del presupuesto de este mes.";
        };
    }

    public boolean puedeComprar(double gastoActual, double precioJuego) {
        return (gastoActual + precioJuego) <= presupuestoMensual;
    }

    public enum EstadoFinanciero {
        VERDE, AMARILLO, ROJO
    }

    public EstadoFinanciero obtenerEstado(double totalGastado) {
        if (totalGastado < presupuestoMensual * PORCENTAJE_UMBRAL_VERDE) {
            return EstadoFinanciero.VERDE;
        } else if (totalGastado < presupuestoMensual * PORCENTAJE_UMBRAL_AMARILLO) {
            return EstadoFinanciero.AMARILLO;
        } else {
            return EstadoFinanciero.ROJO;
        }
    }

    public double estimarPrecio(GameInfo juego) {
        if (juego == null || juego.getRating() <= 0) {
            return PRECIO_MEDIO;
        }

        if (juego.getRating() >= RATING_ALTO) {
            return PRECIO_ALTO;
        } else if (juego.getRating() >= RATING_MEDIO_ALTO) {
            return PRECIO_MEDIO_ALTO;
        } else if (juego.getRating() >= RATING_MEDIO) {
            return PRECIO_MEDIO;
        } else {
            return PRECIO_BAJO;
        }
    }

    public String evaluarCompra(double precioJuego, double gastoMes) {
        double restante = presupuestoMensual - gastoMes;

        if (restante < 0) return "❌ No recomendable";

        if (precioJuego <= restante * PORCENTAJE_EVALUAR_RECOMENDADO) {
            return "✅ Compra recomendada";
        } else if (precioJuego <= restante * PORCENTAJE_EVALUAR_AJUSTADO) {
            return "⚠️ Compra ajustada";
        } else if (precioJuego <= restante) {
            return "⌛ Mejor esperar";
        } else {
            return "❌ No recomendable";
        }
    }

    public double getPresupuestoMensual() {
        return presupuestoMensual;
    }
}

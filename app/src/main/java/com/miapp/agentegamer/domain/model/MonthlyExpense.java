package com.miapp.agentegamer.domain.model;

import com.miapp.agentegamer.util.FinancialTrendHelper;

/**
 * Modelo de dominio para gastos mensuales agrupados.
 * Usado para el gráfico de tendencias del Dashboard.
 */
public class MonthlyExpense {

    private final String mes; // Formato "YYYY-MM" o label "Ene 2024"
    private final double total;
    private final SistemaFinanciero.EstadoFinanciero estado;
    private final int monthNumber; // 0-11 para label
    private final int year;

    /**
     * Constructor para datos desde query de base de datos.
     * 
     * @param mes       Label del mes (ej: "Ene", "Feb")
     * @param total     Total gastado en ese mes
     * @param presupuesto Presupuesto mensual para calcular estado
     */
    public MonthlyExpense(String mes, double total, double presupuesto) {
        this.mes = mes;
        this.total = total;
        this.estado = calcularEstado(total, presupuesto);
        this.monthNumber = -1;
        this.year = -1;
    }

    /**
     * Constructor completo con información temporal.
     * 
     * @param monthNumber Número de mes (0-11)
     * @param year        Año
     * @param total       Total gastado
     * @param presupuesto Presupuesto para cálculo de estado
     */
    public MonthlyExpense(int monthNumber, int year, double total, double presupuesto) {
        this.monthNumber = monthNumber;
        this.year = year;
        this.mes = FinancialTrendHelper.getMonthLabel(monthNumber);
        this.total = total;
        this.estado = calcularEstado(total, presupuesto);
    }

    /**
     * Calcula el estado financiero según el porcentaje gastado.
     */
    private SistemaFinanciero.EstadoFinanciero calcularEstado(double total, double presupuesto) {
        if (presupuesto <= 0) {
            return SistemaFinanciero.EstadoFinanciero.AMARILLO;
        }

        double porcentaje = (total / presupuesto) * 100;

        if (porcentaje < 50) {
            return SistemaFinanciero.EstadoFinanciero.VERDE;
        } else if (porcentaje < 80) {
            return SistemaFinanciero.EstadoFinanciero.AMARILLO;
        } else {
            return SistemaFinanciero.EstadoFinanciero.ROJO;
        }
    }

    // Getters

    public String getMes() {
        return mes;
    }

    public double getTotal() {
        return total;
    }

    public SistemaFinanciero.EstadoFinanciero getEstado() {
        return estado;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public int getYear() {
        return year;
    }

    /**
     * Obtiene el color del estado para UI.
     */
    public int getEstadoColor(boolean useResources) {
        // Retorna el recurso de color correspondiente
        // Los IDs se definen en R.color
        return switch (estado) {
            case VERDE -> 0xFF3CFB3;    // accent_green
            case AMARILLO -> 0xFFFF9F3C; // accent_orange  
            case ROJO -> 0xFFFF4D4D;     // accent_red
        };
    }

    /**
     * Formatea el total como moneda.
     */
    public String getTotalFormatted() {
        return String.format("%.2f €", total);
    }

    @Override
    public String toString() {
        return String.format("MonthlyExpense{mes='%s', total=%.2f, estado=%s}", 
                mes, total, estado);
    }
}

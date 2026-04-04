package com.miapp.agentegamer.domain.model;

import com.miapp.agentegamer.util.FinancialTrendHelper;

/**
 * Modelo de dominio que representa un gasto mensual agrupado para visualización
 * en gráficos de tendencias del Dashboard.
 * 
 * <p>Esta clase encapsula la información de un mes específico de gasto, incluyendo
 * el total gastado, el estado financiero correspondiente y metadatos temporales
 * para su correcta presentación en la interfaz de usuario.</p>
 * 
 * <p>El estado financiero se calcula automáticamente según el porcentaje del
 * presupuesto consumido:</p>
 * <ul>
 *   <li><b>VERDE:</b> Porcentaje &lt; 50%</li>
 *   <li><b>AMARILLO:</b> 50% ≤ Porcentaje &lt; 80%</li>
 *   <li><b>ROJO:</b> Porcentaje ≥ 80%</li>
 * </ul>
 * 
 * @author AgenteGamer
 * @version 1.0
 * @since 2024
 * @see SistemaFinanciero
 * @see FinancialTrendHelper
 */
public class MonthlyExpense {

    /** Etiqueta del mes (formato "YYYY-MM" o nombre localized como "Ene 2024"). */
    private final String mes;
    
    /** Total acumulado de gastos en el mes especificado en euros. */
    private final double total;
    
    /** Estado financiero del mes según el porcentaje de presupuesto consumido. */
    private final SistemaFinanciero.EstadoFinanciero estado;
    
    /** Número del mes en el año (0-11). Valor -1 si no se proporcionó. */
    private final int monthNumber;
    
    /** Año del mes registrado. Valor -1 si no se proporcionó. */
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
     * Calcula el estado financiero según el porcentaje gastado con respecto al presupuesto.
     * 
     * <p>Utiliza los umbrales del sistema financiero:</p>
     * <ul>
     *   <li>VERDE: porcentaje &lt; 50%</li>
     *   <li>AMARILLO: 50% ≤ porcentaje &lt; 80%</li>
     *   <li>ROJO: porcentaje ≥ 80%</li>
     * </ul>
     * 
     * @param total      Total gastado en el mes
     * @param presupuesto Presupuesto mensual de referencia
     * @return EstadoFinanciero correspondiente
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

    // Getters con documentación Javadoc

    /**
     * Obtiene la etiqueta del mes.
     * 
     * @return Nombre del mes (ej: "Ene", "Feb" o formato "YYYY-MM")
     */
    public String getMes() {
        return mes;
    }

    /**
     * Obtiene el total gastado en el mes.
     * 
     * @return Total en euros
     */
    public double getTotal() {
        return total;
    }

    /**
     * Obtiene el estado financiero del mes.
     * 
     * @return EstadoFinanciero (VERDE, AMARILLO o ROJO)
     */
    public SistemaFinanciero.EstadoFinanciero getEstado() {
        return estado;
    }

    /**
     * Obtiene el número del mes (0-11).
     * 
     * @return Número de mes o -1 si no fue especificado
     */
    public int getMonthNumber() {
        return monthNumber;
    }

    /**
     * Obtiene el año del registro.
     * 
     * @return Año o -1 si no fue especificado
     */
    public int getYear() {
        return year;
    }

    /**
     * Obtiene el color hexadecimal correspondiente al estado financiero para la UI.
     * 
     * <p>Los colores devueltos son:</p>
     * <ul>
     *   <li>VERDE: 0xFF3CFB3 (verde éxito)</li>
     *   <li>AMARILLO: 0xFFFF9F3C (naranja advertencia)</li>
     *   <li>ROJO: 0xFFFF4D4D (rojo alerta)</li>
     * </ul>
     * 
     * @param useResources Indica si debe usar recursos del sistema (no utilizado, reservado para futuras versiones)
     * @return Entero representando el color en formato ARGB
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
     * Formatea el total gastado como cadena de moneda en euros.
     * 
     * @return Cadena con formato "XX.XX €"
     */
    public String getTotalFormatted() {
        return String.format("%.2f €", total);
    }

    /**
     * Representación en cadena del objeto para depuración.
     * 
     * @return Cadena con formato "MonthlyExpense{mes='XXX', total=XX.XX, estado=XXX}"
     */
    @Override
    public String toString() {
        return String.format("MonthlyExpense{mes='%s', total=%.2f, estado=%s}", 
                mes, total, estado);
    }
}

package com.miapp.agentegamer.domain.model;

import java.util.List;

/**
 * Clase central del sistema de gestión financiera para videojuegos.
 * 
 * <p>Esta clase encapsula toda la lógica de negocio relacionada con el control
 * del presupuesto de compras de juegos. Proporciona métodos para calcular gastos,
 * determinar estados financieros, estimar precios y evaluar la viabilidad de
 * compras según el presupuesto disponible.</p>
 * 
 * <p>El sistema utiliza un presupuesto mensual configurable que permite al usuario
 * mantener un registro de sus gastos y recibir recomendaciones personalizadas.</p>
 * 
 * <h2>Fórmulas de cálculo principales:</h2>
 * <ul>
 *   <li><b>Total de gastos:</b> Σ(precio de cada gasto)</li>
 *   <li><b>Presupuesto restante:</b> presupuestoMensual - totalGastos</li>
 *   <li><b>Porcentaje gastado:</b> (totalGastos / presupuestoMensual) × 100</li>
 *   <li><b>Estado financiero:</b> Determinado por umbrales (50% verde, 80% amarillo, &gt;80% rojo)</li>
 * </ul>
 * 
 * <h2>Rangos de precio estimados por rating:</h2>
 * <ul>
 *   <li>Rating ≥ 4.5: Precio alto (69.99€)</li>
 *   <li>Rating ≥ 4.0: Precio medio-alto (59.99€)</li>
 *   <li>Rating ≥ 3.5: Precio medio (49.99€)</li>
 *   <li>Rating &lt; 3.5: Precio bajo (39.99€)</li>
 * </ul>
 * 
 * @author AgenteGamer
 * @version 1.0
 * @since 2024
 * @see Gasto
 * @see GameInfo
 * @see EstadoFinanciero
 */
public class SistemaFinanciero {

    /** Porcentaje del presupuesto que define el umbral verde (estado saludable). Equivalente al 50%. */
    private static final double PORCENTAJE_UMBRAL_VERDE = 0.5;
    
    /** Porcentaje del presupuesto que define el umbral amarillo (advertencia). Equivalente al 80%. */
    private static final double PORCENTAJE_UMBRAL_AMARILLO = 0.8;
    
    /** Porcentaje del presupuesto restante que se considera "compra recomendada". Equivalente al 30%. */
    private static final double PORCENTAJE_EVALUAR_RECOMENDADO = 0.3;
    
    /** Porcentaje del presupuesto restante que se considera "compra ajustada". Equivalente al 60%. */
    private static final double PORCENTAJE_EVALUAR_AJUSTADO = 0.6;
    
    /** Precio alto para juegos con rating tinggi (≥ 4.5). */
    private static final double PRECIO_ALTO = 69.99;
    
    /** Precio medio-alto para juegos con rating entre 4.0 y 4.49. */
    private static final double PRECIO_MEDIO_ALTO = 59.99;
    
    /** Precio medio para juegos con rating entre 3.5 y 3.99. */
    private static final double PRECIO_MEDIO = 49.99;
    
    /** Precio bajo para juegos con rating inferior a 3.5. */
    private static final double PRECIO_BAJO = 39.99;
    
    /** Rating alto que justifica precio máximo. */
    private static final double RATING_ALTO = 4.5;
    
    /** Rating medio-alto que justifica precio medio-alto. */
    private static final double RATING_MEDIO_ALTO = 4.0;
    
    /** Rating medio que justifica precio medio. */
    private static final double RATING_MEDIO = 3.5;

    /** Presupuesto mensual establecido por el usuario en euros. */
    private final double presupuestoMensual;

    /**
     * Constructor que inicializa el sistema financiero con un presupuesto mensual.
     * 
     * <p>El presupuesto debe ser un valor positivo mayor a cero. Si el valor
     * proporcionado es menor o igual a cero, se lanza una excepción.</p>
     * 
     * @param presupuestoMensual Cantidad máxima de dinero disponible para gastar en un mes (en euros)
     * @throws IllegalArgumentException Si el presupuesto es menor o igual a cero
     */
    public SistemaFinanciero(double presupuestoMensual) {
        if (presupuestoMensual <= 0) {
            throw new IllegalArgumentException("El presupuesto mensual debe ser mayor que 0");
        }
        this.presupuestoMensual = presupuestoMensual;
    }

    /**
     * Calcula el total de todos los gastos en la lista proporcionada.
     * 
     * <p>Itera sobre la lista de gastos y suma los precios de aquellos que no
     * sean null. Si la lista es null, retorna 0.</p>
     * 
     * <p>Fórmula: total = Σ(gasto.getPrecio()) para todo gasto ≠ null</p>
     * 
     * @param gastos Lista de objetos Gasto a sumar. Puede ser null
     * @return Suma total de los precios de todos los gastos válidos
     */
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

    /**
     * Calcula el presupuesto restante después de aplicar los gastos del mes.
     * 
     * <p>Resta el total de gastos del presupuesto mensual establecido.</p>
     * 
     * <p>Fórmula: restante = presupuestoMensual - totalGastos</p>
     * 
     * @param gastos Lista de gastos del período a calcular
     * @return Presupuesto restante en euros (puede ser negativo si se excedió)
     * @see #calcularTotalGastos(List)
     */
    public double calcularPresupuestoRestante(List<Gasto> gastos) {
        return presupuestoMensual - calcularTotalGastos(gastos);
    }

    /**
     * Calcula el porcentaje del presupuesto que ha sido gastado.
     * 
     * <p>Expresa como porcentaje la proporción del presupuesto que ya ha sido
     * consumida por los gastos del período.</p>
     * 
     * <p>Fórmula: porcentaje = (totalGastos / presupuestoMensual) × 100</p>
     * 
     * @param gastos Lista de gastos del período a evaluar
     * @return Porcentaje del presupuesto gastado (0 si el presupuesto es ≤ 0)
     * @see #calcularTotalGastos(List)
     */
    public double calcularPorcentajeGastado(List<Gasto> gastos) {
        if (presupuestoMensual <= 0) return 0;
        return (calcularTotalGastos(gastos) / presupuestoMensual * 100);
    }

    /**
     * Genera una recomendación textual basada en el estado financiero actual.
     * 
     * <p>Analiza el total gastado y devuelve un mensaje personalizado que indica
     * al usuario si puede realizar nuevas compras o si debe esperar.</p>
     * 
     * <p>Los posibles estados de recomendación son:</p>
     * <ul>
     *   <li><b>Verde:</b> Buen control de gastos - se pueden hacer compras</li>
     *   <li><b>Amarillo:</b> Consumo elevado - se requiere atención</li>
     *   <li><b>Rojo:</b> Presupuesto comprometido (&gt;80%)</li>
     *   <li><b>Excedido:</b> Se superó el presupuesto del mes</li>
     * </ul>
     * 
     * @param gastos Lista de gastos del período actual
     * @return Mensaje de recomendación con emoji según el estado
     * @see #obtenerEstado(double)
     * @see #calcularTotalGastos(List)
     */
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

    /**
     * Determina si el usuario puede realizar una compra adicional sin exceder el presupuesto.
     * 
     * <p>Compara la suma del gasto actual más el precio del nuevo juego con el
     * presupuesto mensual establecido.</p>
     * 
     * <p>Fórmula: puedeComprar = (gastoActual + precioJuego) ≤ presupuestoMensual</p>
     * 
     * @param gastoActual   Total gastado hasta el momento en el período
     * @param precioJuego   Precio del juego que se desea comprar
     * @return true si la compra no excede el presupuesto, false en caso contrario
     */
    public boolean puedeComprar(double gastoActual, double precioJuego) {
        return (gastoActual + precioJuego) <= presupuestoMensual;
    }

    /**
     * Enumera los posibles estados financieros del usuario.
     * 
     * <p>Los estados representan el nivel de consumo del presupuesto mensual:</p>
     * <ul>
     *   <li><b>VERDE:</b> Gasto inferior al 50% del presupuesto - situación saludable</li>
     *   <li><b>AMARILLO:</b> Gasto entre 50% y 80% del presupuesto - precaución</li>
     *   <li><b>ROJO:</b> Gasto superior al 80% del presupuesto - alerta</li>
     * </ul>
     * 
     * @see #obtenerEstado(double)
     */
    public enum EstadoFinanciero {
        VERDE, AMARILLO, ROJO
    }

    /**
     * Determina el estado financiero basado en el total gastado.
     * 
     * <p>Compara el total gastado con los umbrales definidos para clasificar
     * el estado financiero del usuario.</p>
     * 
     * <p>Umbrales utilizados:</p>
     * <ul>
     *   <li>VERDE: totalGastado &lt; presupuesto × 0.5 (50%)</li>
     *   <li>AMARILLO: presupuesto × 0.5 ≤ totalGastado &lt; presupuesto × 0.8 (80%)</li>
     *   <li>ROJO: totalGastado ≥ presupuesto × 0.8 (80%)</li>
     * </ul>
     * 
     * @param totalGastado Cantidad total gastada en el período
     * @return EstadoFinanciero correspondiente según los umbrales
     * @see EstadoFinanciero
     */
    public EstadoFinanciero obtenerEstado(double totalGastado) {
        if (totalGastado < presupuestoMensual * PORCENTAJE_UMBRAL_VERDE) {
            return EstadoFinanciero.VERDE;
        } else if (totalGastado < presupuestoMensual * PORCENTAJE_UMBRAL_AMARILLO) {
            return EstadoFinanciero.AMARILLO;
        } else {
            return EstadoFinanciero.ROJO;
        }
    }

    /**
     * Estima el precio de un juego basándose en su calificación (rating).
     * 
     * <p>Utiliza el rating del juego para asignar un rango de precio estimado
     * según las políticas de pricing definidas en el sistema.</p>
     * 
     * <p>Rangos de precio por rating:</p>
     * <ul>
     *   <li>Rating ≥ 4.5 → 69.99€ (precio alto)</li>
     *   <li>Rating ≥ 4.0 → 59.99€ (precio medio-alto)</li>
     *   <li>Rating ≥ 3.5 → 49.99€ (precio medio)</li>
     *   <li>Rating &lt; 3.5 → 39.99€ (precio bajo)</li>
     * </ul>
     * 
     * <p>Si el juego es null o tiene rating inválido (≤ 0), retorna el precio medio.</p>
     * 
     * @param juego Objeto GameInfo con la información del juego a estimar
     * @return Precio estimado en euros según el rating del juego
     * @see GameInfo
     */
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

    /**
     * Evalúa si una compra es recomendable según el presupuesto disponible.
     * 
     * <p>Analiza el precio del juego en relación con el dinero restante del
     * presupuesto y devuelve una valoración textual de la compra.</p>
     * 
     * <p>Criterios de evaluación:</p>
     * <ul>
     *   <li><b>✅ Compra recomendada:</b> precio ≤ restante × 0.3</li>
     *   <li><b>⚠️ Compra ajustada:</b> restante × 0.3 &lt; precio ≤ restante × 0.6</li>
     *   <li><b>⌛ Mejor esperar:</b> restante × 0.6 &lt; precio ≤ restante</li>
     *   <li><b>❌ No recomendable:</b> precio &gt; restante o presupuesto ya excedido</li>
     * </ul>
     * 
     * @param precioJuego Precio del juego a evaluar en euros
     * @param gastoMes     Total gastado en el mes hasta el momento
     * @return Cadena con la evaluación de la compra (incluye emoji indicador)
     * @see #calcularPresupuestoRestante(List)
     */
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

    /**
     * Obtiene el presupuesto mensual configurado.
     * 
     * @return Presupuesto mensual en euros
     */
    public double getPresupuestoMensual() {
        return presupuestoMensual;
    }
}

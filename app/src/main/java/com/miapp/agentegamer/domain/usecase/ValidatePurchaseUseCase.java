package com.miapp.agentegamer.domain.usecase;

import com.miapp.agentegamer.data.local.entity.WishlistEntity;
import com.miapp.agentegamer.domain.model.SistemaFinanciero;

import javax.inject.Inject;

/**
 * Caso de uso para validar si una compra es recomendable según el presupuesto.
 * 
 * <p>Encapsula la lógica de negocio para evaluar la viabilidad de comprar
 * un juego de la wishlist basándose en el presupuesto disponible del usuario
 * y el gasto acumulado en el mes actual.</p>
 * 
 * <p>Este caso de uso utiliza {@link SistemaFinanciero} para realizar la
 * evaluación, la cual retorna una cadena con la valoración de la compra
 * indicando si es:</p>
 * <ul>
 *   <li><b>✅ Compra recomendada:</b> El precio representa menos del 30% del presupuesto restante</li>
 *   <li><b>⚠️ Compra ajustada:</b> El precio está entre el 30% y 60% del presupuesto restante</li>
 *   <li><b>⌛ Mejor esperar:</b> El precio supera el 60% del presupuesto restante</li>
 *   <li><b>❌ No recomendable:</b> El precio supera el presupuesto restante</li>
 * </ul>
 * 
 * <p>Este caso de uso forma parte de la capa de dominio de Clean Architecture
 * y no mantiene estado, siendo seguro instanciarlo múltiples veces.</p>
 * 
 * @author AgenteGamer
 * @version 1.0
 * @since 2024
 * @see SistemaFinanciero
 * @see WishlistEntity
 */
public class ValidatePurchaseUseCase {

    /**
     * Constructor sin dependencias necesarias (usa inyección de constructores vacía).
     * 
     * <p>La clase no requiere dependencias ya que crea internamente la instancia
     * de {@link SistemaFinanciero} necesaria para realizar la evaluación.</p>
     */
    @Inject
    public ValidatePurchaseUseCase() {
    }

    /**
     * Evalúa si la compra de un juego de la wishlist es recomendable.
     * 
     * <p>Utiliza el sistema financiero con el presupuesto proporcionado para
     * determinar si la compra del juego especificado es viable.</p>
     * 
     * <p>La evaluación considera:</p>
     * <ul>
     *   <li>El precio estimado del juego ({@link WishlistEntity#getPrecioEstimado()})</li>
     *   <li>El gasto acumulado en el mes actual</li>
     *   <li>El presupuesto mensual disponible</li>
     * </ul>
     * 
     * @param juego         Entidad del juego de la wishlist a evaluar
     * @param presupuesto   Presupuesto mensual disponible del usuario
     * @param gastoActual   Total gastado en el mes hasta el momento
     * @return Cadena con la evaluación de la compra (incluye emoji indicador)
     * @see SistemaFinanciero#evaluarCompra(double, double)
     */
    public String validate(WishlistEntity juego, double presupuesto, double gastoActual) {
        SistemaFinanciero sistema = new SistemaFinanciero(presupuesto);
        return sistema.evaluarCompra(juego.getPrecioEstimado(), gastoActual);
    }
}

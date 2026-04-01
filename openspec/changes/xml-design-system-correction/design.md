# Design: Validación y Corrección de Layouts XML contra Fintech Dark Theme Design System

## Technical Approach

Implementar corrección manual de atributos XML en layouts existentes para alinearlos con el design system fintech dark theme. El enfoque es directo: modificar los atributos específicos que violan el design system, priorizando consistencia visual absoluta. No se requiere refactorización estructural ni nuevos componentes.

## Architecture Decisions

### Decision: Orden de implementación basado en severidad

**Choice**: Comenzar con el issue CRITICAL (tipografía DEBUG) seguido de warnings de cardCornerRadius, luego cardElevation.
**Alternatives considered**: Agrupar por archivo o por tipo de atributo.
**Rationale**: La severidad define la prioridad: CRITICAL primero para corregir violación de accesibilidad (tipografía monospace afecta legibilidad). Los warnings de consistencia visual pueden agruparse por tipo de atributo para eficiencia.

### Decision: Corrección in-place vs extracción a estilos

**Choice**: Corrección in-place de atributos individuales.
**Alternatives considered**: Crear estilos específicos para cada tipo de card (ej: `@style/CardDebug`, `@style/ItemCard`).
**Rationale**: El número de archivos es pequeño (5), y las variaciones son mínimas (solo 3 atributos diferentes). Extraer a estilos agregaría complejidad innecesaria. Además, el proyecto ya tiene un estilo base `Widget.AgenteGamer.Card` que podría usarse, pero requiere modificación del elevation a 0dp (actualmente 2dp).

### Decision: Mantener elevation="0dp" explícito vs usar estilo base

**Choice**: Mantener `app:cardElevation="0dp"` explícito en cada card.
**Alternatives considered**: Modificar el estilo base `Widget.AgenteGamer.Card` para que tenga elevation 0dp.
**Rationale**: El estilo base se usa como fallback, pero el design system actual requiere elevation 0dp explícito para flat design. Cambiar el estilo base podría afectar otras cards no consideradas. Es más seguro mantener explicito y evaluar posteriormente si consolidar.

## Data Flow

No aplica - corrección estática de layouts XML.

```
XML Layout ──→ Validación manual ──→ Corrección de atributos ──→ Layout corregido
     │                                    │
     └── Design System Rules ─────────────┘
```

## File Changes

| File | Action | Description |
|------|--------|-------------|
| `app/src/main/res/layout/activity_main.xml` | Modify | 1. Cambiar `android:fontFamily="monospace"` a `android:fontFamily="@font/manrope_regular"` en tvDebug (línea 49). 2. Cambiar `app:cardCornerRadius="24dp"` a `app:cardCornerRadius="20dp"` en cardTotal (línea 64). |
| `app/src/main/res/layout/dialog_detalle_juego.xml` | Modify | Agregar `app:cardElevation="0dp"` en MaterialCardView (después de línea 8). |
| `app/src/main/res/layout/item_gasto.xml` | Modify | Cambiar `app:cardCornerRadius="16dp"` a `app:cardCornerRadius="20dp"` (línea 13). |
| `app/src/main/res/layout/item_juego.xml` | Modify | Cambiar `app:cardCornerRadius="16dp"` a `app:cardCornerRadius="20dp"` (línea 13). |
| `app/src/main/res/values/themes.xml` | Modify (opcional) | Considerar cambiar `cardElevation` de `2dp` a `0dp` en `Widget.AgenteGamer.Card` para consistencia global. |

## Interfaces / Contracts

No se requieren nuevas interfaces. Las modificaciones son atributos XML existentes.

**Atributos a validar en futuras correcciones:**
- `cardCornerRadius`: Siempre `"20dp"`
- `cardElevation`: Siempre `"0dp"`
- `fontFamily`: Usar `@font/manrope_*` (regular, medium, bold)
- `cardBackgroundColor`: `@color/surface`
- `strokeColor`: `@color/surface_variant`

## Testing Strategy

| Layer | What to Test | Approach |
|-------|-------------|----------|
| Visual | Verificar que los layouts renderizan correctamente sin cambios de diseño | Ejecutar la app en dispositivo/emulador y comparar screenshots antes/después |
| Layout | Verificar que no hay errores de atributos XML | Compilar el proyecto y revisar logs de lint |
| Accesibility | Verificar que el texto DEBUG usa tipografía correcta | Inspeccionar visualmente el TextView DEBUG |

## Migration / Rollout

No se requiere migración de datos. Los cambios son puramente visuales y no afectan la lógica de negocio o datos.

**Plan de rollout:**
1. Aplicar correcciones en orden CRITICAL → warnings.
2. Compilar y probar en emulador.
3. Verificar que no hay regressions visuales.
4. Commit con mensaje descriptivo.

## Open Questions

- [ ] ¿Deberíamos modificar el estilo base `Widget.AgenteGamer.Card` para que tenga `cardElevation="0dp"` globalmente? Actualmente tiene `2dp` pero todos los layouts lo sobreescriben explícitamente.
- [ ] ¿Hay otros layouts no considerados que usen `cardCornerRadius` diferente a 20dp?
- [ ] ¿El DEBUG TextView debe mantenerse visible en producción o solo en debug builds?
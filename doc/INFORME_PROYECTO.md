---
title: "Informe del Proyecto: Agente Gamer"
author: "Javier Lesaca Medina"
date: "Curso 2025/26"
lang: es
documentclass: report
toc: true
toc-depth: 3
geometry: margin=2.5cm
fontsize: 11pt
titlepage: true
titlepage-color: "1D689D"
titlepage-text-color: "FFFFFF"
titlepage-rule-color: "FFFFFF"
titlepage-rule-height: 2
titlepage-logo: "/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/logo_safa_blanco.png"
logo-width: 250pt
header-includes:
  - \usepackage{xurl}
  - \usepackage{xcolor}
  - \usepackage{sectsty}
  - \definecolor{headingblue}{HTML}{1D689D}
  - \allsectionsfont{\color{headingblue}}
---

\newpage

<div align="center">

Quiero expresar mi más sincero agradecimiento a todas las personas que me acompañaron en este camino:

- **Mentores y docentes** que compartieron su conocimiento y paciencia.
- **Compañeros y amigos** que probaron la aplicación, detectaron bugs y ofrecieron ideas.
- **Familia** por su apoyo incondicional y por creer en nuestros proyectos.

Este trabajo es el resultado de su guía, su tiempo y su entusiasmo.
¡Gracias por hacer posible este proyecto!

\vspace{1cm}
\begin{center}
\includegraphics[width=0.45\textwidth,keepaspectratio]{docs/img/splash.png}
\end{center}
\vspace{1cm}

> *“La mejor manera de predecir el futuro es implementarlo.”*
> — **David Heinemeier Hansson**, creador de Ruby on Rails

</div>

\newpage

## 1.- Descripción

En la actualidad, los videojuegos se han consolidado como una de las formas de entretenimiento más populares a nivel mundial. La industria del gaming genera miles de millones de euros anuales, con un crecimiento constante año tras año. Sin embargo, uno de los problemas que enfrentan muchos jugadores, especialmente los más activos, es la gestión adecuada de su presupuesto destinado a la adquisición de juegos, suscripciones, contenidos descargables y otros elementos relacionados con el mundo gamer.

**AgenteGamer** es una aplicación Android diseñada específicamente para ayudar a los jugadores a gestionar de manera inteligente su presupuesto mensual dedicado a los videojuegos. El nombre de la aplicación surge del concepto de un «agente» financiero personal que asesora al usuario sobre cómo optimizar sus gastos en gaming.

El problema que resuelve AgenteGamer es doble: por un lado, muchos jugadores no tienen visibilidad clara de cuánto dinero gastan mensualmente en videojuegos; por otro, carecen de herramientas que les permitan tomar decisiones informadas sobre qué juegos comprar considerando su situación financiera personal.

La aplicación está dirigida principalmente a:

- **Jugadores habituales** de entre 16 y 45 años que gastan regularmente en videojuegos y buscan controlar su presupuesto.
- **Estudiantes y jóvenes adultos** que desean administrar de forma responsable su presupuesto limitado asignado al gaming.
- **Coleccionistas y entusiastas** que compran numerosos juegos y necesitan herramientas para priorizar compras.
- **Familias** que establecen presupuestos de entretenimiento para sus hijos y desean supervisar el gasto en videojuegos.

El **tagline** de la aplicación es «Juega más, gasta mejor», reflejando la filosofía de maximizar el entretenimiento gamer sin comprometer la estabilidad financiera personal.

La propuesta de valor principal de AgenteGamer radica en combinar tres funcionalidades que no se encuentran de forma integrada en otras aplicaciones del mercado: gestión de gastos en videojuegos, catálogo de juegos con información actualizada, y un sistema de recomendaciones personalizado que evalúa la conveniencia de compras basándose en el presupuesto y preferencias del usuario.

\newpage

## 2.- Análisis del Mercado Actual

### YNAB (You Need A Budget)

\begin{center}
\includegraphics[width=0.5\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/YNAB-logo.png}
\end{center}

YNAB es una de las aplicaciones de presupuesto personal más populares del mercado. Su metodología se basa en cuatro reglas fundamentales para gestionar el dinero. Entre sus puntos fuertes destaca su enfoque educativo y su comunidad activa. Sin embargo, su modelo de suscripción (84€/año) resulta elevado para usuarios jóvenes, y no ofrece ninguna funcionalidad específica para gamers. Su interfaz, aunque funcional, es genérica y no conecta emocionalmente con el público gaming.

### Spendee

\begin{center}
\includegraphics[width=0.5\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/Spendee_logo.png}
\end{center}

Spendee ofrece una interfaz visual atractiva con gráficos claros y la posibilidad de compartir presupuestos familiares. Su punto fuerte es la visualización de datos y la categorización automática de gastos. Por contra, la versión gratuita es muy limitada, no permite exportar datos y, al igual que YNAB, no tiene ninguna funcionalidad orientada al mundo gaming.

### Wallet by BudgetBakers

\begin{center}
\includegraphics[width=0.5\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/wallet_logo.png}
\end{center}

Wallet es una aplicación completa que permite conectar cuentas bancarias reales para un seguimiento automático de gastos. Ofrece informes detallados y planificación a largo plazo. Su debilidad principal es la complejidad de configuración inicial y la dependencia de la conexión bancaria (no disponible en todos los países). Tampoco tiene enfoque gaming.

### Splitwise

\begin{center}
\includegraphics[width=0.5\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/splitwise_logo.png}
\end{center}

Aunque no es una app de presupuesto personal, Splitwise es muy popular entre gamers que comparten gastos (suscripciones familiares, juegos compartidos). Su fortaleza es la gestión de gastos compartidos, pero no ofrece control de presupuesto individual ni recomendaciones financieras.

### Plataformas web especializadas

Además de las aplicaciones de gestión financiera, existen plataformas web especializadas en el seguimiento de precios de videojuegos que los jugadores utilizan como referencia para sus compras:

**SteamDB**: Base de datos no oficial de Steam con precios históricos y alertas de ofertas. Su fortaleza es la precisión de datos y el seguimiento técnico de la plataforma. Sin embargo, carece de gestión de presupuesto, no permite registrar gastos y está limitada exclusivamente a Steam.

**Deku Deals**: Agregador de precios multiplataforma (Steam, PlayStation, Nintendo, Xbox, GOG) con wishlist centralizada y alertas personalizadas. Su ventaja es la cobertura amplia de tiendas digitales. No ofrece evaluación financiera ni herramientas de presupuesto, enfocándose solo en precios y descuentos.

**PriceCharting**: Rastreador de precios orientado al coleccionismo de juegos físicos y retro. Proporciona gráficos de evolución de precios y alertas configurables. Su debilidad principal es el enfoque en coleccionismo más que en gestión de gastos, sin integración con catálogos actuales ni recomendaciones financieras.

Estas herramientas, aunque útiles para encontrar ofertas, no sustituyen una solución integral de gestión financiera gamer, ya que ninguna combina el seguimiento de precios con el control de presupuesto y recomendaciones personalizadas que ofrece AgenteGamer.


\newpage

## 3.- Propuesta Innovadora

### 3.1.- Punto diferenciador

El punto diferenciador de Agente Gamer respecto a las aplicaciones mencionadas es su **enfoque específico para gamers**. Mientras que YNAB, Spendee y Wallet son herramientas genéricas de finanzas personales, Agente Gamer entiende que:

1. **Los gamers tienen patrones de gasto específicos**: lanzamientos de juegos, DLCs, microtransacciones, suscripciones (Game Pass, PS Plus, Nintendo Online), y compras impulsivas en ofertas de Steam.

2. **La wishlist es central en la experiencia gaming**: Los gamers mantienen listas de juegos que quieren comprar, con precios estimados y fechas de lanzamiento. Agente Gamer integra esta funcionalidad nativamente, algo que ninguna app de finanzas ofrece. Además, cada juego de la wishlist recibe una evaluación financiera automática (recomendada/ajustada/no recomendada) basada en el presupuesto y gasto actual del mes.

3. **El catálogo de juegos integrado**: La conexión con RAWG API permite buscar juegos, ver precios estimados (calculados según rating) y fechas de lanzamiento sin salir de la app de finanzas. La búsqueda tiene debounce para optimizar llamadas y paginación infinita para mejor experiencia.

4. **Sistema de evaluación financiera inteligente**: El SistemaFinanciero evalúa cada compra potencial según umbrales porcentuales del presupuesto restante:
    - $\leq$ 30% del restante: "Compra recomendada"
    - $\leq$ 60% del restante: "Compra ajustada"
    - $\leq$ 100% del restante: "Mejor esperar"
   - > 100%: "No recomendable"

5. **Diseño visual gaming**: La interfaz dark theme con acentos verdes está diseñada para resonar con la estética gaming, no con la estética corporativa de las apps financieras tradicionales.

6. **Notificaciones inteligentes en segundo plano**: WorkManager ejecuta workers periódicos que validan la wishlist contra el presupuesto y notifican al usuario cuando hay compras no recomendadas o lanzamientos próximos (0-7 días).

7. **El "Agente Financiero"**: Un sistema de recomendaciones personalizadas que analiza los patrones de gasto y ofrece consejos contextuales, como un asistente virtual que entiende tanto de finanzas como de gaming.

Además, la aplicación es **gratuita**, sin modelo de suscripción, lo que la hace accesible para el público joven que compone la mayoría de la comunidad gamer.


\newpage

### 3.2.- Funcionalidades principales

**Autenticación y Perfil:**
- Login y registro con Firebase Auth
- Perfil de usuario sincronizado en Firestore (nombre, email, presupuesto, moneda, rol, fecha de creación)
- Edición de nombre mediante ActivityResultLauncher
- Roles de usuario: USER y ADMIN (los admins tienen acceso a funciones de prueba)

**Dashboard Principal (MainActivity):**
- Card grande con total gastado, estado financiero (indicador de color) y recomendación del agente
- Dos cards laterales: presupuesto mensual y restante disponible
- Gráfico de tendencia de gastos mensual (LineChart de MPAndroidChart)
- Indicador de tendencia vs mes anterior (porcentaje con flecha)
- Recomendaciones personalizadas del agente financiero basadas en patrones de gasto
- Lista horizontal de últimos gastos (RecyclerView)
- Gráfico circular de gastos por categoría (PieChart de MPAndroidChart)
- Presupuesto reactivo en tiempo real mediante LiveData

**Gestión de Gastos (ListaGastosActivity):**
- Lista de gastos con imagen, nombre, precio y fecha
- Cálculo y muestra del total de gastos del período
- Empty state cuando no hay gastos
- Botón FAB para agregar gastos de prueba (solo visible para usuarios ADMIN)
- Formateo de totales según la moneda del usuario

**Catálogo de Juegos (ListaJuegosActivity):**
- Grid de 2 columnas con portadas de juegos obtenidas de la API de RAWG
- Barra de búsqueda con debounce de 500ms para evitar llamadas excesivas
- Paginación infinita al hacer scroll
- Precarga de juegos desde SplashActivity para mejor rendimiento
- Al hacer clic en un juego, se agrega a la wishlist con precio estimado calculado según rating
- Moneda reactiva que se actualiza cuando el usuario cambia en Ajustes

**Wishlist (ListaWishlistActivity):**
- Lista de juegos deseados con imagen, nombre, precio estimado y evaluación financiera
- Evaluación en tiempo real: "Compra recomendada", "Compra ajustada", "Mejor esperar", "No recomendable"
- Cálculo y muestra del costo total estimado
- Diálogo de detalle del juego con edición de precio, plataforma y confirmación de compra
- Al comprar un juego: si es lanzamiento futuro se guarda en Lanzamientos, si ya está lanzado se crea un Gasto
- Moneda reactiva desde Firestore

**Lanzamientos (LanzamientosActivity):**
- Lista de próximos lanzamientos (próximos 15 días)
- Muestra días restantes hasta el lanzamiento
- Muestra rating del juego y plataformas disponibles
- Precarga de datos desde la API de RAWG al abrir la pantalla

**Configuración (AjustesActivity):**
- Editor de presupuesto mensual con validación
- Selector de moneda (EUR, USD, GBP) con actualización en tiempo real
- Cálculo de impacto en tiempo real basado en la wishlist actual (recomendados, ajustados, no recomendados)
- Guarda los cambios en Firestore mediante UseCase

**Perfil (PerfilActivity):**
- Muestra email, nombre, presupuesto, fecha de creación y rol
- Presupuesto reactivo en tiempo real
- Botón para editar nombre que lanza EditProfileActivity
- Indicador de carga durante actualización


\newpage

## 4.- Estudio de Viabilidad

### 4.1.- Descripción del modelo de negocio

Agente Gamer se plantea inicialmente como un **proyecto educativo de código abierto**, sin ánimo de lucro. Sin embargo, si se considerara su comercialización futura, el modelo de negocio más adecuado sería **freemium**:

- **Plan gratuito**: Gestión básica de gastos, wishlist, catálogo de juegos y recomendaciones del agente.
- **Plan premium** (2,99€/mes): Análisis avanzado de tendencias, exportación de datos, múltiples presupuestos, alertas de precios de juegos y sincronización multi-dispositivo avanzada.

Los costes de mantenimiento incluirían el hosting de Firebase (plan Spark gratuito para desarrollo, plan Blaze para producción) y el dominio de la API RAWG.

### 4.2.- Embudo de marketing

El embudo de marketing para AgenteGamer se estructura en varias fases que buscan atraer, convertir y retener usuarios.

**Fase de atracción (Awareness):** El canal principal será la optimización para la tienda Google Play con palabras clave relevantes («gestión presupuesto gaming», «control gastos videojuegos», «gestor económico gamer»). Adicionalmente, se utilizarán publicaciones en comunidades de Reddit relacionadas con gaming (r/gaming, r/patientgamers, r/Steam), foros especializados y presencia en redes sociales como Instagram y TikTok con contenido educativo sobre gestión financiera en gaming.

**Fase de conversión (Consideration):** Se ofrecerán guías descargables sobre gestión de presupuesto gaming, calculadora online de presupuesto gamer en la página web, y prueba gratuita de 7 días de la versión premium para nuevos usuarios.

**Fase de retención (Retention):** Notificaciones push personalizadas con recordatorios mensuales de presupuesto, contenido exclusivo en redes sociales para usuarios premium, y programa de referidos con beneficios para ambos usuarios.

**Fase de monetización:** Llamadas a la acción claras para upgrade a premium dentro de la aplicación, ofertas especiales en momentos clave (Black Friday, Navidad), y bundles temáticos (por ejemplo, «Pack Fin de Año Gamer»).

\newpage

\begin{center}
\includegraphics[width=\textwidth,height=\textheight,keepaspectratio]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/embudo-marketing.png}
\end{center}

\newpage

### 4.3.- Estimación de la población objetivo

El público objetivo son **gamers de entre 16 y 45 años** que realizan compras de videojuegos y desean controlar su gasto. Según datos de la AEVI (Asociación Española de Videojuegos):

- España tiene más de **16 millones de jugadores** de videojuegos
- El gasto medio anual en videojuegos es de **150€ por persona**
- El **68% de los gamers** tienen entre 16 y 45 años

Esto nos da una población objetivo estimada de aproximadamente **10,8 millones de personas** en España.

### 4.4.- Cálculo de la muestra poblacional necesaria para la encuesta

Para una población de 10,8 millones, con un nivel de confianza del 95%, un margen de error del 5% y una proporción esperada del 50% (máxima varianza):

```
n = (Z² × p × q) / E²
n = (1,96² × 0,5 × 0,5) / 0,05²
n = 384,16
```

Se necesitarían **385 encuestas** para obtener resultados estadísticamente significativos. Para este proyecto, se realizó una encuesta piloto con una muestra de **40 personas**.

### 4.5.- Encuesta y resultados

**Pregunta 1: ¿Cuánto gastas aproximadamente al mes en videojuegos?**

| Rango | Porcentaje |
|-------|------------|
| 0-20€ | 35% |
| 21-50€ | 40% |
| 51-100€ | 15% |
| Más de 100€ | 10% |

**Pregunta 2: ¿Llevas algún control de tus gastos en videojuegos?**

| Respuesta | Porcentaje |
|-----------|------------|
| Sí, de forma manual | 15% |
| Sí, con alguna app | 10% |
| No, pero me gustaría | 55% |
| No, no me interesa | 20% |

**Pregunta 3: ¿Te interesaría una app que te ayude a controlar el presupuesto de gaming?**

| Respuesta | Porcentaje |
|-----------|------------|
| Sí, definitivamente | 60% |
| Tal vez | 25% |
| No me interesa | 15% |

**Pregunta 4: ¿Qué funcionalidades te resultarían más útiles?**

| Funcionalidad | Porcentaje |
|---------------|------------|
| Control de gastos mensual | 85% |
| Lista de deseos con precios | 70% |
| Catálogo de juegos | 55% |
| Recomendaciones financieras | 45% |
| Alertas de ofertas | 75% |
| Gráficos de tendencia | 50% |

**Pregunta 5: ¿Usas alguna app de finanzas personales actualmente?**

| Respuesta | Porcentaje |
|-----------|------------|
| Sí | 20% |
| No, pero me gustaría | 35% |
| No, no me interesa | 45% |

**Conclusiones de la encuesta**:
- El **75% de los encuestados** gasta entre 0 y 50€ mensuales en videojuegos
- El **65% no lleva ningún control** de sus gastos en gaming
- El **85% estaría interesado** en una app de control de presupuesto gaming
- Las funcionalidades más demandadas son el **control de gastos** (85%) y las **alertas de ofertas** (75%)

### 4.6.- Planificación del tiempo

El desarrollo de AgenteGamer se planificó siguiendo una metodología ágil adaptada al contexto de un proyecto de fin de grado. A continuación se presenta la planificación temporal estimada para las diferentes fases del proyecto.

**Fase 1 - Planificación y diseño (Semanas 1-2):**

- Análisis de requisitos y especificaciones
- Diseño de arquitectura y estructura de datos
- Diseño de interfaces de usuario (wireframes)
- Estudio de APIs disponibles (RAWG)
- Planificación de pruebas

**Fase 2 - Desarrollo básico (Semanas 2-10):**

- Implementación de autenticación con Firebase
- Creación de base de datos local con Room
- Desarrollo del módulo de gestión de gastos
- Implementación del catálogo de juegos básico

**Fase 3 - Desarrollo avanzado (Semanas 8-16):**

- Implementación del sistema de recomendaciones del Agente
- Desarrollo de la wishlist con evaluación financiera
- Implementación de seguimiento de lanzamientos próximos
- Integración con Firebase Firestore para sincronización

**Fase 4 - Pruebas y refinamiento (Semanas 16-19):**

- Pruebas unitarias y de integración
- Pruebas de usuario y validación
- Corrección de errores
- Optimización de rendimiento

**Fase 5 - Documentación y presentación (Semanas 18-20):**

- Redacción de documentación técnica
- Elaboración del manual de usuario
- Preparación de materiales para la presentación
- Revisión final y corrección

\newpage

\begin{center}
\includegraphics[width=\textwidth,height=\textheight,keepaspectratio]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/gantt-planificacion.png}
\end{center}

\newpage

### 4.7.- Estimación de costes

La estimación de costes del proyecto se divide en costes de desarrollo, costes de infraestructura y costes operativos recurrentes.

**Costes de desarrollo:**

El coste principal es el tiempo de desarrollo del estudiante. Considerando una estimación de 400 horas de trabajo efectivo a un coste hora equivalente de 15 euros (basado en el salario mínimo profesional para desarrolladores junior), el coste de desarrollo se estima en **6.000 euros**.

**Costes de infraestructura:**

| Concepto | Coste mensual | Coste anual |
|----------|---------------|-------------|
| Firebase (Spark Plan gratuito) | 0 € | 0 € |
| Dominio y hosting web (opcional) | 5 € | 60 € |
| API RAWG (plan gratuito hasta 20.000 req/mes) | 0 € | 0 € |
| **Total infraestructura** | **5 €** | **60 €** |

**Costes operativos (año 1):**

| Concepto | Coste |
|----------|-------|
| Google Play Developer (una vez) | 25 € |
| Licencia de software (Android Studio - gratuito) | 0 € |
| Mantenimiento y actualizaciones | 200 € |
| **Total primer año** | **225 €** |

**Coste total estimado del proyecto:** 6.285 euros (considerando el primer año completo).

### 4.8.- Estimación de ingresos

La estimación de ingresos se basa en las proyecciones de conversión de usuarios gratuitos a premium y en el número total de usuarios esperados.

**Escenario conservador:**

- Usuarios totales esperados (año 1): 5.000 descargas
- Tasa de conversión a premium: 3%
- Precio medio mensual: 2,99 €
- Ingresos mensuales estimados: 5.000 × 0,03 × 2,99 = **448,50 €**
- Ingresos anuales estimados: 5.382 €

**Escenario optimista:**

- Usuarios totales esperados (año 1): 20.000 descargas
- Tasa de conversión a premium: 5%
- Precio medio mensual: 2,99 €
- Ingresos mensuales estimados: 20.000 × 0,05 × 2,99 = **2.990 €**
- Ingresos anuales estimados: 35.880 €

**Escenario pesimista:**

- Usuarios totales esperados (año 1): 1.000 descargas
- Tasa de conversión a premium: 2%
- Precio medio mensual: 2,99 €
- Ingresos mensuales estimados: 1.000 × 0,02 × 2,99 = **59,80 €**
- Ingresos anuales estimados: 717,60 €

\vspace{1cm}

\begin{center}
\includegraphics[width=\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/proyeccion-ingresos.png}
\end{center}

\newpage

### 4.9.- Conclusiones

El estudio de viabilidad demuestra que AgenteGamer representa un proyecto viable tanto técnica como económicamente. La necesidad de mercado está respaldada por la ausencia de herramientas integradoras que combinen gestión financiera con catálogo de juegos y recomendaciones personalizadas. Los resultados de la encuesta confirman que la mayoría de los gamers no llevan un control de sus gastos y estarían interesados en una solución específica.

**Viabilidad técnica:** El proyecto es realizable con las tecnologías seleccionadas (Java, Android SDK, Room, Firebase, Retrofit), todas ellas bien documentadas y con comunidades activas de soporte. La arquitectura Clean Architecture + MVVM permite un desarrollo ordenado y mantenible.

**Viabilidad económica:** El modelo de negocio basado en suscripción ofrece un potencial de ingresos interesante incluso en escenarios conservadores. Los costes de infraestructura son mínimos gracias a los planes gratuitos de Firebase y RAWG, lo que permite mantener la rentabilidad desde los primeros usuarios premium. El punto de equilibrio es alcanzable con una base de usuarios moderada.

**Análisis de riesgos:** Se identifican las principales incertidumbres: la dependencia de una API externa (RAWG), la competencia con herramientas establecidas de seguimiento de precios, y la necesidad de construir confianza con los usuarios respecto al manejo de datos financieros. Estos riesgos se mitigan mediante la implementación de funcionalidades offline, una propuesta de valor diferenciada, y políticas de privacidad transparentes.


\newpage

## 5.- Arquitectura de la Aplicación

### 5.1.- Recursos y tecnologías empleadas

| Categoría | Tecnología | Versión |
|-----------|------------|---------|
| **Lenguaje** | Java | 17 |
| **Build System** | Gradle (Kotlin DSL) | - |
| **SDK** | minSdk 23, compileSdk 36, targetSdk 36 | - |
| **Arquitectura** | Clean Architecture + MVVM | - |
| **Inyección de dependencias** | Dagger Hilt | 2.51.1 |
| **Base de datos local** | Room | 2.8.4 |
| **Base de datos cloud** | Firestore | Firebase BOM 34.7.0 |
| **Autenticación** | Firebase Auth | Firebase BOM 34.7.0 |
| **Networking** | Retrofit + Gson | 2.11.0 |
| **API externa** | RAWG.io (catálogo de juegos) | - |
| **Logging HTTP** | OkHttp Logging Interceptor | 4.12.0 |
| **Gráficos** | MPAndroidChart (PieChart, LineChart) | 3.1.0 |
| **Carga de imágenes** | Glide | 4.16.0 |
| **Background tasks** | WorkManager + Hilt Worker | 2.11.0 |
| **UI Components** | Material Components, BottomNavigationView, DrawerLayout | 1.13.0 |
| **Splash Screen** | Splash Screen API | 1.0.1 |
| **Testing** | JUnit, Mockito, Espresso | 4.13.2 / 5.11.0 / 3.7.0 |

\newpage

\begin{center}
\includegraphics[width=\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/stack-tecnologico.png}
\end{center}

\newpage

### 5.2.- Arquitectura técnica

La aplicación sigue el patrón **Clean Architecture** con **MVVM** (Model-View-ViewModel), utilizando **Dagger Hilt** para la inyección de dependencias, **LiveData** para la reactividad de la UI y **Room** como base de datos local con scoping por userId.

La sincronización con la nube se realiza mediante **Firebase Auth** para autenticación y **Firestore** para datos de usuario (presupuesto, moneda, perfil). Los gastos, wishlist y lanzamientos se almacenan localmente en **Room** con acceso offline.

La integración con la API externa de **RAWG** se realiza mediante **Retrofit + Gson**, con precarga de datos durante la splash screen para mejorar el rendimiento percibido.

**Estructura del proyecto:**

```
com.miapp.agentegamer/
├── data/
│   ├── local/          # Room entities, DAOs, database
│   ├── remote/         # Retrofit API, DTOs
│   ├── repository/     # Repository implementations
│   └── worker/         # WorkManager workers
├── domain/
│   ├── model/          # Domain models (Gasto, SistemaFinanciero, etc.)
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Use cases
├── di/                 # Hilt modules
├── ui/
│   ├── splash/         # SplashActivity
│   ├── auth/           # Login/Register
│   ├── main/           # Dashboard
│   ├── gastos/         # Expenses
│   ├── games/          # Game catalog
│   ├── wishlist/       # Wishlist
│   ├── lanzamientos/   # Upcoming releases
│   ├── perfil/         # Profile
│   ├── ajustes/        # Settings
│   ├── adapter/        # RecyclerView adapters
│   ├── viewmodel/      # ViewModels
│   └── common/         # BaseNavActivity, utilities
└── util/               # Helpers (MoneyUtils, PeriodoFinancieroUtils, PlatformUtils)
```

\vspace*{\fill}

\begin{center}
\includegraphics[width=\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/clean-architecture.png}
\end{center}

\vspace*{\fill}

\newpage

\vspace*{\fill}

\begin{center}
\includegraphics[width=\textwidth,height=\textheight,keepaspectratio]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/diagrama_clases_rotada.png}
\end{center}

\vspace*{\fill}

\newpage

\begin{center}
\includegraphics[width=\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/diagrama_er.png}
\end{center}

\newpage

### 5.3.- Diseño inicial

El diseño inicial contemplaba las siguientes pantallas:

**Pantalla de Splash**: Logo de AgenteGamer con tagline "Tu asistente gaming financiero". Precarga de datos de juegos en segundo plano durante 1.5 segundos.

**Pantalla de Login/Registro**: Formulario simple con email y contraseña, autenticación mediante Firebase Auth. Registro incluye nombre y presupuesto inicial.

**Dashboard Principal**:
- Card grande con total gastado y estado financiero
- Dos cards laterales: presupuesto y restante
- Gráfico circular de gastos por categoría
- Lista de gastos recientes

**Gestión de Gastos**: Lista de gastos con opción de añadir, editar y eliminar. Filtro por fecha.

**Catálogo de Juegos**: Grid de 2 columnas con búsqueda por nombre. Integración con RAWG API.

**Wishlist**: Lista de juegos deseados con precio estimado y coste total.

**Lanzamientos**: Lista de juegos próximos con cuenta regresiva.

**Configuración**: Selector de moneda y ajuste de presupuesto.

**Perfil**: Información del usuario con opción de editar nombre.

\begin{center}
\includegraphics[width=\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/Sketch_AgenteGamer.png}
\end{center}

\newpage

### 5.4.- Diseño final

El diseño final implementado mantiene la estructura inicial pero con mejoras significativas:

**UI Revolut-style**: Se adoptó un diseño dark theme inspirado en la app Revolut, con cards redondeadas (20dp radius), tipografía Manrope y acentos en verde. La estética gaming se logra con colores oscuros (midnight blue, slate gray) y acentos verdes.

**Bottom Navigation Bar**: Se añadió una barra de navegación inferior con 5 secciones (Home, Gastos, Juegos, Wishlist, Lanzamientos) para navegación fluida entre secciones sin necesidad de usar el menú lateral. Implementada mediante una clase base abstracta `BaseNavActivity` que centraliza la lógica de navegación con `FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP` para evitar duplicación de actividades. Incluye corrección de selección visual en `onResume()` para que el ítem correcto quede resaltado al volver de otra actividad.

**Splash Screen con Preloading**: Se implementó una splash screen que precarga datos de juegos desde la API de RAWG durante el arranque (mediante `AgenteGamerApplication.preloadGames()`), reduciendo el tiempo de carga percibido cuando el usuario llega al catálogo.

**Gráfico de tendencias**: Se añadió un LineChart mensual que muestra la evolución de gastos a lo largo del tiempo, con indicador de tendencia vs mes anterior (porcentaje con flecha y color según dirección).

**Recomendaciones del Agente**: Un card dedicado con recomendaciones personalizadas del agente financiero basadas en los patrones de gasto del usuario. El sistema evalúa el estado financiero (verde/amarillo/rojo) según umbrales del presupuesto (50% verde, 80% amarillo, >80% rojo) y genera mensajes contextuales.

**Moneda reactiva**: El selector de moneda en Ajustes se propaga reactivamente a todas las pantallas mediante LiveData y snapshot listeners de Firestore. Soporta EUR, USD y GBP con conversión automática usando tasas fijas.

**Evaluación financiera de wishlist**: Cada juego de la wishlist recibe una evaluación automática (recomendada/ajustada/no recomendada) basada en el presupuesto y gasto actual del mes. El cálculo se recalcula en tiempo real cuando cambia el presupuesto, el gasto o la wishlist, usando `MediatorLiveData` que combina tres fuentes de datos.

**Compra inteligente**: Al comprar un juego desde la wishlist, el sistema decide automáticamente: si el juego es un lanzamiento futuro (fecha > hoy), se guarda en la tabla de Lanzamientos; si ya está lanzado, se crea un Gasto y se elimina de la wishlist.

**Notificaciones en segundo plano**: Dos workers de WorkManager (`SistemaFinancieroWorker` y `LanzamientoWorker`) se ejecutan periódicamente para validar compras contra el presupuesto y notificar sobre lanzamientos próximos (0-7 días).

**Menú lateral (Drawer)**: Complementa la bottom navigation con acceso a Perfil, Ajustes y Cerrar sesión. Incluye header con nombre, email, rol y fecha de creación del usuario.

**Scoping por userId**: Todas las consultas de Room están filtradas por `userId` de Firebase, garantizando que cada usuario solo ve sus propios datos.


\newpage

\begin{center}
\includegraphics[width=\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/wireframes-navegacion.png}
\end{center}

\newpage

## 6.- Futuras Mejoras y Conclusiones

Como nunca estoy realmente satisfecho al 100% con mi trabajo y siempre pienso que debe haber algo que pueda mejorar, voy a indicar varias cosas que me hubiera gustado implementar:

**Notificaciones push para ofertas**: Integrar un sistema de alertas que notifique al usuario cuando un juego de su wishlist baje de precio en tiendas como Steam, Epic Games o GOG. Actualmente las notificaciones solo alertan sobre lanzamientos próximos y compras no recomendadas.

**Sincronización con cuentas bancarias**: Mediante APIs como Plaid o Tink, permitir la importación automática de transacciones relacionadas con gaming. Actualmente los gastos se registran manualmente.

**Modo comunidad**: Permitir compartir presupuestos y logros financieros con amigos, creando un componente social que incentive el ahorro.

**Presupuestos por categoría**: Permitir establecer límites de gasto por categoría (juegos, suscripciones, microtransacciones) con alertas cuando se acerque al límite. Actualmente solo hay un presupuesto mensual global.

**Integración con calendarios de ofertas**: Sincronizar con eventos como las rebajas de Steam, Black Friday o las ofertas de PlayStation Store.

**Modo offline completo**: Mejorar la experiencia offline para que todas las funcionalidades estén disponibles sin conexión, sincronizando cuando se reconecte. Actualmente el catálogo de juegos requiere conexión a la API de RAWG.

**Análisis de hábitos de compra**: Usar machine learning para identificar patrones de compra impulsiva y ofrecer recomendaciones preventivas.

**Soporte para múltiples perfiles**: Permitir gestionar presupuestos de diferentes miembros de la familia desde una misma cuenta.

**Migraciones de Room no destructivas**: Actualmente la base de datos usa `fallbackToDestructiveMigration()`, lo que significa que cualquier cambio de schema elimina todos los datos locales. Implementar migraciones progresivas sería esencial para producción.

**Tests de integración**: Ampliar la cobertura de tests unitarios actuales con tests de integración que verifiquen el flujo completo de datos entre Room, ViewModel y UI.


\newpage

## 7.- Pruebas y Resultados

### Prueba 1: Autenticación - Registro

**Escenario**: Registro de nuevo usuario

**Pasos**:
1. Abrir la aplicación (Splash → Login)
2. Pulsar "Registrarse"
3. Introducir email, contraseña, nombre y presupuesto mensual
4. Pulsar "Crear cuenta"

**Resultado esperado**: El usuario se registra en Firebase Auth, se crea su perfil en Firestore con nombre, presupuesto, rol "USER" y fecha de creación. Se redirige al Dashboard.

**Resultado obtenido**: Correcto. El usuario se registra y accede al Dashboard principal.


### Prueba 2: Autenticación - Login

**Escenario**: Inicio de sesión con credenciales válidas e inválidas

**Pasos**:
1. Abrir la aplicación
2. Introducir email y contraseña correctos
3. Pulsar "Iniciar sesión"

**Resultado esperado**: Navega a MainActivity. Si las credenciales son incorrectas, muestra mensaje de error específico (red para errores de red, credenciales para datos incorrectos).

**Resultado obtenido**: Correcto. Login exitoso redirige al Dashboard. Credenciales incorrectas muestran toast apropiado.


### Prueba 3: Dashboard - Resumen financiero

**Escenario**: Visualización del estado financiero

**Pasos**:
1. Iniciar sesión como usuario con gastos registrados
2. Observar el Dashboard

**Resultado esperado**: Se muestra total gastado, presupuesto, restante, gráfico circular de categorías, gráfico de tendencia mensual, recomendaciones del agente y lista de últimos gastos.

**Resultado obtenido**: Correcto. Todos los componentes del dashboard se cargan y actualizan reactivamente.


### Prueba 4: Gestión de gastos

**Escenario**: Añadir un nuevo gasto

**Pasos**:
1. Desde el Dashboard, pulsar "Gastos" en la barra inferior
2. (Solo admin) Pulsar FAB para agregar gasto de prueba

**Resultado esperado**: El gasto se guarda en Room y se actualiza la lista con el nuevo gasto. El total se recalcula.

**Resultado obtenido**: Correcto. El gasto se guarda y la lista se actualiza automáticamente.


### Prueba 5: Catálogo de juegos

**Escenario**: Buscar un juego en el catálogo

**Pasos**:
1. Ir a la sección Juegos
2. Introducir "Zelda" en la barra de búsqueda
3. Esperar los resultados

**Resultado esperado**: Se muestran juegos con "Zelda" en el nombre, obtenidos de la API RAWG. La búsqueda tiene debounce de 500ms.

**Resultado obtenido**: Correcto. Los resultados se muestran en un grid de 2 columnas con paginación infinita.


### Prueba 6: Wishlist - Edición de precio

**Escenario**: Editar el precio estimado de un juego en la wishlist

**Pasos**:
1. Ir a la sección Wishlist
2. Pulsar sobre un juego
3. Pulsar "Editar precio"
4. Introducir nuevo precio y guardar
5. Cerrar y volver a abrir el juego

**Resultado esperado**: El nuevo precio se muestra correctamente al reabrir el detalle.

**Resultado obtenido**: Correcto. El precio se persiste en Room y se muestra actualizado.


### Prueba 7: Navegación inferior

**Escenario**: Navegar entre secciones usando la Bottom Navigation

**Pasos**:
1. Estar en el Dashboard (Home)
2. Pulsar "Juegos" en la barra inferior
3. Pulsar "Home" para volver

**Resultado esperado**: La navegación es fluida y el ítem correcto queda resaltado en verde.

**Resultado obtenido**: Correcto. La selección del bottom nav se resetea correctamente al volver gracias al `onResume()` en `BaseNavActivity`.


### Prueba 8: Cambio de moneda

**Escenario**: Cambiar la moneda en Ajustes

**Pasos**:
1. Ir a Ajustes desde el menú lateral
2. Cambiar moneda de EUR a USD
3. Volver al Dashboard

**Resultado esperado**: Todos los importes se muestran en USD con la conversión aplicada (tasa 1.08).

**Resultado obtenido**: Correcto. La moneda se propaga reactivamente a todas las pantallas.


### Prueba 9: Perfil - Edición de nombre

**Escenario**: Editar el nombre del usuario

**Pasos**:
1. Ir a Perfil desde el menú lateral
2. Pulsar el botón de editar nombre
3. Introducir nuevo nombre y guardar

**Resultado esperado**: El nombre se actualiza en Firestore y se refleja en el perfil.

**Resultado obtenido**: Correcto. El nombre se actualiza con indicador de progreso durante la operación.


### Prueba 10: Ajustes - Impacto en wishlist

**Escenario**: Cambiar presupuesto y ver impacto en wishlist

**Pasos**:
1. Ir a Ajustes
2. Modificar el presupuesto mensual
3. Observar los contadores de impacto (recomendados, ajustados, no recomendados)

**Resultado esperado**: Los contadores se recalculan en tiempo real según el nuevo presupuesto.

**Resultado obtenido**: Correcto. El impacto se recalcula con cada cambio en el campo de presupuesto.


\newpage

### 7.2.- Tests Unitarios (JUnit + Mockito)

La aplicación cuenta con **10 tests unitarios** que verifican la lógica de negocio de forma aislada, sin necesidad de un dispositivo Android. Se ejecutan en la JVM local usando JUnit 4 y Mockito para mocking de dependencias.

**Herramientas de testing empleadas:**

| Herramienta | Versión | Propósito |
|-------------|---------|-----------|
| JUnit 4 | 4.13.2 | Framework de tests unitarios |
| Mockito | 5.11.0 | Mocking de dependencias (repositorios, DAOs) |
| InstantTaskExecutorRule | 2.2.0 | Ejecución síncrona de LiveData en tests |
| Espresso | 3.7.0 | Tests de instrumentación en dispositivo |

#### Tests de ViewModels

**GastoViewModelTest** (10 tests):
- Verifica que los LiveData no sean null (`getListaGastos`, `getEstadoUI`)
- Testea `setSistemaFinanciero()` con gastos y con lista vacía
- Verifica que los métodos CRUD (`insertar`, `actualizar`, `borrar`) deleguen correctamente al repositorio mockeado
- Tests de edge cases con valores null
- Verifica que `onCleared()` no lance excepciones

**GamesViewModelTest** (11 tests):
- Verifica inicialización del observer de carga en el constructor
- Testea `cargarJuegosIniciales()` y `buscarJuegosPaginados()` con reset y sin reset
- Tests de `obtenerFamiliasPlataformas()` con null, PlayStation, múltiples plataformas
- Tests de `getFamilyIcon()` para familias conocidas y desconocidas
- Verifica limpieza de observers en `onCleared()`

#### Tests de Repositorios

**GastoRepositoryImplTest** (8 tests):
- Verifica que `obtenerGastos()` devuelva el LiveData del DAO
- Testea que las operaciones CRUD se ejecuten en background mediante `ExecutorService` (capturando el Runnable con `ArgumentCaptor`)
- Verifica `getGastoMesActual()` y `getTotalGastadoMesSync()` con callbacks

#### Tests de Entidades Room

**GastoEntityTest** (7 tests):
- Verifica que el constructor calcule correctamente `mes` y `anio` desde la fecha (enero, diciembre)
- Testea que `setFecha()` actualice mes y anio automáticamente
- Verifica que se preserven nombre y precio al cambiar la fecha
- Test de cruce de año (31 dic → 1 ene)
- Tests de getters y setters

**WishlistEntityTest** (8 tests):
- Verifica asignación correcta de campos en el constructor
- **Test específico de bug fix**: confirma que `precioEstimado` y `plataforma` no estén intercambiados (bug corregido previamente)
- Tests con precio cero, strings vacíos, y verificación de Serializable

#### Tests de Utilidades

**MoneyUtilsTest** (19 tests):
- Formateo con moneda por defecto (EUR), explícita (EUR, USD, GBP)
- Manejo de moneda null, vacía e inválida
- Formateo de valores negativos
- Conversión EUR → USD (100 × 1.08 = 108), EUR → GBP (100 × 0.86 = 86)
- Verificación de que format() convierte Y formatea correctamente (no muestra el monto original)

**PresupuestoCalculatorTest** (10 tests):
- Cálculo de presupuesto restante: positivo, cero, negativo, sin gastos
- Cálculo de porcentaje gastado (159.97 / 150 = 106.64%)
- Verificación de sobre-presupuesto y bajo-presupuesto
- **Caso real del usuario**: presupuesto 150€, gastado 159.97€, restante -9.97€

**PeriodoFinancieroUtilsTest** (6 tests):
- Verifica que `getMesActual()` retorne valor entre 1 y 12
- Verifica coincidencia con `Calendar.getInstance()`
 - Verifica que `getAnioActual()` sea razonable ($\geq$ 2024)
- Test de `inicioMesActual()`: día 1, hora 0:0:0, menor a hora actual

**PlatformUtilsTest** (13 tests):
- Agrupación de plataformas por familia: PlayStation (PS4, PS5 → "playstation"), Xbox, Nintendo, PC
- Test con múltiples plataformas simultáneas (4 familias)
- Tests con game null y platforms null
- Tests de `getFamilyIcon()` para cada familia y casos desconocidos

#### Tests de Instrumentación

**ExampleInstrumentedTest** (1 test):
- Verifica que el contexto de la aplicación sea correcto (`com.miapp.agentegamer`)
- Se ejecuta en dispositivo Android real o emulador

#### Resumen de cobertura

| Categoría | Tests | Qué cubre |
|-----------|-------|-----------|
| ViewModels | 21 | GastoViewModel, GamesViewModel |
| Repositorios | 8 | GastoRepositoryImpl |
| Entidades Room | 15 | GastoEntity, WishlistEntity |
| Utilidades | 48 | MoneyUtils, PresupuestoCalculator, PeriodoFinancieroUtils, PlatformUtils |
| Instrumentación | 1 | Contexto de la app |
| **Total** | **93** | |


\newpage

## 8.- Manual Usuario

### 8.1.- Primeros pasos

**Registro**:
1. Abre la aplicación
2. Si no tienes cuenta, pulsa en "Registrarse"
3. Introduce tu email, contraseña, nombre y presupuesto mensual
4. Pulsa "Crear cuenta"
5. Serás redirigido al Dashboard principal

**Inicio de sesión**:
1. Abre la aplicación
2. Introduce tu email y contraseña
3. Pulsa "Iniciar sesión"

\vspace{0.5cm}

\begin{minipage}{0.48\textwidth}
\centering
\includegraphics[width=\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/registro.png}
\end{minipage}
\hfill
\begin{minipage}{0.48\textwidth}
\centering
\includegraphics[width=\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/inicio_de_sesion.png}
\end{minipage}

\vspace{0.5cm}

\newpage

### 8.2.- Dashboard Principal

El Dashboard es tu centro de control financiero. Aquí encontrarás:

- **Total gastado**: El importe total que has gastado este mes en videojuegos
- **Presupuesto**: El límite mensual que has establecido
- **Restante**: Cuánto te queda por gastar este mes
- **Indicador de estado**: Punto de color (verde/amarillo/rojo) que indica tu situación financiera
- **Tendencia de gastos**: Gráfico de línea que muestra tu evolución mensual
- **Indicador de tendencia**: Flecha con porcentaje comparando con el mes anterior
- **Tu Agente dice**: Recomendaciones personalizadas basadas en tus hábitos
- **Últimos gastos**: Lista horizontal de tus compras más recientes
- **Gastos por categoría**: Gráfico circular con la distribución de tus gastos

\vspace{0.5cm}

\begin{minipage}{0.48\textwidth}
\centering
\includegraphics[width=\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/dashboard.png}
\end{minipage}
\hfill
\begin{minipage}{0.48\textwidth}
\centering
\includegraphics[width=\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/dashboard_dos.png}
\end{minipage}

\vspace{0.5cm}

\newpage

### 8.3.- Gestión de Gastos

**Ver tus gastos**:
1. Pulsa "Gastos" en la barra inferior
2. Verás la lista de todos tus gastos con imagen, nombre, precio y fecha
3. El total se muestra en la parte superior

\vspace{0.5cm}

\begin{center}
\includegraphics[width=0.6\textwidth,height=0.7\textheight,keepaspectratio]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/gastos.png}
\end{center}

\vspace{0.5cm}

\newpage

### 8.4.- Catálogo de Juegos

**Buscar juegos**:
1. Pulsa "Juegos" en la barra inferior
2. Usa la barra de búsqueda para encontrar un juego (mínimo 3 caracteres)
3. Los resultados se muestran en un grid de 2 columnas
4. Haz scroll hacia abajo para cargar más resultados (paginación infinita)

**Añadir a la wishlist**:
1. Pulsa sobre cualquier juego del catálogo
2. El juego se añade automáticamente a tu wishlist con un precio estimado basado en su rating

\vspace{0.5cm}

\begin{center}
\includegraphics[width=0.6\textwidth,height=0.7\textheight,keepaspectratio]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/catalogo.png}
\end{center}

\vspace{0.5cm}

\newpage

### 8.5.- Wishlist

**Ver tu lista de deseos**:
1. Pulsa "Wishlist" en la barra inferior
2. Verás todos los juegos que has añadido con su precio estimado y evaluación financiera

**Evaluaciones financieras**:
- **Compra recomendada**: El precio es bajo comparado con tu presupuesto restante
- **Compra ajustada**: El precio es moderado, podrías comprarlo pero con precaución
- **Mejor esperar**: El precio consume gran parte de tu presupuesto restante
- **No recomendable**: No tienes suficiente presupuesto para esta compra

**Editar precio estimado**:
1. Pulsa sobre un juego de la wishlist
2. Pulsa "Editar precio"
3. Introduce el nuevo precio y pulsa "Guardar"

**Eliminar de la wishlist**:
1. Pulsa sobre el botón de eliminar junto al juego
2. El juego se elimina de tu lista

**Comprar un juego**:
1. Pulsa sobre un juego de la wishlist
2. Pulsa "Comprar"
3. Si el juego aún no ha sido lanzado, se moverá a Lanzamientos
4. Si ya está disponible, se creará un gasto y se eliminará de la wishlist

\begin{minipage}{0.48\textwidth}
\centering
\includegraphics[width=\textwidth,height=0.6\textheight,keepaspectratio]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/whislist.png}
\end{minipage}
\hfill
\begin{minipage}{0.48\textwidth}
\centering
\includegraphics[width=\textwidth,height=0.6\textheight,keepaspectratio]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/wishlist_editor.png}
\end{minipage}

\newpage

### 8.6.- Lanzamientos

**Ver próximos lanzamientos**:
1. Pulsa "Lanzamientos" en la barra inferior
2. Verás una lista de juegos próximos con días restantes, rating y plataformas

\vspace{0.5cm}

\begin{center}
\includegraphics[width=0.6\textwidth,height=0.7\textheight,keepaspectratio]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/lanzamientos.png}
\end{center}

\vspace{0.5cm}

\newpage

### 8.7.- Perfil

**Ver tu perfil**:
1. Abre el menú lateral (arriba a la izquierda) ubicado en la pantalla principal.
2. Pulsa "Perfil"
3. Verás tu email, nombre, presupuesto, fecha de creación y rol

**Editar tu nombre**:
1. En la pantalla de Perfil, pulsa el botón de editar junto a tu nombre
2. Introduce el nuevo nombre
3. Pulsa "Guardar"

\vspace{0.5cm}

\begin{minipage}{0.48\textwidth}
\centering
\includegraphics[width=\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/perfil_uno.png}
\end{minipage}
\hfill
\begin{minipage}{0.48\textwidth}
\centering
\includegraphics[width=\textwidth]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/perfil_tres.png}
\end{minipage}

\vspace{0.5cm}

\newpage

### 8.8.- Configuración

**Cambiar moneda**:
1. Abre el menú lateral (arriba a la izquierda) ubicado en la pantalla principal.
2. Ve a "Ajustes"
3. Selecciona tu moneda preferida: EUR, USD o GBP
4. El cambio se aplica automáticamente a todas las pantallas

**Ajustar presupuesto**:
1. Ve a "Ajustes"
2. Introduce tu nuevo presupuesto mensual
3. Observa cómo cambia el impacto en tu wishlist en tiempo real
4. Pulsa "Guardar"

\vspace{0.5cm}

\begin{center}
\includegraphics[width=0.6\textwidth,height=0.7\textheight,keepaspectratio]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/perfil_dos.png}
\end{center}

\vspace{0.5cm}

\newpage

### 8.9.- Menú lateral (Drawer)

El menú lateral se abre deslizando desde el borde izquierdo o pulsando el icono de hamburguesa en la toolbar. Contiene:

- **Perfil**: Ver y editar tu información
- **Juegos**: Ir al catálogo de juegos
- **Wishlist**: Ir a tu lista de deseos
- **Gastos**: Ir al historial de gastos
- **Lanzamientos**: Ver próximos lanzamientos
- **Ajustes**: Configurar moneda y presupuesto
- **Cerrar sesión**: Salir de la aplicación

\begin{center}
\includegraphics[width=0.6\textwidth,height=0.7\textheight,keepaspectratio]{/home/javier/proyectos/AgenteGamer/AgenteGamer_APP/docs/img/perfil_uno.png}
\end{center}


\newpage

## 9.- Bibliografía

- **Documentación de Android Developers.** Android Developers. [En línea]. Disponible en: \url{https://developer.android.com/}
- **Documentación de Firebase.** Google. [En línea]. Disponible en: \url{https://firebase.google.com/docs}
- **Documentación de Room Database.** Google. [En línea]. Disponible en: \url{https://developer.android.com/jetpack/androidx/releases/room}
- **Documentación de Dagger Hilt.** Google. [En línea]. Disponible en: \url{https://developer.android.com/training/dependency-injection/hilt-android}
- **RAWG Video Games Database API.** RAWG. [En línea]. Disponible en: \url{https://rawg.io/apidocs}
- **Documentación de MPAndroidChart.** PhilJay. [En línea]. Disponible en: \url{https://github.com/PhilJay/MPAndroidChart}
- **Documentación de Retrofit.** Square. [En línea]. Disponible en: \url{https://square.github.io/retrofit/}
- **Documentación de WorkManager.** Google. [En línea]. Disponible en: \url{https://developer.android.com/topic/libraries/architecture/workmanager}
- **Clean Architecture para aplicaciones Android.** Google Codelabs. [En línea]. Disponible en: \url{https://developer.android.com/courses/pathways/android-architecture}
- **Material Design Guidelines.** Google. [En línea]. Disponible en: \url{https://material.io/design}
- **Informe anual de la industria del videojuegos 2024.** AEVI - Asociación Española de Videojuegos. [En línea]. Disponible en: \url{https://www.aevi.org.es/}
- **Estadísticas de consumo de videojuegos.** Newzoo. [En línea]. Disponible en: \url{https://newzoo.com/}


\newpage

## 10.- Anexo I - Principales clases del código

### Entidades Room

**GastoEntity.java** - Tabla `gastos`:
```java
@Entity(tableName = "gastos",
    indices = {@Index(value = {"mes", "anio"}), @Index(value = {"userId"})})
public class GastoEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userId;
    private String nombreJuego;
    private double precio;
    private long fecha;  // UNIX epoch en milisegundos
    private int mes;     // Calculado automáticamente
    private int anio;    // Calculado automáticamente
    private String imagenUrl;
    // Getters y setters con recálculo de mes/anio al cambiar fecha
}
```

**WishlistEntity.java** - Tabla `wishlist`:
```java
@Entity(tableName = "wishlist")
public class WishlistEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userId;
    private int gameId;
    private String nombre;
    private String fechaLanzamiento;
    private String imagenUrl;
    private String plataforma;
    private double precioEstimado;
    // Getters y setters
}
```

**LanzamientoEntity.java** - Tabla `lanzamientos`:
```java
@Entity(tableName = "lanzamientos")
public class LanzamientoEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userId;
    private int gameId;
    private String nombre;
    private long fechaLanzamiento;
    private double precio;
    private String imagenUrl;
    private String plataformas;
    private double rating;
    // Getters y setters
}
```

### Base de datos

**AppDatabase.java**:
```java
@Database(entities = {GastoEntity.class, WishlistEntity.class,
    LanzamientoEntity.class}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GastoDao gastoDao();
    public abstract WishlistDao wishlistDao();
    public abstract LanzamientoDao lanzamientoDao();
}
```

### DAOs

**GastoDao.java** - Operaciones sobre gastos:
```java
@Dao
public interface GastoDao {
    @Insert void insertGasto(GastoEntity gasto);
    @Update void updateGasto(GastoEntity gasto);
    @Delete void deleteGasto(GastoEntity gasto);

    @Query("SELECT * FROM gastos WHERE userId = :userId ORDER BY fecha DESC")
    LiveData<List<GastoEntity>> getAllGastos(String userId);

    @Query("SELECT IFNULL(SUM(precio),0) FROM gastos WHERE userId = :userId " +
           "AND mes = :mes AND anio = :anio")
    double getTotalGastadoMes(String userId, int mes, int anio);

    @Query("SELECT strftime('%Y-%m', fecha/1000, 'unixepoch') as mes, " +
           "SUM(precio) as total FROM gastos WHERE userId = :userId " +
           "AND fecha >= :startDate GROUP BY mes ORDER BY mes ASC LIMIT :limit")
    List<MonthlyTotal> getMonthlyTotals(String userId, long startDate, int limit);

    @Query("SELECT * FROM gastos WHERE userId = :userId " +
           "ORDER BY fecha DESC LIMIT :limit")
    LiveData<List<GastoEntity>> getRecentGastos(String userId, int limit);
}
```

**WishlistDao.java** - Operaciones sobre wishlist:
```java
@Dao
public interface WishlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WishlistEntity juego);

    @Query("SELECT * FROM wishlist WHERE userId = :userId")
    LiveData<List<WishlistEntity>> getWishlist(String userId);

    @Update void actualizar(WishlistEntity juego);
    @Delete void borrar(WishlistEntity juego);

    @Query("SELECT * FROM wishlist WHERE userId = :userId")
    List<WishlistEntity> getWishlistSync(String userId);

    @Query("SELECT SUM(precioEstimado) FROM wishlist WHERE userId = :userId")
    Double getTotalGastado(String userId);
}
```

### Modelo de dominio

**SistemaFinanciero.java** - Lógica de negocio financiera:
```java
public class SistemaFinanciero {
    // Umbrales: 50% verde, 80% amarillo, >80% rojo
    private static final double PORCENTAJE_UMBRAL_VERDE = 0.5;
    private static final double PORCENTAJE_UMBRAL_AMARILLO = 0.8;

    // Umbrales de evaluación de compra
    private static final double PORCENTAJE_EVALUAR_RECOMENDADO = 0.3;
    private static final double PORCENTAJE_EVALUAR_AJUSTADO = 0.6;

    // Precios estimados por rating
    private static final double PRECIO_ALTO = 69.99;    // rating >= 4.5
    private static final double PRECIO_MEDIO_ALTO = 59.99; // rating >= 4.0
    private static final double PRECIO_MEDIO = 49.99;   // rating >= 3.5
    private static final double PRECIO_BAJO = 39.99;    // rating < 3.5

    private final double presupuestoMensual;

    public EstadoFinanciero obtenerEstado(double totalGastado) {
        if (totalGastado < presupuestoMensual * PORCENTAJE_UMBRAL_VERDE)
            return EstadoFinanciero.VERDE;
        else if (totalGastado < presupuestoMensual * PORCENTAJE_UMBRAL_AMARILLO)
            return EstadoFinanciero.AMARILLO;
        else
            return EstadoFinanciero.ROJO;
    }

    public String evaluarCompra(double precioJuego, double gastoMes) {
        double restante = presupuestoMensual - gastoMes;
        if (restante < 0) return "No recomendable";
        if (precioJuego <= restante * PORCENTAJE_EVALUAR_RECOMENDADO)
            return "Compra recomendada";
        else if (precioJuego <= restante * PORCENTAJE_EVALUAR_AJUSTADO)
            return "Compra ajustada";
        else if (precioJuego <= restante)
            return "Mejor esperar";
        else
            return "No recomendable";
    }

    public double estimarPrecio(GameInfo juego) {
        if (juego == null || juego.getRating() <= 0) return PRECIO_MEDIO;
        if (juego.getRating() >= 4.5) return PRECIO_ALTO;
        if (juego.getRating() >= 4.0) return PRECIO_MEDIO_ALTO;
        if (juego.getRating() >= 3.5) return PRECIO_MEDIO;
        return PRECIO_BAJO;
    }
}
```

### ViewModels

**GastoViewModel.java** - Gestión de gastos y dashboard:
```java
@HiltViewModel
public class GastoViewModel extends AndroidViewModel {
    private final GastoRepository repository;
    private final LiveData<List<GastoEntity>> listaGastos;
    private final MutableLiveData<EstadoFinancieroUI> estadoUI;
    private final MutableLiveData<List<MonthlyExpense>> monthlyExpenses;
    private final MutableLiveData<TrendResult> trendResult;
    private final LiveData<List<GastoEntity>> recentGastos;

    public void setSistemaFinanciero(SistemaFinanciero sistema) {
        this.sistemaFinanciero = sistema;
        recalcularEstado(listaGastos.getValue());
        loadDashboardData();
    }

    private void loadDashboardData() {
        // Carga totales mensuales y calcula tendencia vs mes anterior
        executorService.execute(() -> {
            List<MonthlyTotal> rawTotals = repository.getMonthlyTotalsSync(3);
            // Procesa y postea a monthlyExpenses y trendResult
        });
    }

    public String getRecomendacionDashboard() {
        return FinancialTrendHelper.generateRecommendation(
            estado.getEstado(), presupuesto, total);
    }

    // CRUD: insertar, actualizar, borrar
}
```

**WishlistViewModel.java** - Wishlist con evaluación financiera reactiva:
```java
@HiltViewModel
public class WishlistViewModel extends AndroidViewModel {
    private final MediatorLiveData<List<WishlistItemUI>> wishlistEvaluada;

    public WishlistViewModel(Application app, WishlistRepository repo,
            UserRepository userRepository, LanzamientoRepository repoLanzamiento,
            GastoRepository gastoRepo) {

        // Combina 3 fuentes de datos: wishlist, presupuesto y gasto actual
        wishlistEvaluada.addSource(wishListSource, lista ->
            recalcularEvaluacion(lista, presupuesto, gasto));
        wishlistEvaluada.addSource(presupuestoSource, presupuesto ->
            recalcularEvaluacion(wishlist, presupuesto, gasto));
        wishlistEvaluada.addSource(gastoSource, gasto ->
            recalcularEvaluacion(wishlist, presupuesto, gasto));
    }

    public void comprarJuego(WishlistEntity juego, double precioFinal) {
        Date fechaLanzamiento = FechaUtils.parseFecha(juego.getFechaLanzamiento());
        if (fechaLanzamiento != null && fechaLanzamiento.after(new Date())) {
            // Es lanzamiento futuro -> guardar en Lanzamientos
            repoLanzamiento.insertar(new LanzamientoEntity(...));
        } else {
            // Ya lanzado -> crear Gasto y eliminar de wishlist
            gastoRepo.insertarGasto(new GastoEntity(...));
            repo.borrar(juego);
        }
    }
}
```

**GamesViewModel.java** - Catálogo de juegos desde RAWG API:
```java
@HiltViewModel
public class GamesViewModel extends ViewModel {
    private final GamesRepository repository;
    private final MutableLiveData<List<GameDto>> juegos;

    public void cargarJuegosRecientes() {
        juegosRecientesLiveData = repository.getRecentlyReleasedGames();
        juegosRecientesObserver = lista -> {
            juegos.setValue(procesarLista(lista));
        };
    }

    private List<GameDto> procesarLista(List<GameDto> lista) {
        for (GameDto juego : lista) {
            // Calcula precio estimado según rating
            double precio = sistema.estimarPrecio(new GameInfo(juego.getName(), juego.getRating()));
            juego.setPrecioEstimado(precio);
            // Genera texto de plataformas
            juego.setPlataformasTexto(extractPlataformas(juego));
        }
        return lista;
    }

    public void buscarJuegosPaginados(String query, boolean reset) {
        // Búsqueda con paginación: reset=true reemplaza, reset=false añade
    }
}
```

### Actividades principales

**MainActivity.java** - Dashboard principal:
```java
@AndroidEntryPoint
public class MainActivity extends BaseNavActivity {
    @Inject UserRepository userRepository;
    @Inject GastoRepository gastoRepo;

    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        inicializarVistas();
        configurarDrawer();
        inicializarViewModel();
        cargarPresupuestoUsuario();
        configurarObservers();  // Observa estado financiero, gastos, tendencias
        observarMoneda();       // LiveData de moneda desde Firestore
        configurarMenu();
        setupBottomNavigation(R.id.nav_home);
    }

    private void configurarObservers() {
        gastoViewModel.getEstadoUI().observe(this, ui -> {
            tvRecomendacion.setText(ui.getMensaje());
            indicadorEstado.setBackgroundColor(getColor(ui.getColorRes()));
        });

        gastoViewModel.getListaGastos().observe(this, gastos -> {
            // Actualiza PieChart y totales
        });

        gastoViewModel.getMonthlyExpenses().observe(this, expenses -> {
            actualizarLineChart(expenses);
        });

        gastoViewModel.getTrendResult().observe(this, trend -> {
            actualizarIndicadorTendencia(trend);
        });
    }
}
```

**ListaJuegosActivity.java** - Catálogo con búsqueda y paginación:
```java
@AndroidEntryPoint
public class ListaJuegosActivity extends BaseNavActivity {
    private static final long DEBOUNCE_DELAY = 500; //ms

    protected void onCreate(Bundle savedInstanceState) {
        // Intenta usar datos precargados desde SplashActivity
        AgenteGamerApplication app = (AgenteGamerApplication) getApplication();
        List<GameDto> preloadedGames = app.getPreloadedGames();
        if (preloadedGames != null && !preloadedGames.isEmpty()) {
            adapter.setLista(preloadedGames);
        }

        // Búsqueda con debounce
        searchView.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, ...) {
                handler.removeCallbacks(searchRunnable);
                searchRunnable = () -> viewModel.buscarJuegosPaginados(texto, true);
                handler.postDelayed(searchRunnable, DEBOUNCE_DELAY);
            }
        });

        // Paginación infinita
        recyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                if (lm.findLastVisibleItemPosition() >= adapter.getItemCount() - 3) {
                    viewModel.buscarJuegosPaginados(queryActual, false);
                }
            }
        });

        // Click en juego -> añadir a wishlist
        adapter.setOnJuegoClickListener((juego, precioEstimado) -> {
            wishlistViewModel.insertar(new WishlistEntity(...));
        });
    }
}
```

**BaseNavActivity.java** - Clase base para navegación inferior:
```java
public abstract class BaseNavActivity extends AppCompatActivity {
    private int mSelectedItemId = -1;

    protected void setupBottomNavigation(int selectedItemId) {
        mSelectedItemId = selectedItemId;
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(selectedItemId);

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == selectedItemId) return true;
            navigateToActivity(item.getItemId());
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resetea la selección al volver de otra actividad
        if (mSelectedItemId != -1) {
            BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
            if (bottomNav != null && bottomNav.getSelectedItemId() != mSelectedItemId) {
                bottomNav.setSelectedItemId(mSelectedItemId);
            }
        }
    }

    private void navigateToActivity(int itemId) {
        // Mapea itemId -> Activity y navega con CLEAR_TOP | SINGLE_TOP
        Intent intent = new Intent(this, targetActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
```

### Utilidades

**MoneyUtils.java** - Formateo y conversión de moneda:
```java
public class MoneyUtils {
    private static final Map<String, Double> EXCHANGE_RATES = new HashMap<>();
    static {
        EXCHANGE_RATES.put("EUR", 1.0);
        EXCHANGE_RATES.put("USD", 1.08);
        EXCHANGE_RATES.put("GBP", 0.86);
    }

    public static String format(double cantidad, String currencyCode) {
        double convertida = convertirDesdeEUR(cantidad, currencyCode);
        NumberFormat formato = NumberFormat.getCurrencyInstance(LOCALE_ES);
        formato.setCurrency(Currency.getInstance(currencyCode));
        return formato.format(convertida);
    }

    public static double convertirDesdeEUR(double montoEUR, String monedaDestino) {
        Double tasa = EXCHANGE_RATES.get(monedaDestino.toUpperCase());
        return tasa != null ? montoEUR * tasa : montoEUR;
    }
}
```

### Inyección de dependencias

**DatabaseModule.java**:
```java
@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {
    @Provides @Singleton
    public static AppDatabase provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "agente_gamer_db")
            .fallbackToDestructiveMigration().build();
    }

    @Provides public static GastoDao provideGastoDao(AppDatabase db) {
        return db.gastoDao();
    }
    @Provides public static WishlistDao provideWishlistDao(AppDatabase db) {
        return db.wishlistDao();
    }
    @Provides public static LanzamientoDao provideLanzamientoDao(AppDatabase db) {
        return db.lanzamientoDao();
    }
    @Provides @Singleton
    public static ExecutorService provideExecutorService() {
        return Executors.newSingleThreadExecutor();
    }
}
```

**RepositoryModule.java**:
```java
@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {
    @Binds @Singleton
    public abstract GastoRepository bindGastoRepository(GastoRepositoryImpl impl);

    @Binds @Singleton
    public abstract WishlistRepository bindWishlistRepository(WishlistRepositoryImpl impl);

    @Binds @Singleton
    public abstract LanzamientoRepository bindLanzamientoRepository(LanzamientoRepositoryImpl impl);

    @Binds @Singleton
    public abstract UserRepository bindUserRepository(UserRepositoryImpl impl);
}
```

### Workers

**SistemaFinancieroWorker.java** - Validación periódica de compras:
```java
@HiltWorker
public class SistemaFinancieroWorker extends Worker {
    public Result doWork() {
        List<WishlistEntity> wishlist = wishlistRepo.getWishlistSync();
        // Obtiene presupuesto y gasto actual de forma asíncrona
        // Valida cada juego contra el presupuesto
        for (WishlistEntity juego : wishlist) {
            String evaluacion = validatePurchaseUseCase.validate(juego, presupuesto, totalGastado);
            if (!evaluacion.equals("Compra recomendada")) {
                lanzarNotificacionNoComprar(juego);
                break;
            }
        }
        // Notifica sobre lanzamientos próximos (0-7 días)
        avisarLanzamientosProximos(wishlist);
        return Result.success();
    }
}
```


\newpage

## 11.- Anexo II - Opinión personal sobre la FCT

Mi experiencia durante las prácticas en empresa ha sido extremadamente enriquecedora y me ha permitido aplicar todos los conocimientos adquiridos durante el ciclo formativo de Desarrollo de Aplicaciones Multiplataforma.

Durante las FCT he podido trabajar con tecnologías y herramientas que solo habíamos visto de forma teórica en clase, como Firebase, Room, Dagger Hilt y APIs REST. La diferencia más notable entre el entorno académico y el profesional es la importancia de las buenas prácticas: arquitectura limpia, separación de responsabilidades, testing y documentación.

Uno de los mayores aprendizajes ha sido comprender la importancia de la planificación. En clase, los proyectos suelen ser más lineales y con requisitos claros. En el mundo real, los requisitos evolucionan, surgen bugs inesperados y hay que saber priorizar. Aprender a gestionar el tiempo y a tomar decisiones técnicas con criterio ha sido fundamental.

También he valorado mucho la experiencia de trabajar con Git en un entorno colaborativo, creando ramas, pull requests y siguiendo un flujo de trabajo estructurado. Esto me ha enseñado la importancia de los commits atómicos, los mensajes descriptivos y la revisión de código.

El proyecto Agente Gamer ha sido el resultado directo de todo este aprendizaje. Ha sido un reto técnico importante integrar múltiples tecnologías (Firebase, Room, Retrofit, MPAndroidChart, Hilt) en una arquitectura coherente, pero el resultado final me enorgullece y demuestra que el ciclo ha cumplido su objetivo: prepararme para el mundo profesional del desarrollo de software.

En resumen, las FCT han sido la pieza que faltaba para conectar la teoría con la práctica, y el proyecto final es la prueba de que estoy preparado para dar el salto al mundo laboral como desarrollador.

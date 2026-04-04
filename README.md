# 🎮 Agente Gamer

**Asistente financiero inteligente para gestionar gastos y compras de videojuegos.**

Agente Gamer es una aplicación Android que ayuda a los jugadores a controlar su presupuesto mensual, registrar gastos en videojuegos y recibir recomendaciones financieras personalizadas. La app sincroniza datos en la nube con Firebase y ofrece una experiencia fluida con preloading de datos durante el arranque.

---

## ✨ Funcionalidades

### Autenticación y Perfil
- Login y registro con **Firebase Auth**
- Perfil de usuario sincronizado en **Firestore**
- Edición de nombre y gestión de presupuesto

### Dashboard Principal
- Resumen financiero: total gastado, presupuesto y restante
- Gráfico de tendencia de gastos (LineChart mensual)
- Gráfico circular de categorías (PieChart)
- Recomendaciones del agente financiero
- Lista de gastos recientes

### Gestión de Gastos
- CRUD completo de gastos con **Room**
- Seguimiento mensual por categoría
- Vista filtrable por fecha

### Catálogo de Juegos
- Integración con **RAWG API** para búsqueda de juegos
- Grid de 2 columnas con paginación
- Detalle de juego en diálogo emergente
- Search con filtros

### Wishlist
- Añadir/eliminar juegos a lista de deseos
- Cálculo de coste total
- Seguimiento de compras

### Lanzamientos
- Juegos próximos con cuenta regresiva
- Precios estimados
- Información de plataforma y género

### Configuración
- Selector de moneda (EUR/USD/GBP)
- Ajuste de presupuesto mensual

---

## 🧱 Arquitectura

Patrón **Clean Architecture** con capas:

```text
com.miapp.agentegamer/
├── data/
│   ├── local/          # Room entities, DAOs, database
│   ├── remote/         # Retrofit API, DTOs
│   ├── repository/     # Implementaciones de repositorio
│   └── worker/         # WorkManager workers
├── domain/
│   ├── model/          # Modelos de dominio
│   └── repository/     # Interfaces de repositorio
├── di/                 # Módulos Hilt
├── ui/
│   ├── splash/         # SplashActivity + preloading
│   ├── auth/           # Login/Register
│   ├── main/           # Dashboard
│   ├── gastos/         # Gestión de gastos
│   ├── games/          # Catálogo de juegos
│   ├── wishlist/       # Lista de deseos
│   ├── lanzamientos/   # Lanzamientos futuros
│   ├── perfil/         # Perfil de usuario
│   ├── ajustes/        # Configuración
│   ├── adapter/        # Adaptadores RecyclerView
│   ├── viewmodel/      # ViewModels + LiveData
│   └── common/         # Utilities y clases base
└── util/               # Helpers (MoneyUtils, etc.)
```

**Patrón MVVM** con ViewModel + LiveData, Repository y Dagger Hilt para inyección de dependencias.

---

## 🛠 Tecnologías

| Categoría | Tecnología |
|-----------|------------|
| **Lenguaje** | Java |
| **Build** | Gradle (Kotlin DSL) |
| **SDK** | minSdk 23, targetSdk 35 |
| **DI** | Dagger Hilt |
| **Auth** | Firebase Auth |
| **DB Cloud** | Firestore |
| **DB Local** | Room |
| **Networking** | Retrofit + Gson |
| **Gráficos** | MPAndroidChart (PieChart, LineChart) |
| **Background** | WorkManager |
| **UI** | Material Components, BottomNavigationView |
| **Splash** | Splash Screen API con preloading |

---

## 🚀 Configuración

### Requisitos previos
- Android Studio (última versión)
- JDK 11+
- Cuenta en [RAWG.io](https://rawg.io/apidocs) para obtener API key

### Variables de entorno
Crea `gradle.properties` en la raíz del proyecto:

```properties
RAWG_API_KEY=tu_api_key_aqui
```

### Ejecución
1. Clonar el repositorio
2. Abrir en Android Studio
3. Sincronizar gradle
4. Build → Rebuild Project
5. Run → Run 'app'

### Firebase Setup
1. Crear proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Añadir aplicación Android con package `com.miapp.agentegamer`
3. Descargar `google-services.json` y colocar en `app/`
4. Habilitar Authentication y Firestore

---

## 📷 Capturas de pantalla

*Próximamente*

---

## 📝 Licencia

Proyecto educativo de uso libre.

---

## ✨ Autor

Javier Lesaca Medina  
Ciclo Formativo de Grado Superior SAFA — DAM  
Proyecto Final — Agente Gamer
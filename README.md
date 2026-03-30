# 🎮🤖 Agente Gamer  
**Un asistente financiero inteligente para gestionar gastos y compras de videojuegos.**

Agente Gamer es una aplicación Android desarrollada en **Java** bajo arquitectura **MVVM**, cuyo objetivo es ayudar a jugadores a gestionar su presupuesto mensual, registrar gastos en videojuegos y recibir recomendaciones financieras mediante un “agente” inteligente.  
El proyecto incluye persistencia local con **Room**, integración con APIs de videojuegos y notificaciones inteligentes.

---

## ✨ Características principales

### 🟢 Fase 1 – Fundamentos (MVP)
- Gestión completa de gastos (CRUD).
- Persistencia local con **Room Database**.
- Pantalla principal con lista de compras.
- Arquitectura **MVVM** modular y escalable.

### 🟡 Fase 2 – Agente financiero simple
- Clase **AgenteFinanciero** con reglas básicas.
- Recomendaciones según presupuesto disponible.
- Dashboard con gráfico circular (MPAndroidChart).
- Indicadores de gasto mensual.

### 🟠 Fase 3 – Integración con APIs
- Implementación de **Retrofit + JSON (Gson/Moshi)**.
- Consulta a API RAWG.io / IGDB.
- Registro de futuros lanzamientos.
- Opción para añadir juegos a la Wishlist.

### 🔵 Fase 4 – Wishlist inteligente + notificaciones
- Cálculo de posibilidad de compra según presupuesto.
- Comparación entre lanzamientos y gasto acumulado.
- Notificaciones inteligentes con **WorkManager**:
  - “Este juego sale en X días…”
  - “Has gastado más del 80% del presupuesto.”

### 🔴 Fase 5 – Extras opcionales (TFG)
- Firebase Auth (login/multiusuario).
- Firestore (sincronización en la nube).
- Reglas predictivas de gasto (IA ligera).
- UI final con Material Design / Jetpack Compose.

---

## 🧱 Arquitectura del proyecto

El proyecto sigue el patrón **MVVM** estándar:
```text
com.miapp.agentegamer/
│
├── data/
│ ├── model/ # Entidades Room
│ ├── dao/ # Interfaces DAO (Room)
│ ├── database/ # AppDatabase (Room)
│ └── repository/ # Capa de repositorio (entre BD y ViewModels)
│
├── ui/
│ ├── main/ # Actividad principal / navegación
│ ├── gastos/ # Pantallas para lista y gestión de gastos
│ ├── lanzamientos/ # Pantallas con datos de las APIs de videojuegos
│ └── wishlist/ # Pantallas de juegos guardados / favoritos
│
└── agent/
└── AgenteFinanciero/ # Lógica del "agente" y sus reglas
```

---

## 🗃 Tecnologías utilizadas

| Tecnología / Librería | Uso |
|-----------------------|-----|
| **Java** | Lenguaje principal |
| **Android Studio** | Entorno de desarrollo |
| **Room** | Base de datos local |
| **LiveData / ViewModel** | MVVM y reactividad |
| **RecyclerView** | Listado de gastos y lanzamientos |
| **Retrofit** | Consumo de API externa |
| **MPAndroidChart** | Gráficos financieros |
| **WorkManager** | Notificaciones programadas |
| **Firebase (opcional)** | Login + nube |
| **Material Design** | UI final |

---

## 🚀 Instalación y ejecución

1. Clonar este repositorio:
   ```bash
   git clone https://github.com/javilesaca/AgenteGamer-App.git
2. Abrir en Android Studio.

3. Reconstruir proyecto:
   ```bash 
   Build → Rebuild Project


4. Ejecutar en un emulador o dispositivo físico:
   ```bash
   Run → Run 'app'

---

## 🧪 Tests y pruebas

    - Pruebas manuales de UI.

    -Verificación de inserción, borrado y actualización en Room.

    -Testeo de las reglas del Agente Financiero.

    -Validación de llamadas a la API.

---

## 📝 Roadmap completo

 Fase 1 – CRUD de gastos + Room

 Fase 2 – Agente financiero + Dashboard

 Fase 3 – Integración con API de videojuegos

 Fase 4 – Wishlist inteligente + notificaciones

 Fase 5 – Extras (Firebase, IA, Compose…)

---

## 📷 Capturas de pantalla


---

## 📚 Licencia

Este proyecto puede usarse libremente para fines educativos.

---

## ✨ Autor

[Javier Lesaca Medina]
Ciclo Formativo de Grado Superior SAFA — Desarrollo de Aplicaciones Multiplataforma (DAM)
Proyecto Final — Agente Gamer

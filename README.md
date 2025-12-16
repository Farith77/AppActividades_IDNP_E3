# 📅 App Actividades IDNP E3

![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-purple?logo=kotlin)
![Android](https://img.shields.io/badge/Platform-Android-green?logo=android)
![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue?logo=jetpackcompose)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange)

Un aplicativo móvil nativo desarrollado en **Android (Kotlin)** para la gestión y organización de tiempo de estudiantes. Permite crear, categorizar y monitorear actividades académicas, personales y laborales, con un sistema de alertas en segundo plano.

---

## 🚀 Características Principales

* **Gestión de Tareas (CRUD):** Crear, Leer, Editar y Eliminar actividades.
* **Categorización:** Clasificación por etiquetas (Universidad, Casa, Trabajo, Otros) con distinción visual de colores.
* **Filtros Inteligentes:**
    * Búsqueda por texto en tiempo real.
    * Filtrado por categorías específicas.
* **Notificaciones y Servicios:**
    * **Foreground Service:** Monitoreo constante de fechas de vencimiento incluso si la app no está en pantalla.
    * **Notificaciones Push:** Alertas cuando una tarea está próxima a vencer.
* **Persistencia de Datos:** Almacenamiento local seguro utilizando **Room Database**.
* **Ordenamiento:** Lista priorizada automáticamente por fecha de cumplimiento.

---

## 🛠️ Stack Tecnológico

El proyecto sigue una arquitectura **MVVM (Model-View-ViewModel)** y Clean Architecture simplificada.

* **Lenguaje:** [Kotlin](https://kotlinlang.org/)
* **Interfaz de Usuario:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
* **Base de Datos:** [Room Database](https://developer.android.com/training/data-storage/room) (SQLite abstraction)
* **Asincronismo:** [Coroutines & Flow](https://kotlinlang.org/docs/coroutines-overview.html)
* **Inyección de Dependencias:** Manual (Service Locator pattern en `Application`).
* **Navegación:** Navigation Compose.
* **Servicios:** Android Foreground Services & Notification Channels.

---

## 📂 Estructura del Proyecto

```text
com.example.app_actividades_idnp_e3
├── data                 # Capa de Datos
│   ├── local            # Room Database (DAO, Entity, DB)
│   └── repository       # Fuente de la verdad (Repository)
├── model                # Modelos de dominio
├── service              # Lógica de segundo plano (Foreground Service, Notifications)
├── ui                   # Capa de Presentación (Jetpack Compose)
│   ├── components       # Composables reutilizables (ActivityItem)
│   ├── navigation       # Grafo de navegación (AppNavigation)
│   ├── screens          # Pantallas (Home, AddActivity)
│   ├── theme            # Tema y estilos
│   └── viewmodel        # Gestión de estado (ActivitiesViewModel)
└── utils                # Utilidades (Date formatting, Constants)

# **Proyecto-Kotlin-App**
*Aplicación Android desarrollada con Kotlin*

---

## 🚀 **Descripción general**
**Proyecto-Kotlin-App** es una aplicación educativa desarrollada en **Kotlin** para Android.  
Su objetivo es demostrar el uso de **Intents implícitos**, **layouts dinámicos**, manejo de **botones interactivos** y recursos dentro de un proyecto Android.

La app permite al usuario realizar varias acciones directamente desde la interfaz, como:
- Abrir Youtube
- Abrir una URL específica
- Realizar una llamada telefónica
- Activar una alarma
- Abrir la cámara
- Acceder a ajustes o funcionalidades personalizadas

Está pensada como un proyecto de aprendizaje para **estudiantes de Android**, mostrando buenas prácticas en la organización de código y recursos.

---

## 🧩 **Características principales**
- **Interfaz amigable y moderna** con diseño limpio
- **GridLayout** para organizar botones de forma clara
- **ImageButtons** con iconos personalizados (`drawable`)
- Uso de **LinearLayout** para botones adicionales
- Manejo de **Intents implícitos** para abrir apps externas (web, cámara, llamadas, alarmas)
- Inclusión de botones de ejemplo como `ACDC` y cámara
- Compatibilidad con **Android Studio** y dispositivos físicos/emuladores

---

## 🎨 **Diseño y Layout**
La app utiliza un diseño **verticalº con LinearLayout** principal, incluyendo:
- **TextView** para título principal
- **GridLayout** con 6 botones principales
- Botones adicionales (Ajustes, Cámara, ACDC) organizados en **LinearLayout horizontal** para mejor alineación
- Uso de `scaleType`, `padding`, y márgenes para una visualización consistente en distintos tamaños de pantalla
- Fondo con **gradient drawable** para mejorar la estética

---

## 📂 **Estructura del proyecto**
```app/
├─ src/
│ ├─ main/
│ │ ├─ java/com/example/sosphone/
│ │ │ └─ MainActivity.kt # Actividad principal
│ │ ├─ res/
│ │ │ ├─ layout/
│ │ │ │ └─ activity_main.xml
│ │ │ ├─ drawable/ # Iconos y fondos
│ │ │ ├─ values/ # Colors, strings, styles
│ │ │ └─ mipmap/ # Iconos del launcher
│ │ └─ AndroidManifest.xml # Declaración de actividades y permisos
├─ build.gradle
├─ gradle.properties
├─ settings.gradle
```

## 🔧 **Requisitos**
- **Android Studio** (recomendado: versión 2022 o superior)
- **Kotlin 1.8+**
- **SDK Android** compatible con la versión mínima configurada (`minSdkVersion`)
- Dispositivo físico con **Depuración USB activada** o emulador

---

## 📲 **Clonar el repo**
git clone https://github.com/Amartinezz23/Proyecto-Kotlin-App.git
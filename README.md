🌌 Diario Espacial
Aplicación móvil Android desarrollada en Kotlin y Jetpack Compose que consume información de la API pública de la NASA y almacena los datos localmente mediante Room Database.

El proyecto permite explorar imágenes astronómicas del día y consultar información sobre asteroides cercanos a la Tierra.

👥 Integrantes del equipo

Integrante	Responsabilidad
Andy Christ Salva Grijalva	Opción de Imágenes (APOD)
Juan Arturo Alvarado Ojeda	Opción de Asteroides (NeoWs)
Jesús Daniel Leon Condori	Arquitectura, integración y documentación


🚀 Tecnologías utilizadas
Kotlin
Android Studio
Jetpack Compose
Navigation Compose
Retrofit
Room Database (SQLite)
Kotlin Coroutines
StateFlow
Coil
NASA Open API
✨ Funcionalidades de la aplicación
🪐 1. Imágenes astronómicas (APOD)
Responsable: Andy Christ Salva Grijalva

Visualización de imágenes astronómicas de los últimos 7 días.
Visualización del título y fecha de cada imagen.
Visualización del detalle de cada imagen.
Visualización de la explicación completa.
Soporte para contenido multimedia de tipo video.
Sistema para marcar y desmarcar imágenes favoritas.
Pantalla de imágenes favoritas.
Almacenamiento de información mediante Room Database.
☄️ 2. Asteroides cercanos a la Tierra (NeoWs)
Responsable: Juan Arturo Alvarado Ojeda

Listado de asteroides cercanos a la Tierra de los últimos 7 días.
Visualización del nombre y fecha de aproximación.
Visualización del diámetro estimado en metros.
Visualización de la velocidad en km/h.
Visualización de la distancia de paso en kilómetros.
Identificación de asteroides potencialmente peligrosos.
Filtro para mostrar únicamente asteroides potencialmente peligrosos.
Pantalla de detalle de cada asteroide.
Sistema para marcar y desmarcar asteroides favoritos.
Pantalla de asteroides favoritos.
Almacenamiento de información mediante Room Database.
🏗️ Arquitectura del proyecto
Responsable: Jesús Daniel Leon Condori

El proyecto utiliza una arquitectura organizada mediante Repository y ViewModel, separando las responsabilidades de acceso a datos, lógica de negocio e interfaz de usuario.

Componentes principales
Data: contiene las entidades, DTO, DAO y configuración de la base de datos.
Remote: administra las solicitudes a la API de NASA mediante Retrofit.
Local: gestiona el almacenamiento local mediante Room Database.
Repository: administra el acceso a los datos remotos y locales.
ViewModel: administra los estados de la aplicación mediante StateFlow.
UI: contiene las interfaces desarrolladas con Jetpack Compose.
Navigation: gestiona la navegación entre las diferentes pantallas.
⚙️ Requisitos para ejecutar el proyecto
Android Studio.
Kotlin.
JDK compatible con la configuración del proyecto.
SDK de Android requerido por el proyecto.
Conexión a Internet.
API Key de NASA.
🔑 Configuración de la API Key de NASA
Para consumir los servicios de la NASA es necesario obtener una API Key personal.

Obtener la API Key
Ingresar a la página oficial: https://api.nasa.gov
Completar el formulario de registro en la sección "Generate API Key".
Registrar un nombre y correo electrónico.
Revisar el correo para obtener la clave de acceso.
Configurar la API Key en Android Studio
Abrir el proyecto en Android Studio.
Ubicar el archivo local.properties en la raíz del proyecto.
Agregar la siguiente propiedad:

properties
NASA_API_KEY=TU_API_KEY
Configurar el acceso a esta propiedad desde BuildConfig en Gradle.
Sincronizar el proyecto.
Importante: El archivo local.properties no debe subirse al repositorio público de GitHub, ya que contiene información privada de configuración.

No utilizar DEMO_KEY durante el desarrollo.

▶️ Instalación y ejecución del proyecto
1. Clonar el repositorio

bash
git clone https://github.com/jalv0750-source/PC2GRUPAL_21101373.git
2. Abrir el proyecto
   Abrir Android Studio.
   Seleccionar Open.
   Buscar la carpeta del proyecto descargado.
   Esperar a que termine la sincronización de Gradle.
3. Configurar la API Key
   Agregar la clave personal de NASA en el archivo local.properties.

4. Ejecutar la aplicación
   Seleccionar un emulador Android o conectar un dispositivo físico.
   Presionar el botón Run en Android Studio.
   Esperar a que la aplicación se instale y ejecute.
   🌿 Organización del repositorio y ramas
   El desarrollo se realizará mediante ramas independientes para cada integrante.


Rama	Responsable	Función
main	Todos	Rama principal protegida
feature/apod	Andy Christ Salva Grijalva	Desarrollo de imágenes
feature/asteroides	Juan Arturo Alvarado Ojeda	Desarrollo de asteroides
feature/arquitectura	Jesús Daniel Leon Condori	Arquitectura e integración
Flujo de trabajo con GitHub
Cada integrante trabaja en su propia rama.
Realiza commits pequeños y descriptivos.
Sube los cambios al repositorio remoto.
Crea un Pull Request hacia la rama main.
Otro integrante revisa el código y realiza un comentario.
Una vez aprobado, se fusiona el Pull Request.
No se permiten commits directos sobre la rama main.

🤝 Coordinación del equipo
La configuración de Retrofit, la clase Database y la navegación principal serán componentes compartidos.

Andy Christ Salva Grijalva: desarrollará completamente la opción Imágenes (APOD), incluyendo consumo de API, persistencia, Repository, ViewModel, pantallas y favoritos.
Juan Arturo Alvarado Ojeda: desarrollará completamente la opción Asteroides (NeoWs), incluyendo consumo de API, persistencia, Repository, ViewModel, pantallas, filtros y favoritos.
Jesús Daniel Leon Condori: desarrollará la arquitectura compartida, incluyendo configuración de Retrofit, Room Database, API Key, navegación principal, integración de los módulos y documentación del proyecto.
Los integrantes coordinarán la integración de sus respectivos módulos para garantizar el correcto funcionamiento de la aplicación.

📸 Capturas de pantalla
En esta sección se agregarán capturas de pantalla de las principales funcionalidades de la aplicación:

Pantalla principal.
Lista de imágenes astronómicas.
Detalle de imágenes.
Imágenes favoritas.
Lista de asteroides.
Detalle de asteroides.
Asteroides favoritos.
Filtro de asteroides potencialmente peligrosos.
📡 Fuente de datos
NASA Open APIs

Página oficial: https://api.nasa.gov

Servicios utilizados:

APOD: https://api.nasa.gov/planetary/apod
NeoWs Feed: https://api.nasa.gov/neo/rest/v1/feed
🎓 Información académica
Curso: Desarrollo de Aplicaciones Móviles

Ciclo: 2026-2

Práctica: Práctica Calificada 2

Proyecto: Diario Espacial

Desarrollado por el equipo de Diario Espacial 🚀
# Análisis técnico del proyecto

> Versión completa del análisis técnico elaborado durante el desarrollo del proyecto. Movido aquí desde `resumen.txt` para mantener el `README.md` enfocado en lo que necesita un visitante nuevo (stack, arquitectura, cómo correr, endpoints, tests).
>
> Documento de referencia técnica, no promocional. Útil para entender decisiones de diseño y la estructura del proyecto en detalle.

## Tabla de contenidos

- [Arquitectura general](#arquitectura-general)
- [Backend (directorio `back/`)](#backend-directorio-back)
- [Frontend (directorio `front/`)](#frontend-directorio-front)
- [Archivos de configuración y construcción](#archivos-de-configuración-y-construcción)
- [Funcionamiento general y flujo de datos](#funcionamiento-general-y-flujo-de-datos)

---

## Arquitectura general

Esta aplicación es una solución completa (Full-Stack) para la gestión de eventos y sus tipos, dividida en dos componentes principales: un Backend desarrollado con Spring Boot (Java) que actúa como API REST, y un Frontend desarrollado con Angular (TypeScript) que proporciona la interfaz de usuario.

* **Backend (Java/Spring Boot):** Proporciona la lógica de negocio, la persistencia de datos y expone una API RESTful para que el frontend interactúe con ella. Utiliza Maven para la gestión de dependencias y construcción.
* **Frontend (Angular):** Es una Single Page Application (SPA) que consume la API del backend para mostrar y gestionar los datos de eventos y tipos de eventos. Utiliza npm para la gestión de paquetes.
* **Base de datos:** Se utiliza una base de datos relacional (H2 en desarrollo, por `import.sql` y `application.properties`, pero fácilmente configurable para otras como PostgreSQL, MySQL, etc.).

---

## Backend (directorio `back/`)

El backend es una aplicación Spring Boot que sigue una arquitectura en capas:

* `BackApplication.java`: Clase principal que arranca la aplicación Spring Boot.
* **Modelos (`modelo/`):**
  * `TipoEvento.java`: Representa un tipo de evento (ej. "Conferencia", "Taller", "Reunión"). Contiene un id, un nombre (único) y una lista de Eventos asociados (`@OneToMany`).
  * `Evento.java`: Representa un evento específico. Contiene un id, nombre, descripcion, fechaInicio, fechaFin y una referencia al TipoEvento al que pertenece (`@ManyToOne`). La relación es obligatoria (`nullable = false`).
* **Repositorios (`repository/`):**
  * `TipoEventoRepository.java`: Interfaz que extiende `JpaRepository` para realizar operaciones CRUD sobre la entidad TipoEvento. Incluye métodos para buscar por nombre.
  * `EventoRepository.java`: Interfaz similar para la entidad Evento.
* **Servicios (`services/`):** Contienen la lógica de negocio principal.
  * `TipoEventoService.java`:
    * Gestiona las operaciones CRUD para TipoEvento.
    * Validaciones clave:
      * Asegura que el nombre del tipo de evento no sea nulo o vacío (`TipoEventoEmptyNameException`).
      * Verifica que no exista otro tipo de evento con el mismo nombre (`TipoEventoExistenteException`).
      * Manejo de borrado: Impide la eliminación de un `TipoEvento` si tiene `Evento`s asociados, lanzando una `TipoEventoConEventosException`. Esto es crucial para mantener la integridad referencial de la base de datos.
  * `EventoService.java`:
    * Gestiona las operaciones CRUD para Evento.
    * Valida la coherencia de la duración del evento contra el rango definido por su `TipoEvento`.
    * Valida solapamiento de horario y lugar contra los eventos existentes (ignorando el propio id en caso de actualización).
* **Controladores (`controller/`):** Exponen los endpoints REST para el frontend.
  * `EventoController.java`: Maneja las peticiones HTTP relacionadas con los eventos (GET, POST, PUT, DELETE).
  * `TipoEventoController.java`: Maneja las peticiones HTTP relacionadas con los tipos de eventos.
* **Manejo de Excepciones (`controlleradvice/`):**
  * `GlobalExceptionHandler.java`: Centraliza el manejo de excepciones en la aplicación, proporcionando respuestas HTTP adecuadas (ej. 400 Bad Request, 404 Not Found) cuando ocurren errores específicos.
  * Excepciones personalizadas: `ConflictoSolapamientoException`, `DuracionEventoInvalidaException`, `DuracionIncoherenteException`, `TipoEventoConEventosException`, `TipoEventoEmptyNameException`, `TipoEventoExistenteException`, `TipoEventoNotFoundException`. Estas excepciones son lanzadas por los servicios y capturadas por el `GlobalExceptionHandler` para enviar mensajes de error claros al cliente.
* **Recursos (`resources/`):**
  * `application.properties`: Configuración base (puerto, datasource, perfil por defecto).
  * `application-dev.properties`: ddl-auto, show-sql, TRACE hibernate logging (perfil dev).
  * `import.sql`: Script SQL que se ejecuta al inicio de la aplicación para precargar datos en la base de datos (útil para desarrollo y pruebas).
* **Pruebas (`test/`):**
  * `BackApplicationTests.java`: Prueba básica de contexto de Spring Boot.
  * `controllerintegrationtest/`: Pruebas de integración para los controladores, asegurando que los endpoints REST funcionan correctamente.

---

## Frontend (directorio `front/`)

El frontend es una aplicación Angular que proporciona la interfaz de usuario:

* `index.html`: Punto de entrada de la aplicación.
* `main.ts`: Archivo principal de arranque de Angular.
* `app/`: Contiene la lógica principal de la aplicación Angular.
  * `app.component.ts`, `.html`, `.css`: Componente raíz de la aplicación.
  * `app.routes.ts`: Define las rutas de navegación de la aplicación (`/home`, `/eventos`, `/tipos-evento`).
  * `app.config.ts`: Configuración de la aplicación Angular.
  * **Componentes (`component/`):**
    * `home/`: Componente de la página de inicio.
    * `navigation/`: Componente para la barra de navegación (menú).
    * `evento-list/`: Muestra una lista de eventos. Permite ver, editar y borrar eventos.
    * `evento-form/`: Formulario para crear o editar un evento. Permite seleccionar el tipo de evento.
    * `tipo-evento/`: Muestra una lista de tipos de eventos. Permite ver, editar y borrar tipos de eventos.
    * `tipo-evento-form/`: Formulario para crear o editar un tipo de evento.
  * **Interfaces (`interface/`):**
    * `evento.ts`: Define la estructura de datos de un evento en TypeScript, mapeando la entidad Evento del backend.
    * `tipo-evento.ts`: Define la estructura de datos de un tipo de evento en TypeScript, mapeando la entidad TipoEvento del backend.
  * **Servicios (`services/`):**
    * `evento.service.ts`: Se encarga de realizar las llamadas HTTP al `EventoController` del backend para obtener, crear, actualizar y borrar eventos.
    * `tipo-evento.service.ts`: Similar al anterior, pero para los tipos de eventos, interactuando con el `TipoEventoController`.
* `styles.css`: Estilos globales de la aplicación.
* `proxy.conf.json`: Configuración para redirigir las peticiones de la API del frontend al backend durante el desarrollo, evitando problemas de CORS.

---

## Archivos de configuración y construcción

* `Dockerfile`: Define cómo construir una imagen Docker de la aplicación (multi-stage para el backend). Permite empaquetar la aplicación y sus dependencias en un contenedor, facilitando su despliegue.
* `Jenkinsfile`: Script para Jenkins, una herramienta de Integración Continua/Despliegue Continuo (CI/CD). Define los pasos para construir, probar y desplegar la aplicación automáticamente.
* `LICENSE`: Apache License 2.0.
* `.gitignore`: Especifica archivos y directorios que Git debe ignorar (ej. `node_modules`, `target/`).
* `.dockerignore`: Excluye archivos del contexto de build de Docker.
* `pom.xml` (en `back/`): Archivo de configuración de Maven para el backend. Define dependencias, plugins de construcción, etc.
* `package.json` (en `front/`): Archivo de configuración de npm para el frontend. Define dependencias de paquetes, scripts para ejecutar la aplicación, pruebas, etc.

---

## Funcionamiento general y flujo de datos

1. El usuario interactúa con la interfaz de usuario del Frontend (Angular).
2. Cuando el usuario realiza una acción (ej. "crear un evento", "listar tipos de evento"), el componente Angular correspondiente llama a su servicio (ej. `evento.service.ts`).
3. El servicio del frontend realiza una petición HTTP (GET, POST, PUT, DELETE) a la API REST expuesta por el Backend.
4. El Backend recibe la petición en el Controller adecuado (ej. `EventoController`).
5. El Controller delega la lógica de negocio al Service correspondiente (ej. `EventoService`).
6. El Service interactúa con el Repository para acceder o modificar los datos en la base de datos.
7. Si ocurre alguna validación o error en el Service, se lanza una excepción personalizada que es capturada por el `GlobalExceptionHandler`, el cual formatea una respuesta de error adecuada para el frontend.
8. Si la operación es exitosa, el Service devuelve los datos al Controller, que a su vez los envía como respuesta HTTP al Frontend.
9. El Frontend recibe la respuesta, actualiza su estado y renderiza los cambios en la interfaz de usuario.

---

## Origen

Documento original: `resumen.txt` en la raíz del repositorio, conservado durante el desarrollo como notas para una presentación del proyecto al cliente del bootcamp. Convertido a Markdown y movido a `docs/ANALYSIS.md` para evitar inflar el `README.md` con prosa no promocional.

# Gestión de Eventos — Spring Boot + Angular

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Angular](https://img.shields.io/badge/Angular-18-DD0031?logo=angular&logoColor=white)](https://angular.io/)
[![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

> Proyecto final del Bootcamp Java · CIC Consulting Informático Cantabria (jul–sep 2025).
> Aplicación fullstack para la gestión de eventos y sus tipos, con API REST en Spring Boot y frontend en Angular.

---

## Tabla de contenidos

- [Stack](#stack)
- [Arquitectura](#arquitectura)
- [Cómo correr local](#cómo-correr-local)
- [Docker](#docker)
- [Endpoints REST](#endpoints-rest)
- [Tests](#tests)
- [CI/CD](#cicd)
- [Anexo · Análisis técnico](#anexo--análisis-técnico)
- [Licencia](#licencia)

---

## Stack

### Backend (`back/`)
| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 17 |
| Framework | Spring Boot 3.5.5 |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos (dev) | H2 en memoria (`jdbc:h2:mem:testdb`) |
| Base de datos (prod) | PostgreSQL (driver incluido, config en `application-prod.properties`) |
| Validación | Jakarta Bean Validation (`@Valid`) |
| Build | Maven 3.x |
| Tests | JUnit 5 (Spring Boot Test starter) |
| Observabilidad | Spring Actuator (`/actuator/health`) |

### Frontend (`front/`)
| Capa | Tecnología |
|------|-----------|
| Framework | Angular 18 |
| Lenguaje | TypeScript |
| HTTP | `HttpClient` contra `/api/*` |
| Build | npm / Angular CLI |
| Proxy dev | `proxy.conf.json` → `http://localhost:8080` |

### Infraestructura
- **Docker** — multi-stage (Maven 3.8 + Eclipse Temurin 17 JRE Alpine, usuario no-root, healthcheck)
- **CI/CD** — `Jenkinsfile` con stages de install (npm + ng build), compile, test (con JaCoCo coverage) y análisis SonarQube
- **.gitignore / .dockerignore** — excluyen `target/`, `node_modules/`, IDE junk, builds, `.env`, OS files

---

## Arquitectura

Monorepo con backend y frontend en el mismo repositorio. Separación física por carpetas, sin acoplamiento de build (Maven y npm son independientes).

```
final/
├── back/                              # Spring Boot
│   ├── src/main/java/es/cic/curso25/back/
│   │   ├── BackApplication.java      # @SpringBootApplication entrypoint
│   │   ├── modelo/                   # JPA entities: Evento, TipoEvento
│   │   ├── repository/               # Spring Data: EventoRepository, TipoEventoRepository
│   │   ├── services/                 # Lógica de negocio: EventoService, TipoEventoService
│   │   ├── controller/               # REST: EventoController, TipoEventoController
│   │   └── controlleradvice/         # @ControllerAdvice + excepciones custom
│   │       ├── GlobalExceptionHandler.java
│   │       ├── TipoEventoEmptyNameException, TipoEventoExistenteException
│   │       ├── TipoEventoConEventosException, TipoEventoNotFoundException
│   │       ├── DuracionIncoherenteException, DuracionEventoInvalidaException
│   │       └── ConflictoSolapamientoException
│   ├── src/main/resources/
│   │   ├── application.properties              # base (H2 + perfil dev)
│   │   ├── application-dev.properties          # ddl-auto, show-sql, TRACE hibernate
│   │   └── import.sql                          # seed de datos para H2
│   ├── src/test/java/...                       # BackApplicationTests + controllerintegrationtest/
│   ├── pom.xml
│   └── Dockerfile (en raíz del repo, multi-stage)
│
├── front/                             # Angular 18
│   ├── src/app/
│   │   ├── component/                 # evento-list, evento-form, tipo-evento, navigation, home
│   │   ├── services/                  # evento.service.ts, tipo-evento.service.ts
│   │   ├── interface/                 # evento.ts, tipo-evento.ts
│   │   └── app.routes.ts
│   ├── proxy.conf.json                # /api/** → http://localhost:8080
│   └── package.json
│
├── docs/
│   └── ANALYSIS.md                    # análisis técnico completo (movido desde resumen.txt)
├── Dockerfile                         # multi-stage del backend
├── Jenkinsfile                        # pipeline CI/CD
├── LICENSE                            # Apache 2.0
├── .gitignore
├── .dockerignore
└── README.md                          # este archivo
```

### Capas del backend

```
HTTP request
   ↓
@RestController (EventoController, TipoEventoController)     ← capa web
   ↓
@Service (EventoService, TipoEventoService)                  ← lógica + validaciones
   ↓
@Repository (EventoRepository, TipoEventoRepository)         ← acceso a datos (JpaRepository)
   ↓
DB (H2 dev / PostgreSQL prod)
```

Las validaciones de negocio viven en los servicios y lanzan **excepciones custom** que `GlobalExceptionHandler` mapea a respuestas HTTP con el código apropiado:

| Excepción | HTTP | Cuándo |
|-----------|------|--------|
| `TipoEventoEmptyNameException` | 400 | nombre nulo o vacío al crear |
| `TipoEventoExistenteException` | 409 | nombre duplicado al crear |
| `TipoEventoConEventosException` | 409 | intento de borrar un tipo con eventos asociados |
| `TipoEventoNotFoundException` | 404 | lookup por id no encuentra la entidad |
| `DuracionIncoherenteException` | 400 | `minima > típica > máxima` |
| `DuracionEventoInvalidaException` | 400 | duración del evento fuera del rango del tipo |
| `ConflictoSolapamientoException` | 409 | solapamiento de horario en el mismo lugar |
| `MethodArgumentNotValidException` | 400 | errores de Bean Validation |

### CORS

`CorsConfig` permite por defecto los orígenes de desarrollo (`http://localhost:4200` para Angular CLI, `http://localhost:5173` para Vite) y se amplía en producción con la variable de entorno `APP_CORS_ORIGINS` (CSV):

```bash
APP_CORS_ORIGINS="https://mi-portfolio.com,https://www.otra.com" java -jar back.jar
```

---

## Cómo correr local

### Pre-requisitos
- Java 17 (`java --version`)
- Maven 3.9+ (`mvn --version`)
- Node 20+ y npm 10+ (`node --version && npm --version`)
- Angular CLI 18 (`npm i -g @angular/cli@18`)

### 1) Backend — Spring Boot

Desde la raíz del repo:

```bash
cd back
mvn clean package -DskipTests          # genera target/back-0.0.1-SNAPSHOT.jar
mvn spring-boot:run                    # arranca en http://localhost:8080
```

Health check: `curl http://localhost:8080/actuator/health` → `{"status":"UP"}`.

Por defecto arranca con perfil `dev` (H2 en memoria + ddl-auto=create-drop + seed con `import.sql`).

### 2) Frontend — Angular

En otra terminal:

```bash
cd front
npm install
ng serve                              # arranca en http://localhost:4200
```

El `proxy.conf.json` redirige `/api/**` al backend en `http://localhost:8080`, evitando CORS en dev.

### 3) Verificar end-to-end

```bash
curl http://localhost:8080/api/evento
curl http://localhost:8080/api/tipo_evento
# desde el navegador: http://localhost:4200
```

---

## Docker

> ⚠️ Este `Dockerfile` solo construye el **backend**. El frontend Angular se sirve por separado (build estático, Vite, o reverse proxy tipo Nginx). Si necesitas unificar, añade un segundo `Dockerfile` en `front/` o un `docker-compose.yml`.

El repo incluye un `Dockerfile` **multi-stage** (Maven builder + JRE Alpine runtime, usuario no-root, healthcheck).

### Build de la imagen

Desde la raíz del repo:

```bash
docker build -t albertoarenaldev/gestion-eventos:1.0 .
```

### Run del contenedor

```bash
docker run -d \
  --name gestion-eventos \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e APP_CORS_ORIGINS="https://tu-portfolio.com" \
  albertoarenaldev/gestion-eventos:1.0
```

Healthcheck: `docker inspect --format '{{ .State.Health.Status }}' gestion-eventos` → `healthy`.

### Variables de entorno relevantes

| Variable | Default | Propósito |
|----------|---------|-----------|
| `SPRING_PROFILES_ACTIVE` | `dev` | Perfil Spring (usa `prod` para H2 → PostgreSQL) |
| `JAVA_OPTS` | `-Xmx512m -Xms256m` | Flags JVM |
| `APP_CORS_ORIGINS` | (vacío) | CSV de orígenes extra permitidos para CORS |

---

## Endpoints REST

Base path: `/api`. Content-Type: `application/json`. Los controllers Spring devuelven el cuerpo con `200 OK` por defecto (no se ha anotado `@ResponseStatus(HttpStatus.CREATED)` en los `POST`, así que la convención REST estricta de `201 Created` no se aplica — la respuesta es funcionalmente equivalente).

### Eventos — `/api/evento`

| Método | Path | Body | Respuesta | Notas |
|--------|------|------|-----------|-------|
| `GET` | `/api/evento` | — | `200` + `List<Evento>` | Lista completa |
| `GET` | `/api/evento/{id}` | — | `200` + `Evento` o `404` | Por id |
| `GET` | `/api/evento?nombre={texto}` | — | `200` + `List<Evento>` | Búsqueda parcial case-insensitive |
| `GET` | `/api/evento/hoy` | — | `200` + `List<Evento>` | Eventos del día actual |
| `POST` | `/api/evento` | `Evento` JSON | `200` + `Evento` | Crea. Valida duración y solapamiento |
| `PUT` | `/api/evento/{id}` | `Evento` JSON | `200` + `Evento` o `404` | Actualiza. Valida solapamiento ignorando el propio id |
| `DELETE` | `/api/evento/{id}` | — | `200` o `404` | Borra |

### Tipos de evento — `/api/tipo_evento`

| Método | Path | Body | Respuesta | Notas |
|--------|------|------|-----------|-------|
| `GET` | `/api/tipo_evento` | — | `200` + `List<TipoEvento>` | Lista con `numeroEventos` calculado |
| `GET` | `/api/tipo_evento/{id}` | — | `200` + `TipoEvento` o `404` | Por id |
| `GET` | `/api/tipo_evento?nombre={texto}` | — | `200` + `List<TipoEvento>` | Búsqueda parcial case-insensitive |
| `POST` | `/api/tipo_evento` | `TipoEvento` JSON | `200` + `TipoEvento` | Crea. Valida unicidad de nombre |
| `PUT` | `/api/tipo_evento/{id}` | `TipoEvento` JSON | `200` + `TipoEvento` o `404` | Actualiza |
| `DELETE` | `/api/tipo_evento/{id}` | — | `200` o `404` | Bloquea si tiene eventos asociados (`409`) |

### Ejemplos con curl

```bash
# Listar tipos de evento
curl http://localhost:8080/api/tipo_evento

# Buscar eventos por nombre
curl 'http://localhost:8080/api/evento?nombre=charla'

# Crear tipo
curl -X POST http://localhost:8080/api/tipo_evento \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Conferencia","duracionMinima":60,"duracionTipica":90,"duracionMaxima":240}'

# Crear evento
curl -X POST http://localhost:8080/api/evento \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Charla Spring","descripcion":"Intro a Spring Boot","lugar":"Aula 1","fechaHora":"2026-09-15T18:00:00","duracionEspecifica":90,"tipoEvento":{"id":1}}'

# Borrar tipo (debe estar sin eventos)
curl -X DELETE http://localhost:8080/api/tipo_evento/1
```

---

## Tests

### Backend (Maven)

```bash
cd back
mvn test                              # 9 tests, ~15s
```

Incluye:
- `BackApplicationTests` — smoke test de carga del contexto Spring
- `controllerintegrationtest/` — tests de integración de los controllers

Para coverage con JaCoCo (lo que usa el Jenkinsfile):

```bash
mvn org.jacoco:jacoco-maven-plugin:0.8.8:prepare-agent test \
    org.jacoco:jacoco-maven-plugin:0.8.8:report
open target/site/jacoco/index.html
```

### Frontend (Karma + Jasmine)

```bash
cd front
npm test
```

### Health check en runtime

```bash
curl http://localhost:8080/actuator/health
# {"status":"UP"}
```

---

## CI/CD

`Jenkinsfile` declara pipeline con 4 stages:

1. **Setup Variables** — sanea el nombre del job para `SONAR_PROJECT_KEY` y `SONAR_PROJECT_NAME`.
2. **Install Dependencies** — `npm install` + `ng build` en la carpeta `front/`.
3. **Compile** — `mvn clean compile` en `back/`.
4. **Test** — `mvn ... jacoco ... test`, archiva reports surefire y publica el HTML de JaCoCo.
5. **SonarQube Analysis** — análisis de calidad con cobertura.

Para correrlo en local sin Jenkins, replica los pasos con `bash`.

---

## Anexo · Análisis técnico

El análisis técnico detallado del proyecto (arquitectura general, capas del backend, frontend, configuración, flujo de datos) está en **[`docs/ANALYSIS.md`](docs/ANALYSIS.md)** para no inflar este README.

Ese documento conserva la prosa completa del análisis original en `resumen.txt` (las notas que escribí durante el desarrollo para una presentación), re-formateada en Markdown. Si te interesa entender el proyecto en detalle o las decisiones de diseño, léelo después de las secciones anteriores.

---

## Licencia

Este proyecto está bajo la **Apache License 2.0** — ver el archivo [LICENSE](LICENSE) para más detalles.

# Sigle-CoreService

Microservicio central del sistema SIGLE. Maneja usuarios, establecimientos de la red asistencial y configuración de notificaciones.

## Stack

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- MySQL
- Lombok

## Requisitos

- Java 17+
- Maven 3.9+
- MySQL con la base de datos `db_coreservice` creada

```sql
CREATE DATABASE db_coreservice;
```

## Configuración

En `src/main/resources/application.properties`:

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/db_coreservice
spring.datasource.username=root
spring.datasource.password=tu_password
```

## Instalación

```bash
mvn clean package -DskipTests
java -jar target/core-service-0.0.1-SNAPSHOT.jar
```

Disponible en `http://localhost:8080`

## Docker

```bash
docker build -t sigle-core-service .
docker run -p 8080:10000 sigle-core-service
```

## Endpoints

### Auth
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/auth/usuario/{firebaseUid}` | Obtener usuario por UID de Firebase |
| PUT | `/api/auth/usuario/{id}/rol` | Cambiar rol del usuario |

### Establecimientos
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/establecimientos` | Listar activos |
| GET | `/api/establecimientos/{id}` | Obtener por ID |
| POST | `/api/establecimientos` | Crear |
| DELETE | `/api/establecimientos/{id}` | Borrado lógico |

### Notificaciones (config)
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/notificaciones/config` | Listar configuraciones activas |
| GET | `/api/notificaciones/config/{id}` | Obtener por ID |
| PUT | `/api/notificaciones/config/{id}/estado` | Activar o desactivar canal |

### Dashboard
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/dashboard/metricas` | Métricas generales |

## Patrones

**Repository:** `UsuarioRepository`, `EstablecimientoRepository` y `NotificacionConfigRepository` extienden `JpaRepository`. Spring genera la implementación automáticamente, sin necesidad de escribir SQL.

**Borrado lógico:** los establecimientos no se eliminan de la BD, se marca `activo = false` para conservar el historial.

## Tests

```bash
mvn test
```

Incluye tests unitarios (Mockito) para `AuthService`, `EstablecimientoService`, `DashboardService` y `NotificacionConfigService`, y tests de integración (`MockMvc`) para los 4 controllers correspondientes, usando H2 en memoria.

### Tests con Docker

```bash
docker build -f Dockerfile.test -t core-tests .
docker run --rm core-tests
```

## Health

```
GET http://localhost:8080/actuator/health
```

## Estructura

```
src/main/java/coreService/
├── config/
├── controller/
├── model/
├── repository/
└── service/
```
# Academia — API de estudiantes

API REST para gestionar estudiantes, desarrollada con Java y Spring Boot. El proyecto aplica DTOs para separar los datos de entrada y salida de las entidades, y organiza la lógica en capas de presentación, negocio y persistencia.

## Tecnologías

- Java 21
- Spring Boot
- Maven
- Spring Web
- Spring Data JPA
- Jakarta Validation
- H2 para ejecución local
- MySQL como alternativa de base de datos

## Requisitos

- JDK 21
- Maven

## Ejecutar el proyecto

Desde la raíz del repositorio:

```bash
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

La configuración predeterminada usa una base de datos H2 en memoria. Los datos se eliminan al detener la aplicación.

## Ejecutar las pruebas

```bash
mvn test
```

## API de estudiantes

Ruta base: `/api/v1/students`

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/v1/students` | Lista estudiantes de forma paginada; acepta un filtro opcional por nombre. |
| `GET` | `/api/v1/students/{id}` | Busca un estudiante por su ID. |
| `POST` | `/api/v1/students` | Crea un estudiante. |
| `PUT` | `/api/v1/students/{id}` | Actualiza los datos de un estudiante. |
| `PATCH` | `/api/v1/students/{id}/status` | Cambia el estado del estudiante. |
| `DELETE` | `/api/v1/students/{id}` | Elimina un estudiante. |

### Crear un estudiante

```bash
curl -X POST http://localhost:8080/api/v1/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ana Pérez",
    "age": 20,
    "email": "ana@example.com",
    "status": "ACTIVE"
  }'
```

Los campos requeridos son `name`, `age`, `email` y `status`. El nombre debe tener entre 2 y 100 caracteres, la edad debe estar entre 16 y 99, y el correo debe tener un formato válido. Los estados permitidos son `ACTIVE` e `INACTIVE`.

### Consultar y filtrar

```bash
# Primera página, con el tamaño predeterminado
curl "http://localhost:8080/api/v1/students"

# Buscar por nombre y solicitar página y tamaño
curl "http://localhost:8080/api/v1/students?name=Ana&page=0&size=10"
```

La API limita el tamaño de página a un máximo de 50 elementos.

### Actualizar un estudiante

`PUT /api/v1/students/{id}` recibe los mismos campos que la creación:

```bash
curl -X PUT http://localhost:8080/api/v1/students/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ana Pérez",
    "age": 21,
    "email": "ana@example.com",
    "status": "ACTIVE"
  }'
```

### Cambiar el estado

```bash
curl -X PATCH http://localhost:8080/api/v1/students/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "INACTIVE"}'
```

### Eliminar un estudiante

```bash
curl -X DELETE http://localhost:8080/api/v1/students/1
```

## Estructura del proyecto

```text
Actividad-Academia-DTO-SpringBoot/
├── src/
│   ├── main/
│   │   ├── java/com/academy/
│   │   │   ├── business/
│   │   │   │   ├── impl/
│   │   │   │   │   └── StudentServiceImpl.java
│   │   │   │   └── service/
│   │   │   │       ├── ServiceResult.java
│   │   │   │       ├── ServiceStatus.java
│   │   │   │       └── StudentService.java
│   │   │   ├── persistence/
│   │   │   │   ├── entity/
│   │   │   │   │   ├── StudentEntity.java
│   │   │   │   │   └── StudentStatus.java
│   │   │   │   └── repository/
│   │   │   │       └── StudentJpaRepository.java
│   │   │   ├── presentation/
│   │   │   │   ├── controller/
│   │   │   │   │   └── StudentController.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── PageResponse.java
│   │   │   │   │   ├── StudentRequest.java
│   │   │   │   │   ├── StudentResponse.java
│   │   │   │   │   └── StudentStatusRequest.java
│   │   │   │   └── mapper/
│   │   │   │       └── StudentMapper.java
│   │   │   └── AcademyApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/academy/
│           └── StudentControllerIntegrationTest.java
└── pom.xml
```

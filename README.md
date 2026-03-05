# Producto API

API REST de ejemplo para consultar y comparar productos. Implementada con Spring Boot 3.2.5, Java 21 y JPA sobre H2 en memoria.

---

## 1. Cómo compilar, levantar el servicio y probarlo

### Prerrequisitos
- Java 21 (JDK)
- Maven 3.9+

### Compilar y ejecutar tests
```bash
mvn clean verify
```

### Levantar el servicio (dev)
- Opción A: usando Maven
```bash
mvn spring-boot:run
```
- Opción B: con el JAR empacado
```bash
mvn clean package -DskipTests
java -jar target/sample-1.0.0.jar
```

### Variables de entorno (opcionales)
El `application.yml` permite sobreescribir propiedades mediante variables de entorno. Las más relevantes son:
- `DB.URL` (por defecto: `jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1`)
- `DB.DRIVER` (por defecto: `org.h2.Driver`)
- `DB.USER` (por defecto: `sa`)
- `DB.PASS` (por defecto: vacía)
- `JPA.DIALECT` (por defecto: `org.hibernate.dialect.H2Dialect`)

Ejemplo (PowerShell):
```powershell
$env:DB_URL="jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"
$env:DB_DRIVER="org.h2.Driver"
$env:DB_USER="sa"
$env:DB_PASS=""
$env:JPA_DIALECT="org.hibernate.dialect.H2Dialect"
mvn spring-boot:run
```

### Base de datos y consola H2
- La BD H2 en memoria se inicializa con `src/main/resources/data.sql`.
- Consola H2 disponible en `http://localhost:8080/h2-console` (usuario `sa`, contraseña vacía si no se redefine).

### HealthCheck

- GET `/actuator/health`
    - HealthCheck.
    - Ejemplo curl:
      ```bash
      curl -s "http://localhost:8080/api/v1/producto-api/actuator/health"
      ```
    - Ejemplo con powershell:
      ```bash
      Invoke-RestMethod `
        -Uri "http://localhost:8080/api/v1/producto-api/actuator/health" `
        -Method GET `
        -ContentType "application/json"
      ```

### Endpoints principales
Base URL: `http://localhost:8080/api/v1/producto-api/productos`

- GET `/productos`
    - Devuelve todos los productos.
    - Ejemplo curl:
      ```bash
      curl -s http://localhost:8080/api/v1/producto-api/productos
      ```
    - Ejemplo con powershell:
      ```bash
      Invoke-RestMethod `
        -Uri "http://localhost:8080/api/v1/producto-api/productos" `
        -Method GET `
        -ContentType "application/json"
      ```

- GET `/productos?ids=1,3`
    - Filtra por IDs.
    - Ejemplo curl:
      ```bash
      curl -s "http://localhost:8080/api/v1/producto-api/productos?ids=1,3"
      ```
    - Ejemplo con powershell:
      ```bash
      Invoke-RestMethod `
        -Uri "http://localhost:8080/api/v1/producto-api/productos?ids=1,3" `
        -Method GET `
        -ContentType "application/json"
      ```

- POST `/productos/compare`
    - Compara productos devolviendo sólo las especificaciones indicadas.
    - Body JSON ejemplo:
      ```json
      {
        "ids": [1, 3],
        "especificaciones": ["color", "marca"]
      }
      ```
    - Ejemplo curl:
      ```bash
      curl -s -X POST http://localhost:8080/api/v1/producto-api/productos/compare \
        -H "Content-Type: application/json" \
        -d '{"ids":[1,3],"especificaciones":["color","marca"]}'
      ```
    - Ejemplo con powershell:
      ```bash
      Invoke-RestMethod `
        -Uri "http://localhost:8080/api/v1/producto-api/productos/compare" `
        -Method POST `
        -ContentType "application/json" `
        -Body '{"ids":[1,3],"especificaciones":["color","marca"]}'
      ```

### Documentación OpenAPI (Swagger UI)
Al levantar la aplicación, la especificación OpenAPI y la UI quedan disponibles en:
- Swagger UI: http://localhost:8080/api/v1/producto-api/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/api/v1/producto-api/v3/api-docs
- OpenAPI YAML: http://localhost:8080/api/v1/producto-api/v3/api-docs.yaml

Para descargar el JSON vía curl:
```bash
curl -s http://localhost:8080/api/v1/producto-api/v3/api-docs -o openapi.json
```

### Probar con tests
```bash
mvn test
```

---

## 2. Estructura del proyecto y explicación de arquitectura

El proyecto sigue una arquitectura por capas con inspiración en DDD y puertos/adaptadores (Hexagonal):

```
producto-api/
├─ src/main/java/com/hackerrank/productoapi
│  ├─ ProductoApiApplication.java                       # Bootstrap de Spring Boot
│  ├─ domain/                                           # Capa de dominio (modelo puro)
│  │  ├─ model/Producto.java                            # Entidad de dominio Producto
│  │  ├─ model/vo/                                      # Value Objects (Nombre, UrlImagen, ...)
│  │  ├─ port/ProductoRepository.java                   # Puerto (interfaz) de persistencia
│  │  └─ command/CompareProductosCommand.java           # Comando para caso de uso de comparación
│  ├─ application/                                      # Capa de aplicación (orquestación)
│  │  └─ usecase/                                       # Casos de uso
│  │     ├─ FindProductosUseCase.java                   # Buscar productos (con o sin filtro de IDs)
│  │     └─ CompareProductosUseCase.java                # Comparar y filtrar especificaciones
│  └─ infrastructure/                                   # Adaptadores de entrada/salida y config
│     ├─ config/OpenApiConfig.java                      # Metadatos OpenAPI (springdoc)
│     ├─ input/rest/                                    # API REST (controladores y modelos de IO)
│     │  ├─ controller/ProductoRestController.java      # Endpoints REST (+ anotaciones OpenAPI)
│     │  ├─ exception/GlobalExceptionHandler.java       # Manejo global de errores (ProblemDetail)
│     │  ├─ mapper/ProductoApiMapper.java               # Mapping dominio <-> respuestas API
│     │  └─ model/{CompareRequest, ProductoResponse}.java
│     └─ output/persistence/                            # Persistencia (JPA/H2)
│        ├─ adapter/
│        │  ├─ ProductoCRUDRepository.java              # Spring Data (CRUD JPA)
│        │  └─ ProductoRepositoryImpl.java              # Implementación del puerto de dominio
│        ├─ mapper/ProductoMapper.java                  # Mapping entidad JPA <-> dominio
│        └─ model/ProductoEntity.java                   # Entidad JPA y colección de especificaciones
├─ src/main/resources/
│  ├─ application.yml                                   # Configuración (context-path, H2, JPA, consola)
│  └─ data.sql                                          # Datos seed para H2
└─ src/test/java/
   ├─ com/hackerrank/productoapi/ProductoApiApplicationTests.java            # Smoke test de contexto
   ├─ com/hackerrank/productoapi/domain/model/vo/*Test.java                  # Tests de Value Objects
   ├─ com/hackerrank/productoapi/application/usecase/*UseCaseTest.java       # Tests de casos de uso
   └─ com/hackerrank/productoapi/infrastructure/input/rest/controller/*Test.java # Tests de controlador (MockMvc)
```

- Dominio: contiene la lógica y el modelo de negocio (entidades y VOs). No depende de frameworks.
- Aplicación: casos de uso que orquestan flujos (p. ej., filtrado de especificaciones), dependen de puertos.
- Infraestructura salida: JPA/H2 implementa `ProductoRepository` (puerto) y mapea entidades a dominio.
- Infraestructura entrada: Controladores REST, modelos de request/response y manejador global de errores para respuestas consistentes.

Flujo típico (GET `/api/productos`):
1) Controller recibe la petición.
2) Invoca `FindProductosUseCase` con el parámetro `ids` (opcional).
3) Caso de uso consulta al puerto `ProductoRepository`.
4) Adaptador de persistencia usa Spring Data JPA sobre H2, mapea con `ProductoMapper` a dominio.
5) `ProductoApiMapper` transforma a `ProductoResponse` y responde JSON.

Flujo de comparación (POST `/api/productos/compare`):
1) Controller valida el `CompareRequest` y lo mapea a `CompareProductosCommand`.
2) `CompareProductosUseCase` recupera productos por ID y filtra el mapa de especificaciones.
3) Se devuelven instancias de dominio con especificaciones reducidas y se mapean a respuesta API.

---

## 3. Decisiones tomadas durante el desarrollo

- Java 21 + Spring Boot 3.2.5: versiones propuestas en el challenge.
- Arquitectura por capas con puertos/adaptadores: desacopla dominio de la infraestructura y facilita pruebas.
- Value Objects en dominio (`Nombre`, `UrlImagen`, `Descripcion`, `Precio`, `Calificacion`): encapsulan validaciones y semántica.
- Persistencia con H2 en memoria para desarrollo y pruebas; inicialización con `data.sql`.
- Manejo de errores uniforme con `ProblemDetail` en `GlobalExceptionHandler`, con `log.error` para trazabilidad.
- Testing con JUnit 5 (El ejercicio proponia JUnit 4) y Spring Boot Test.
- Logging: se registra información de búsqueda en casos de uso y errores en el manejador global; niveles ajustables por configuración.
- Mapeos explícitos: se mantienen mappers dedicados (`ProductoApiMapper`, `ProductoMapper`) para separar concerns entre capas. Lo ideal es usar MapStruct pero el IDE da error.
- Especificaciones en el Producto, es un `Map<String, String>`. Podria ser un `Map<Especificacion, String>`, siendo `Especificacion` un Enum. Esta alternativa es para un tipado mas fuerte

### Posibles mejoras futuras
- Seguridad (Spring Security) para endpoints sensibles.
- Datos de conexion a la Base de datos (Y otros datos sensibles) cifrados.
- Validaciones adicionales en Value Objects y en `CompareRequest`.

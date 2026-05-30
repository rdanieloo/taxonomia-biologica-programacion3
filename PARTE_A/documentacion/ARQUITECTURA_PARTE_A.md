# Arquitectura - Integrante A (Parte A)

## Responsabilidades cubiertas

1. **Interfaz del motor** — `TreeAlgorithmStrategy` (recorridos y consultas)
2. **Motor custom** — `CustomTreeStrategy` + `TreeNode`
3. **API OpenAPI** — `openapi.yaml` + interfaces generadas
4. **Persistencia en memoria** — `MemoryTreeRepository`
5. **Coordinación multimódulo** — `taxonomia-system` (padre Maven)

## Estructura de módulos

```
taxonomia-biologica-programacion3/
├── pom.xml                    # Padre (packaging pom)
├── tree-core/                 # Motor + modelo + repositorio (sin Spring)
│   └── src/main/java/com/grupo/taxonomia/core/
│       ├── model/
│       ├── model/strategy/
│       ├── repository/
│       └── exception/
└── taxonomia-api/             # Spring Boot + REST
    └── src/main/java/com/grupo/taxonomia/taxonomia_api/
        ├── controller/
        ├── core/service/
        ├── config/
        └── dto/
```

### Equivalencia con el diseño de referencia

| Referencia (instrucciones) | Implementación actual |
|----------------------------|------------------------|
| `tree-algorithm` | `tree-core` (paquete `model.strategy`) |
| `tree-storage` | `tree-core` (paquete `repository`) |
| `tree-app` | `taxonomia-api` |

## Flujo de datos

```
Cliente HTTP
    ↓
TreeQueryController (implementa APIs OpenAPI)
    ↓
TreeService
    ├─→ CustomTreeStrategy  (consultas / recorridos)
    └─→ MemoryTreeRepository (mutaciones + estado)
    ↓
TreeNodeMapper → JSON (OpenAPI TreeNodeDTO)
```

## Patrones

| Patrón | Uso |
|--------|-----|
| **Strategy** | `TreeAlgorithmStrategy` / `CustomTreeStrategy` |
| **Repository** | `TreeRepository` / `MemoryTreeRepository` |
| **DTO** | `TreeNodeDTO` (core) + modelo OpenAPI generado |
| **DI (Spring)** | Beans en `AppConfig` según properties |

## Configuración

`taxonomia-api/src/main/resources/application.properties`:

```properties
app.tree-strategy=custom
app.storage=memory
server.port=8084
springdoc.swagger-ui.path=/swagger-ui.html
```

- `app.tree-strategy=custom` → bean `CustomTreeStrategy`
- `app.storage=memory` → bean `MemoryTreeRepository`

## Endpoints REST (12 operaciones)

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/nodes/root?value=` | Crear raíz (201) |
| POST | `/nodes/{parentId}/children?value=` | Agregar hijo (201) |
| GET | `/tree` | Árbol completo |
| GET | `/tree/node/{nodeId}` | Subárbol |
| GET | `/nodes/{nodeId}/path` | Camino desde raíz |
| GET | `/traversal/dfs` | Recorrido DFS |
| GET | `/traversal/bfs` | Recorrido BFS |
| GET | `/tree/height` | Altura |
| GET | `/nodes/{nodeId}/depth` | Profundidad |
| GET | `/nodes/{nodeId}/ancestors` | Ancestros |
| GET | `/tree/validate` | Sin ciclos |
| DELETE | `/nodes/{nodeId}` | Eliminar subárbol (204) |
| PUT | `/nodes/{nodeId}?value=` | Actualizar valor |

Swagger UI: `http://localhost:8084/swagger-ui.html`

## Flujo POST /nodes/root

1. `TreeQueryController.createRoot(value)`
2. `TreeService.createRoot` → `MemoryTreeRepository.createRoot`
3. Se crea `TreeNode` con id autoincremental
4. Respuesta 201 + `TreeNodeDTO` mapeado

## Manejo de errores

`GlobalExceptionHandler`:

| Excepción | HTTP |
|-----------|------|
| `NodeNotFoundException` | 404 |
| `ParentNodeNotFoundException` | 404 |
| `InvalidTreeStateException` | 409 |
| `IllegalArgumentException` | 400 |

## Diferencias de diseño respecto al documento de referencia

1. **Ids `Long`** en lugar de `String` — alineado con taxonomía numérica del dominio.
2. **Strategy sin estado** — recibe `TreeNode root` desde el repositorio; el mapa vive en el repositorio, no en `CustomTreeStrategy`.
3. **OpenAPI first** — interfaces generadas con `openapi-generator-maven-plugin`.
4. **Sin prefijo `/api`** — rutas en raíz del servidor (puerto 8084).
5. **Parámetros query** para `value` en POST/PUT — el YAML actual usa query; se puede migrar a `requestBody` JSON en una iteración futura.

## Integración con otros integrantes

- **B (Collections):** nuevo `@Bean` `TreeAlgorithmStrategy` con `app.tree-strategy=collections`.
- **C (PostgreSQL/Neo4j):** nueva implementación de `TreeRepository` con `app.storage=postgres|neo4j`.

## Cómo ejecutar

```bash
cd taxonomia-biologica-programacion3
mvn clean install
cd taxonomia-api
mvn spring-boot:run
```

# Arquitectura - Parte B

## Responsabilidades

1. **CollectionsTreeStrategy** — motor con mapas JDK  
2. **PostgresTreeRepository** — JDBC + PostgreSQL  
3. **Modelo ER** — `documentacion/ER_DIAGRAM.md`  
4. **Scripts SQL** — `init.sql`, `data.sql`  
5. **Datos de prueba** — taxonomía Animalia → Homo sapiens (9 nodos)

## Módulos

```
tree-core/
  model/strategy/collections/
    NodeData.java
    CollectionsTreeStrategy.java   # TreeRepository + TreeAlgorithmStrategy

taxonomia-api/
  storage/postgres/
    PostgresTreeRepository.java
  resources/db/
    init.sql, data.sql
```

## Flujo collections + memory

```
HTTP → TreeController → TreeService
                          ├─ CollectionsTreeStrategy (repo + algoritmos)
                          └─ respuesta JSON
```

## Flujo collections + postgres

```
HTTP → TreeService
         ├─ PostgresTreeRepository (INSERT/SELECT/DELETE)
         └─ CollectionsTreeStrategy (mapas sincronizados desde getTree())
```

## Beans (AppConfig)

| Condición | Bean |
|-----------|------|
| `custom` (default) | `CustomTreeStrategy` |
| `collections` | `CollectionsTreeStrategy` |
| `memory` + `custom` | `MemoryTreeRepository` |
| `memory` + `collections` | `CollectionsTreeStrategy` como repo |
| `postgres` | `PostgresTreeRepository` |

## Ejecutar

```bash
# Collections en memoria
mvn spring-boot:run -Dspring-boot.run.arguments="--app.tree-strategy=collections"

# PostgreSQL (requiere BD local)
mvn spring-boot:run -Dspring-boot.run.profiles=postgres -Dspring-boot.run.arguments="--app.tree-strategy=collections"
```

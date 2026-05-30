# Integración de estrategias (Parte C)

## Objetivo

Permitir combinar:

- **Motor de algoritmos** (`app.tree-strategy`): `custom` o `collections`
- **Persistencia** (`app.storage`): `memory`, `postgres`, `neo4j`, `mongo`

La API debe comportarse igual (mismos endpoints/contratos) independientemente de la combinación.

## Dónde se resuelve la inyección

Archivo:

- `taxonomia-api/src/main/java/com/grupo/taxonomia/taxonomia_api/config/AppConfig.java`

### Selección de motor

- `custom` → `CustomTreeStrategy` como `TreeAlgorithmStrategy`
- `collections` → `CollectionsTreeStrategy` como `TreeAlgorithmStrategy`

### Selección de repositorio (TreeRepository)

- `memory` + `custom` → `MemoryTreeRepository`
- `memory` + `collections` → `CollectionsTreeStrategy` también implementa `TreeRepository`
- `postgres` → `PostgresTreeRepository` (bean `@Repository` + `@ConditionalOnProperty`)
- `neo4j` → `Neo4jTreeRepository` (bean `@Repository` + `@ConditionalOnProperty`)
- `mongo` → `MongoTreeRepository` (bean `@Repository` + `@ConditionalOnProperty`)

## Integración Collections + Persistencia externa

`CollectionsTreeStrategy` soporta “modo externo”:

- Si `app.tree-strategy=collections` y `app.storage` es `postgres|neo4j|mongo`, `AppConfig` le inyecta el repositorio seleccionado vía:

```java
strategy.setExternalRepository(repo);
```

Entonces, para lecturas (`getTree()`, `findById()`, recorridos, etc.) la strategy reconstruye su mapa desde el árbol persistido.

## Propiedades y perfiles

Base (memoria):

- `taxonomia-api/src/main/resources/application.properties`

Perfiles:

- `application-collections.properties`
- `application-postgres.properties`
- `application-neo4j.properties`
- `application-mongo.properties`

Ejemplos:

```properties
app.tree-strategy=custom
app.storage=memory
```

```properties
app.tree-strategy=collections
app.storage=neo4j
```


# Guía de Pruebas Parte C

## Prerrequisitos

- Java 17
- Maven
- (Opcional) Neo4j corriendo en `bolt://localhost:7687`
- (Opcional) MongoDB corriendo en `mongodb://localhost:27017`

## Ejecutar tests

```bash
mvn test
```

## Ejecutar API

Desde `taxonomia-biologica-programacion3/`:

### Memory + Custom (default)

```bash
mvn -pl taxonomia-api spring-boot:run
```

### Collections + Memory

```bash
mvn -pl taxonomia-api spring-boot:run -Dspring-boot.run.profiles=collections
```

### Storage Postgres

```bash
mvn -pl taxonomia-api spring-boot:run -Dspring-boot.run.profiles=postgres
```

### Storage Neo4j

```bash
mvn -pl taxonomia-api spring-boot:run -Dspring-boot.run.profiles=neo4j
```

### Storage Mongo

```bash
mvn -pl taxonomia-api spring-boot:run -Dspring-boot.run.profiles=mongo
```

Abrir:

- UI: `GET /`
- Swagger: `GET /swagger-ui.html`

## Datos de prueba sugeridos

1. Crear raíz: `Ingeniería en Sistemas`
2. Agregar hijos: `Primer Ciclo`, `Segundo Ciclo`, `Tercer Ciclo`
3. Agregar nietos:
   - bajo `Primer Ciclo`: `Matemática Básica`, `Programación I`
   - bajo `Segundo Ciclo`: `Algoritmos`, `Estructuras de Datos`
   - bajo `Tercer Ciclo`: `Bases de Datos`

## Checklist rápido

- `POST /nodes/root?value=...` crea raíz
- `POST /nodes/{id}/children?value=...` agrega hijo
- `GET /tree` devuelve árbol jerárquico
- `GET /traversal/dfs` y `GET /traversal/bfs` devuelven recorridos consistentes
- `GET /tree/height` devuelve altura esperada
- `DELETE /nodes/{id}` elimina subárbol
- Probar al menos:
  - `custom + memory`
  - `collections + postgres`
  - `collections + neo4j`
  - `collections + mongo`


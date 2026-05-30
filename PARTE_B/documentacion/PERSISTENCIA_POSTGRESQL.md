# Persistencia PostgreSQL

## Tabla `nodes`

```sql
CREATE TABLE nodes (
    id          BIGSERIAL PRIMARY KEY,
    "value"     VARCHAR(255) NOT NULL,
    parent_id   BIGINT REFERENCES nodes(id) ON DELETE CASCADE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

La columna `"value"` va entre comillas porque `value` es palabra reservada en algunos motores (p. ej. H2 en tests).

## Scripts

| Archivo | Ubicación |
|---------|-----------|
| `init.sql` | `taxonomia-api/src/main/resources/db/` |
| `data.sql` | Misma carpeta — 9 nodos, taxonomía 3+ niveles |

## PostgresTreeRepository

- **Tecnología:** `JdbcTemplate` (sin JPA).
- **Bean:** `@ConditionalOnProperty(name = "app.storage", havingValue = "postgres")`.
- **DELETE:** `ON DELETE CASCADE` en FK elimina descendientes.
- **getTree():** carga plana y arma jerarquía en memoria.

### Métodos extra

- `findChildren(Long parentId)`
- `findRoot()`
- `findAllFlat()`
- `clear()` — tests / reset

## Configuración

Perfil `postgres`:

```properties
spring.profiles.active=postgres
app.storage=postgres
```

Archivo: `application-postgres.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taxonomia_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:db/init.sql
spring.sql.init.data-locations=classpath:db/data.sql
```

Crear BD:

```sql
CREATE DATABASE taxonomia_db;
```

## Tests sin Docker

Tests de integración usan **H2 en memoria** (`@JdbcTest`, perfil `test`) con el mismo esquema adaptado.

```bash
cd taxonomia-api
mvn test -Dtest=PostgresTreeRepositoryTest
```

## Combinaciones recomendadas

| Motor | Storage | Uso |
|-------|---------|-----|
| `custom` | `memory` | Parte A (default) |
| `collections` | `memory` | Parte B en RAM |
| `custom` o `collections` | `postgres` | Persistencia real |

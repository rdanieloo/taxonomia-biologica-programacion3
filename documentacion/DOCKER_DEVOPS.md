# Docker y DevOps — Parte D

## Visión general

Docker y Docker Compose garantizan:

- **Reproducibilidad** — mismo entorno en cualquier máquina
- **Aislamiento** — app y BDs en contenedores separados
- **Orquestación** — arranque coordinado con health checks
- **Configuración externa** — motor y persistencia vía `.env`

## Archivos del stack

| Archivo | Propósito |
|---------|-----------|
| `Dockerfile` | Build multi-stage Maven → JRE 17 |
| `docker-compose.yml` | Servicios app, postgres, neo4j, mongodb |
| `.env` / `.env.example` | Variables (no versionar `.env`) |
| `.dockerignore` | Excluir `target/`, `.git`, etc. |
| `init-mongo.sh` | Índices MongoDB al primer arranque |
| `scripts/validate.ps1` / `validate.sh` | Validación automatizada |

## Perfiles Compose

| Componente | Perfil | Uso |
|------------|--------|-----|
| `app` | *(siempre)* | Desarrollo rápido en memoria |
| `postgres`, `neo4j`, `mongodb` | `db` | Persistencias |

```bash
# Solo app
docker compose up --build

# App + todas las BDs
docker compose --profile db up --build

# Solo PostgreSQL + app
docker compose --profile db up postgres app --build
```

## Flujo de arranque

```
docker compose up
    → Cargar .env
    → Crear red taxonomia_network
    → Crear volúmenes (postgres_data, neo4j_data, mongo_data)
    → [perfil db] Health checks de BDs
    → Build / start app (depends_on opcional con required: false)
    → Actuator /actuator/health → UP
    → API en http://localhost:8080
```

## Build multi-stage

**Stage 1** (`maven:3.9-eclipse-temurin-17`):

1. Copia POMs de `tree-core` y `taxonomia-api`
2. `mvn dependency:go-offline`
3. `mvn -pl taxonomia-api -am package -DskipTests`

**Stage 2** (`eclipse-temurin:17-jre-jammy`):

1. Copia `taxonomia-api-*.jar` como `app.jar`
2. Health check con `curl` → `/actuator/health`
3. `java -jar app.jar`

## Variables → Spring Boot

Spring Boot mapea variables de entorno con relaxed binding:

| Variable Docker | Propiedad Spring |
|-----------------|------------------|
| `APP_TREE_STRATEGY` | `app.tree-strategy` |
| `APP_STORAGE` | `app.storage` |
| `SPRING_DATASOURCE_URL` | `spring.datasource.url` |
| `SPRING_NEO4J_URI` | `spring.neo4j.uri` |
| `SPRING_DATA_MONGODB_URI` | `spring.data.mongodb.uri` |
| `SERVER_PORT` | `server.port` |

## Health checks

| Servicio | Prueba |
|----------|--------|
| PostgreSQL | `pg_isready` |
| Neo4j | `cypher-shell RETURN 1` |
| MongoDB | `mongosh` ping con auth |
| App (Dockerfile) | `curl /actuator/health` |

## Inicialización PostgreSQL

Scripts montados en el contenedor:

- `taxonomia-api/src/main/resources/db/init.sql` — esquema
- `taxonomia-api/src/main/resources/db/data.sql` — taxonomía biológica (9 nodos)

## Red y DNS interno

Los servicios se resuelven por nombre en `taxonomia_network`:

- `jdbc:postgresql://postgres:5432/taxonomia_db`
- `bolt://neo4j:7687`
- `mongodb://mongodb:27017/...`

## Limpieza

```bash
docker compose --profile memory --profile db down
docker compose --profile memory --profile db down -v   # + volúmenes
docker image prune
```

## Seguridad

Las credenciales por defecto son **solo para desarrollo**. En producción use secretos gestionados y contraseñas fuertes.

# Integrante C — Neo4j / MongoDB + Integración + Frontend

## Responsabilidades

1. **Neo4j** — `Neo4jTreeRepository` (Cypher / Spring Data Neo4j)
2. **MongoDB** — `MongoTreeRepository`
3. **Selectores Spring** — `AppConfig` con `@ConditionalOnProperty`
4. **Perfiles** — `application-neo4j.properties`, `application-mongo.properties`, `application-collections.properties`
5. **Frontend** — `static/index.html`, `static/app.js`
6. **Datos semilla** — `BiologicalTaxonomyInitializer`
7. **Validación** — 8 combinaciones motor × storage

## Rutas reales

```
taxonomia-api/.../storage/neo4j/Neo4jTreeRepository.java
taxonomia-api/.../storage/mongo/MongoTreeRepository.java
taxonomia-api/.../config/AppConfig.java
taxonomia-api/.../config/BiologicalTaxonomyInitializer.java
taxonomia-api/src/main/resources/static/
├── index.html
└── app.js
```

## Comandos

```bash
mvn -pl taxonomia-api spring-boot:run -Dspring-boot.run.profiles=neo4j
mvn -pl taxonomia-api spring-boot:run -Dspring-boot.run.profiles=mongo
# UI: http://localhost:8084/index.html
```

## Coordinación

- PR revisado por **A** (contratos API) y **B** (Postgres/collections)

## Rama sugerida

```bash
git checkout -b feature/C-neo4j-mongo-frontend
```

## Enlaces

- [CHECKLIST_C.md](./CHECKLIST_C.md)
- [ARCHIVOS_C_ENTREGAR.md](./ARCHIVOS_C_ENTREGAR.md)
- [Frontend](./frontend/)
- [Ejemplos](./ejemplos/)
- [Documentación](./documentacion/)

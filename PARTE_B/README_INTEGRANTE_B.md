# Integrante B — Motor Collections + PostgreSQL

## Responsabilidades

1. **Motor Collections** — `CollectionsTreeStrategy` (HashMap, Stack, Queue)
2. **Persistencia PostgreSQL** — `PostgresTreeRepository` (JDBC)
3. **Modelo ER** — tabla `nodes` autorreferenciada
4. **Scripts SQL** — `init.sql` + `data.sql` (taxonomía biológica, 9 nodos)
5. **Perfil Spring** — `application-postgres.properties`
6. **Tests** — integración con H2 o PostgreSQL

## Rutas reales

```
tree-core/.../strategy/collections/
├── CollectionsTreeStrategy.java
└── NodeData.java

taxonomia-api/.../storage/postgres/
└── PostgresTreeRepository.java

taxonomia-api/src/main/resources/
├── db/init.sql
├── db/data.sql
└── application-postgres.properties
```

## Coordinación

- Revisar con **A** antes de cambiar `TreeRepository` o DTOs compartidos
- `AppConfig` debe registrar beans cuando `app.tree-strategy=collections` y `app.storage=postgres`

## Comandos

```bash
# Perfil postgres (BD local taxonomia_db)
cd taxonomia-api
mvn spring-boot:run -Dspring-boot.run.profiles=postgres

# Tests
mvn test -Dtest=PostgresTreeRepositoryTest
```

## Rama sugerida

```bash
git checkout -b feature/B-collections-postgres
```

## Enlaces

- [CHECKLIST_B.md](./CHECKLIST_B.md)
- [ARCHIVOS_B_ENTREGAR.md](./ARCHIVOS_B_ENTREGAR.md)
- [SQL](./sql/)
- [Ejemplos](./ejemplos/)
- [Documentación](./documentacion/)

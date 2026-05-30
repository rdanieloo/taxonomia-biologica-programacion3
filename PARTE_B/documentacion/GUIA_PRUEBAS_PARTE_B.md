# Guía de Pruebas - Parte B

## 1. Tests automatizados

```bash
cd taxonomia-biologica-programacion3
mvn clean install
```

- `CollectionsTreeStrategyTest` — DFS, BFS, altura, ancestros, delete  
- `PostgresTreeRepositoryTest` — JDBC con H2  

## 2. Collections en memoria

```properties
app.tree-strategy=collections
app.storage=memory
```

```bash
cd taxonomia-api
mvn spring-boot:run -Dspring-boot.run.arguments="--app.tree-strategy=collections --app.storage=memory"
```

```bash
curl -X POST "http://localhost:8084/nodes/root?value=CEO"
curl -X POST "http://localhost:8084/nodes/1/children?value=VP"
curl "http://localhost:8084/traversal/dfs"
curl "http://localhost:8084/tree/height"
```

## 3. PostgreSQL

1. Crear BD `taxonomia_db`  
2. Ajustar usuario/clave en `application-postgres.properties`  
3. Ejecutar:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgres -Dspring-boot.run.arguments="--app.tree-strategy=collections"
```

4. Verificar datos semilla:

```sql
SELECT id, "value", parent_id FROM nodes ORDER BY id;
```

Debe mostrar 9 filas desde `Animalia` hasta `Homo sapiens` e `Insecta`.

## 4. Checklist

- [ ] DFS/BFS correctos con collections  
- [ ] Altura 0 para solo raíz  
- [ ] Postgres inserta raíz e hijos  
- [ ] FK rechaza padre inexistente  
- [ ] DELETE en cascada  
- [ ] `mvn clean install` verde  

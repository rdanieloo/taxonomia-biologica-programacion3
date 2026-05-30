# Persistencia Neo4j (Parte C)

## Alcance

Implementación de persistencia NoSQL en Neo4j para el árbol de taxonomía, activable por configuración:

- `app.storage=neo4j`
- Clase principal: `taxonomia-api/src/main/java/com/grupo/taxonomia/taxonomia_api/storage/neo4j/Neo4jTreeRepository.java`

## Modelo de datos (Neo4j)

- **Etiqueta**: `:Node`
- **Propiedades**:
  - `id` (Long)
  - `value` (String)
- **Relación**:
  - `(:Node)-[:CHILD]->(:Node)` (padre → hijo)

## Queries (Cypher) usadas

### Crear raíz (validando unicidad)

- Detectar si existe raíz (nodo sin `CHILD` entrante):

```cypher
MATCH (n:Node)
WHERE NOT (n)<-[:CHILD]-()
RETURN count(n) as c
```

- Crear raíz:

```cypher
CREATE (n:Node {id: $id, value: $value})
```

### Agregar hijo

```cypher
MATCH (p:Node {id: $parentId})
CREATE (c:Node {id: $childId, value: $value})
CREATE (p)-[:CHILD]->(c)
```

### Obtener árbol completo (flat) y reconstruir jerarquía en Java

```cypher
MATCH (n:Node)
OPTIONAL MATCH (p:Node)-[:CHILD]->(n)
RETURN n.id as id, n.value as value, p.id as parentId
ORDER BY id
```

En Java se reconstruye el árbol con un `Map<id, TreeNode>` igual que en `PostgresTreeRepository`.

### Update

```cypher
MATCH (n:Node {id: $id})
SET n.value = $value
RETURN count(n) as c
```

### Delete en cascada (nodo + descendientes)

```cypher
MATCH (n:Node {id: $id})
OPTIONAL MATCH (n)-[:CHILD*0..]->(d:Node)
WITH collect(distinct d) as ds
UNWIND ds as x
DETACH DELETE x
```

## Generación de IDs

Para demo y simplicidad, el repo calcula el siguiente id en Neo4j:

```cypher
MATCH (n:Node) RETURN coalesce(max(n.id), 0) as maxId
```

Luego usa `maxId + 1`. Esto **no** está diseñado para alta concurrencia; es suficiente para validación del proyecto.

## Configuración

Archivo de perfil:

- `taxonomia-api/src/main/resources/application-neo4j.properties`

Propiedades:

```properties
app.storage=neo4j
spring.neo4j.uri=bolt://localhost:7687
spring.neo4j.authentication.username=neo4j
spring.neo4j.authentication.password=password
```

Activación:

```bash
mvn -pl taxonomia-api spring-boot:run -Dspring-boot.run.profiles=neo4j
```


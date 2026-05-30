# Arquitectura Parte C

## Responsabilidades cubiertas

- Persistencia NoSQL **Neo4j** y **MongoDB**
- Integración por configuración con `@ConditionalOnProperty`
- Strategy seleccionable (`custom` / `collections`)
- Frontend básico en `static/`
- Validación de build y tests

## Módulos

- `tree-core`
  - `TreeRepository` (contrato de mutación y acceso al árbol)
  - `TreeAlgorithmStrategy` (recorridos y consultas)
  - `TreeNode` (modelo)
- `taxonomia-api`
  - REST API (OpenAPI generator + controller)
  - Configuración de beans (`AppConfig`)
  - Persistencias: `postgres`, `neo4j`, `mongo`
  - Frontend estático

## Flujo de una petición (ejemplo)

### POST `/nodes/{parentId}/children?value=X`

1. `TreeQueryController.addChild(...)`
2. `TreeService.addChild(parentId, value)`
3. `TreeRepository.addChild(parentId, value)` (según `app.storage`)
4. Respuesta como `TreeNodeDTO` (mapper OpenAPI)

### GET `/traversal/dfs`

1. Controller → Service
2. Service pide `TreeRepository.getTree()`
3. Motor (`TreeAlgorithmStrategy`) ejecuta DFS sobre el árbol retornado
4. Respuesta lista de `TreeNodeDTO`

## Selección de implementación (Conditional)

- `app.storage` activa **exactamente 1** implementación de `TreeRepository`:
  - memory/postgres/neo4j/mongo
- `app.tree-strategy` activa **exactamente 1** implementación de `TreeAlgorithmStrategy`:
  - custom/collections

## Persistencia NoSQL

### Neo4j

- Modelo: nodos `:Node` con relación `:CHILD`
- Repository: `Neo4jTreeRepository` (usa `Neo4jClient`)

### MongoDB

- Colección: `nodes` con `_id`, `value`, `parentId`
- Repository: `MongoTreeRepository` (usa `MongoTemplate`)


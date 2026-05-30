# Persistencia MongoDB (Parte C)

## Alcance

Implementación de persistencia NoSQL en MongoDB para el árbol de taxonomía, activable por configuración:

- `app.storage=mongo`
- Clase principal: `taxonomia-api/src/main/java/com/grupo/taxonomia/taxonomia_api/storage/mongo/MongoTreeRepository.java`

## Modelo de datos (MongoDB)

- **Colección**: `nodes`
- **Documento**:
  - `_id` (Long) → id del nodo
  - `value` (String) → valor
  - `parentId` (Long | null) → referencia al padre por id

No se usan documentos anidados; la jerarquía se reconstruye en memoria para `getTree()`.

## Operaciones

### Crear raíz (validando unicidad)

Se valida que no exista un documento con `parentId = null`:

- `count({ parentId: null })`

Luego se inserta:

```json
{ "_id": 1, "value": "Raíz", "parentId": null }
```

### Agregar hijo

- Verifica existencia del padre por `_id`
- Inserta documento con `parentId = <idPadre>`

### Obtener árbol completo

- `findAll()` sobre `nodes`
- Reconstrucción en Java usando `Map<id, TreeNode>` (idéntica idea a Postgres/Neo4j)

### Update

- `updateFirst({_id: nodeId}, {$set: {value: newValue}})`

### Delete en cascada

Como los hijos solo tienen `parentId`, se realiza:

- carga de todos los documentos
- cálculo de descendientes por BFS usando `parentId`
- `remove({_id: {$in: [...]}})`

## Generación de IDs

Para demo y simplicidad:

- Se obtiene el mayor `_id` y se usa `max + 1` (orden descendente por `_id`, limit 1).

Esto **no** está diseñado para alta concurrencia; es suficiente para validación del proyecto.

## Configuración

Archivo de perfil:

- `taxonomia-api/src/main/resources/application-mongo.properties`

Propiedades:

```properties
app.storage=mongo
spring.data.mongodb.uri=mongodb://localhost:27017/taxonomia_db
```

Activación:

```bash
mvn -pl taxonomia-api spring-boot:run -Dspring-boot.run.profiles=mongo
```


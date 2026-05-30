# Frontend (Parte C)

## Ubicación

- `taxonomia-api/src/main/resources/static/index.html`
- `taxonomia-api/src/main/resources/static/app.js`

Spring Boot lo sirve automáticamente en:

- `GET /` → `index.html`

## Propósito

Interfaz mínima para:

- Crear raíz
- Agregar hijos
- Eliminar nodo (y su subárbol)
- Actualizar valor
- Consultar árbol completo
- Ejecutar DFS / BFS
- Calcular altura
- Validar ciclos

## Endpoints usados (según `openapi.yaml`)

- `POST /nodes/root?value=...`
- `POST /nodes/{parentId}/children?value=...`
- `GET /tree`
- `DELETE /nodes/{nodeId}`
- `PUT /nodes/{nodeId}?value=...`
- `GET /traversal/dfs`
- `GET /traversal/bfs`
- `GET /tree/height`
- `GET /tree/validate`

## Notas

- Los selectores “Motor” y “Persistencia” son **informativos**: cambiar esos valores requiere editar properties y reiniciar.
- La UI refresca los selectores de nodos con `GET /tree` y un `flattenTree()` (preorder) para poblar combos.


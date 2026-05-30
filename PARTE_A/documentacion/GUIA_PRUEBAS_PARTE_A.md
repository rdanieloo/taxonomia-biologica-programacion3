# Guía de Pruebas - Parte A

Servidor base: `http://localhost:8084`

## Caso 1: Árbol simple (taxonomía biológica)

```bash
# Crear raíz
curl -X POST "http://localhost:8084/nodes/root?value=Animalia"

# Hijo
curl -X POST "http://localhost:8084/nodes/1/children?value=Chordata"

# Nieto
curl -X POST "http://localhost:8084/nodes/2/children?value=Mammalia"
```

| Prueba | Comando | Resultado esperado |
|--------|---------|-------------------|
| Árbol completo | `curl http://localhost:8084/tree` | JSON jerárquico 1→2→3 |
| DFS | `curl http://localhost:8084/traversal/dfs` | Animalia, Chordata, Mammalia |
| BFS | `curl http://localhost:8084/traversal/bfs` | Mismo orden (cadena lineal) |
| Altura | `curl http://localhost:8084/tree/height` | `2` |
| Profundidad nodo 3 | `curl http://localhost:8084/nodes/3/depth` | `2` |
| Camino | `curl http://localhost:8084/nodes/3/path` | 3 nodos raíz→hoja |
| Ancestros | `curl http://localhost:8084/nodes/3/ancestors` | Chordata, Animalia |
| Validar | `curl http://localhost:8084/tree/validate` | `true` |

## Caso 2: Árbol con ramas

```
        Animalia (1)
         /         \
   Chordata (2)  Arthropoda (3)
      /  \           /
  Mammalia Aves  Insecta
   (4)    (5)      (6)
```

```bash
curl -X POST "http://localhost:8084/nodes/root?value=Animalia"
curl -X POST "http://localhost:8084/nodes/1/children?value=Chordata"
curl -X POST "http://localhost:8084/nodes/1/children?value=Arthropoda"
curl -X POST "http://localhost:8084/nodes/2/children?value=Mammalia"
curl -X POST "http://localhost:8084/nodes/2/children?value=Aves"
curl -X POST "http://localhost:8084/nodes/3/children?value=Insecta"
```

- **DFS:** Animalia, Chordata, Mammalia, Aves, Arthropoda, Insecta
- **BFS:** Animalia, Chordata, Arthropoda, Mammalia, Aves, Insecta
- **Altura:** `2`

## Caso 3: Errores

### Raíz duplicada

```bash
curl -X POST "http://localhost:8084/nodes/root?value=Raíz1"
curl -X POST "http://localhost:8084/nodes/root?value=Raíz2"
```

→ **409** con `{"code":"INVALID_STATE",...}`

### Padre inexistente

```bash
curl -X POST "http://localhost:8084/nodes/999/children?value=Hijo"
```

→ **404** `NOT_FOUND`

### Eliminar nodo

```bash
curl -X DELETE "http://localhost:8084/nodes/2"
```

→ **204** (elimina Chordata y descendientes)

### Actualizar valor

```bash
curl -X PUT "http://localhost:8084/nodes/3?value=NuevoNombre"
```

→ **200** `true`

## Pruebas unitarias (sin servidor)

```bash
cd taxonomia-biologica-programacion3/tree-core
mvn test
```

Incluye `CustomTreeStrategyTest` y `MemoryTreeRepositoryTest`.

## Prueba de contexto Spring

```bash
cd taxonomia-api
mvn test
```

## Checklist final

- [ ] Crear raíz (201)
- [ ] Agregar hijos
- [ ] DFS / BFS correctos
- [ ] Altura y profundidad
- [ ] Path y ancestros
- [ ] Validación sin ciclos
- [ ] DELETE y PUT
- [ ] Errores 404 / 409 / 400
- [ ] Swagger en `/swagger-ui.html`
- [ ] `mvn test` en tree-core

# Motor Collections - Documentación

## Descripción

`CollectionsTreeStrategy` implementa el árbol con estructuras del JDK:

| Estructura | Uso |
|------------|-----|
| `HashMap<Long, NodeData>` | Nodos por id — O(1) |
| `HashMap<Long, List<Long>>` | Hijos por padre — O(1) |
| `HashMap<Long, Long>` | Padre por hijo — O(1) |
| `ArrayDeque` | BFS |
| `Deque` (push/pop) | DFS iterativo |
| `HashSet` | Detección de ciclos |

## NodeData

Solo `id` y `value`. Las relaciones viven en los mapas.

## Algoritmos

### DFS (Stack)

1. `push(rootId)`
2. Mientras el stack no esté vacío: `pop` → procesar → `push` hijos en orden inverso

Complejidad: tiempo O(n), espacio O(h).

### BFS (ArrayDeque)

1. `offer(rootId)`
2. Mientras la cola no esté vacía: `poll` → procesar → `offer` hijos

Complejidad: tiempo O(n), espacio O(w).

### Altura / profundidad / ancestros / camino

- **Altura:** recursión sobre `children` (hoja = 0).
- **Profundidad:** subir con `parents` hasta la raíz.
- **Ancestros:** `parents` sin incluir el nodo consultado.
- **Camino:** insertar al inicio mientras se sube por `parents`.

### Ciclos

DFS con `HashSet`; si un id se repite en la misma exploración → ciclo.

## Configuración

```properties
app.tree-strategy=collections
app.storage=memory
```

Con `collections` + `memory`, la misma instancia actúa como `TreeRepository` y `TreeAlgorithmStrategy`.

Con `collections` + `postgres`, el repositorio JDBC persiste y la strategy **sincroniza** los mapas desde `getTree()` antes de cada consulta.

## Pruebas

```bash
cd tree-core
mvn test -Dtest=CollectionsTreeStrategyTest
```

## Comparación con Custom

| Aspecto | Collections | Custom |
|---------|-------------|--------|
| Estructura | Mapas implícitos | Nodos explícitos |
| DFS | Stack de ids | Recursión en `TreeNode` |
| Mutaciones | En mapas | En `MemoryTreeRepository` o en la misma clase |

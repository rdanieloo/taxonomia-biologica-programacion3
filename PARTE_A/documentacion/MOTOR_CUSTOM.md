# Motor Custom - Documentación Completa

## Descripción General

El motor custom (`CustomTreeStrategy`) implementa recorridos y consultas sobre árboles jerárquicos usando:

- Estructura de nodos explícitos (`TreeNode`)
- Referencias padre-hijo en memoria
- Solo APIs del JDK (`java.util`)
- Sin librerías externas de algoritmos

La **mutación** del árbol (crear raíz, agregar hijo, eliminar, actualizar) vive en `MemoryTreeRepository`, que mantiene un `ConcurrentHashMap<Long, TreeNode>` para búsqueda O(1) por id.

## Clase TreeNode

### Atributos

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `id` | `Long` | Identificador único |
| `value` | `String` | Contenido del nodo |
| `parent` | `TreeNode` | Padre (`null` si es raíz) |
| `children` | `List<TreeNode>` | Hijos directos |

### Métodos principales

- `addChild(TreeNode)` — enlaza padre/hijo
- `removeChild(TreeNode)` — desvincula hijo
- `isRoot()` / `isLeaf()` — utilidades
- `equals` / `hashCode` — basados en `id`
- `Serializable` — intercambio entre capas

## CustomTreeStrategy

### DFS (Depth-First Search)

```
1. Visitar nodo actual (pre-order)
2. Recursión sobre cada hijo
```

**Ejemplo:**

```
    1 (Animalia)
   / \
  2   3
 / \
4   5

DFS: [1, 2, 4, 5, 3]
```

- Tiempo: O(n)
- Espacio: O(h) — profundidad / pila de recursión

### BFS (Breadth-First Search)

```
1. Cola FIFO con la raíz
2. Mientras la cola no esté vacía:
   - extraer nodo
   - encolar hijos
```

**Mismo árbol:** `BFS: [1, 2, 3, 4, 5]`

- Tiempo: O(n)
- Espacio: O(w) — ancho máximo

### Altura

- Árbol vacío (`null`): **0**
- Solo raíz: **0**
- Raíz + un nivel de hijos: **1**

Se calcula como el máximo de las alturas de los hijos + 1, con base en hoja/raíz sola = 0.

### Profundidad

- Raíz: **0**
- Cada nivel descendiente: +1
- Nodo inexistente: **-1**

### Ancestros

Lista del padre inmediato hasta la raíz, **sin** incluir el nodo consultado.

Ejemplo: nodo `4` → `[2, 1]`

### Camino desde raíz (`getPathToNode`)

Lista inclusiva raíz → … → nodo destino.

Ejemplo: nodo `4` → `[1, 2, 4]`

### Validación de ciclos

DFS con lista de ids visitados. Si un id se repite en la misma rama, hay ciclo.

En un árbol bien formado (una raíz, sin enlaces hacia arriba indebidos) retorna `true`.

## MemoryTreeRepository

| Operación | Comportamiento |
|-----------|----------------|
| `createRoot` | Id secuencial; lanza `InvalidTreeStateException` si ya hay raíz |
| `addChild` | Lanza `ParentNodeNotFoundException` si el padre no existe |
| `deleteNode` | Elimina el nodo y todo su subárbol del mapa |
| `updateNode` | Cambia `value`; valida no vacío |

## Ejemplo de uso (programático)

```java
MemoryTreeRepository repo = new MemoryTreeRepository();
CustomTreeStrategy strategy = new CustomTreeStrategy();

TreeNode root = repo.createRoot("Animalia");
TreeNode chordata = repo.addChild(root.getId(), "Chordata");
TreeNode mammalia = repo.addChild(chordata.getId(), "Mammalia");

strategy.dfs(root);           // pre-order
strategy.calculateHeight(root); // 2
strategy.getPathToNode(root, mammalia.getId()); // [1, 2, 3]
```

## Pruebas automatizadas

- `CustomTreeStrategyTest` — DFS, BFS, altura, profundidad, ancestros, camino, ciclos
- `MemoryTreeRepositoryTest` — raíz, hijos, errores, delete, update

Ejecutar:

```bash
cd tree-core && mvn test
```

## Limitaciones

- Una sola raíz por instancia del repositorio
- Ids numéricos (`Long`), no UUID
- Estado en RAM (volátil)
- Escrituras concurrentes: mapa thread-safe; estructura de nodos no está sincronizada a nivel de árbol completo

## Comparación con motor Collections (Integrante B)

| Aspecto | Custom | Collections (pendiente) |
|---------|--------|-------------------------|
| Estructura | Nodos explícitos | Estructuras JDK |
| Búsqueda por id | O(1) con mapa | Depende de implementación |
| Mutaciones | En repositorio | Por definir |
| DFS/BFS | Recursión / cola manual | Por definir |

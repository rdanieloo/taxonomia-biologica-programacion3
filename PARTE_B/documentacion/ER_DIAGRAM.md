# Modelo Entidad-Relación — Tabla `nodes`

## Diagrama lógico

```
┌─────────────────────────────────────────┐
│                 nodes                    │
├─────────────────┬───────────────────────┤
│ id (PK)         │ BIGSERIAL             │
│ value           │ VARCHAR(255) NOT NULL │
│ parent_id (FK)  │ BIGINT → nodes(id)    │
│ created_at      │ TIMESTAMP             │
└─────────────────┴───────────────────────┘
         │
         │ autorreferencia 1:N
         ▼
   parent_id NULL = raíz
```

## Relaciones

- Un nodo tiene **como máximo un padre** (`parent_id`).
- Un nodo puede tener **varios hijos** (filas con `parent_id` = su `id`).
- Solo una raíz por árbol lógico (`parent_id IS NULL`).
- `ON DELETE CASCADE`: al eliminar un nodo se eliminan sus descendientes.

## Índices

| Índice | Columna | Propósito |
|--------|---------|-----------|
| PK | `id` | Búsqueda por id |
| `idx_nodes_parent_id` | `parent_id` | Listar hijos rápidamente |

## Collation

UTF-8 (por defecto en PostgreSQL).

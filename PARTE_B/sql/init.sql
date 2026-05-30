-- Esquema PostgreSQL: árbol jerárquico (taxonomía biológica)
CREATE TABLE IF NOT EXISTS nodes (
    id          BIGSERIAL PRIMARY KEY,
    "value"     VARCHAR(255) NOT NULL,
    parent_id   BIGINT REFERENCES nodes(id) ON DELETE CASCADE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_nodes_parent_id ON nodes(parent_id);

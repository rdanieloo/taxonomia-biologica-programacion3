package com.grupo.taxonomia.taxonomia_api.storage.neo4j;

import com.grupo.taxonomia.core.exception.InvalidTreeStateException;
import com.grupo.taxonomia.core.exception.NodeNotFoundException;
import com.grupo.taxonomia.core.exception.ParentNodeNotFoundException;
import com.grupo.taxonomia.core.model.TreeNode;
import com.grupo.taxonomia.core.repository.TreeRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataAccessException;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "app.storage", havingValue = "neo4j")
public class Neo4jTreeRepository implements TreeRepository {

    private final Neo4jClient neo4jClient;

    public Neo4jTreeRepository(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    @Override
    @Transactional
    public TreeNode createRoot(String value) {
        validateValue(value);
        try {
            long roots = neo4jClient.query(
                            "MATCH (n:Node) WHERE NOT (n)<-[:CHILD]-() RETURN count(n) as c")
                    .fetchAs(Long.class)
                    .one()
                    .orElse(0L);
            if (roots > 0) {
                throw new InvalidTreeStateException("La raíz del árbol ya existe");
            }

            long id = nextId();
            neo4jClient.query("CREATE (n:Node {id: $id, value: $value})")
                    .bind(id).to("id")
                    .bind(value).to("value")
                    .run();
            return new TreeNode(id, value);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error creando raíz en Neo4j: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public TreeNode addChild(Long parentId, String value) {
        validateValue(value);
        try {
            long parentCount = neo4jClient.query("MATCH (p:Node {id: $parentId}) RETURN count(p) as c")
                    .bind(parentId).to("parentId")
                    .fetchAs(Long.class)
                    .one()
                    .orElse(0L);
            if (parentCount == 0) {
                throw new ParentNodeNotFoundException(parentId);
            }

            long childId = nextId();
            neo4jClient.query(
                            "MATCH (p:Node {id: $parentId}) " +
                                    "CREATE (c:Node {id: $childId, value: $value}) " +
                                    "CREATE (p)-[:CHILD]->(c)")
                    .bind(parentId).to("parentId")
                    .bind(childId).to("childId")
                    .bind(value).to("value")
                    .run();
            return new TreeNode(childId, value);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error agregando hijo en Neo4j: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<TreeNode> findById(Long id) {
        try {
            return neo4jClient.query("MATCH (n:Node {id: $id}) RETURN n.id as id, n.value as value")
                    .bind(id).to("id")
                    .fetchAs(TreeNode.class)
                    .mappedBy((typeSystem, record) ->
                            new TreeNode(record.get("id").asLong(), record.get("value").asString()))
                    .one();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error buscando nodo en Neo4j: " + e.getMessage(), e);
        }
    }

    @Override
    public TreeNode getTree() {
        try {
            var rows = neo4jClient.query(
                            "MATCH (n:Node) " +
                                    "OPTIONAL MATCH (p:Node)-[:CHILD]->(n) " +
                                    "RETURN n.id as id, n.value as value, p.id as parentId " +
                                    "ORDER BY id")
                    .fetchAs(NodeRow.class)
                    .mappedBy((typeSystem, record) ->
                            new NodeRow(
                                    record.get("id").asLong(),
                                    record.get("value").asString(),
                                    record.get("parentId").isNull() ? null : record.get("parentId").asLong()
                            ))
                    .all();

            return buildHierarchy(List.copyOf(rows));
        } catch (DataAccessException e) {
            throw new RuntimeException("Error obteniendo árbol desde Neo4j: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean deleteNode(Long nodeId) {
        try {
            if (findById(nodeId).isEmpty()) {
                throw new NodeNotFoundException(nodeId);
            }

            neo4jClient.query(
                            "MATCH (n:Node {id: $id}) " +
                                    "OPTIONAL MATCH (n)-[:CHILD*0..]->(d:Node) " +
                                    "WITH collect(distinct d) as ds " +
                                    "UNWIND ds as x " +
                                    "DETACH DELETE x")
                    .bind(nodeId).to("id")
                    .run();
            return true;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error eliminando nodo en Neo4j: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean updateNode(Long nodeId, String newValue) {
        validateValue(newValue);
        try {
            long updated = neo4jClient.query(
                            "MATCH (n:Node {id: $id}) SET n.value = $value RETURN count(n) as c")
                    .bind(nodeId).to("id")
                    .bind(newValue).to("value")
                    .fetchAs(Long.class)
                    .one()
                    .orElse(0L);
            if (updated == 0) {
                throw new NodeNotFoundException(nodeId);
            }
            return true;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error actualizando nodo en Neo4j: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void clear() {
        try {
            neo4jClient.query("MATCH (n:Node) DETACH DELETE n").run();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error limpiando Neo4j: " + e.getMessage(), e);
        }
    }

    public List<TreeNode> findChildren(Long parentId) {
        try {
            var children = neo4jClient.query(
                            "MATCH (p:Node {id: $parentId})-[:CHILD]->(c:Node) " +
                                    "RETURN c.id as id, c.value as value ORDER BY id")
                    .bind(parentId).to("parentId")
                    .fetchAs(TreeNode.class)
                    .mappedBy((typeSystem, record) -> new TreeNode(record.get("id").asLong(), record.get("value").asString()))
                    .all();
            return List.copyOf(children);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error buscando hijos en Neo4j: " + e.getMessage(), e);
        }
    }

    public Optional<TreeNode> findParent(Long childId) {
        try {
            return neo4jClient.query(
                            "MATCH (c:Node {id: $childId})<-[:CHILD]-(p:Node) " +
                                    "RETURN p.id as id, p.value as value")
                    .bind(childId).to("childId")
                    .fetchAs(TreeNode.class)
                    .mappedBy((typeSystem, record) -> new TreeNode(record.get("id").asLong(), record.get("value").asString()))
                    .one();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error buscando padre en Neo4j: " + e.getMessage(), e);
        }
    }

    public Optional<TreeNode> findRoot() {
        try {
            return neo4jClient.query(
                            "MATCH (n:Node) WHERE NOT (n)<-[:CHILD]-() " +
                                    "RETURN n.id as id, n.value as value LIMIT 1")
                    .fetchAs(TreeNode.class)
                    .mappedBy((typeSystem, record) -> new TreeNode(record.get("id").asLong(), record.get("value").asString()))
                    .one();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error buscando raíz en Neo4j: " + e.getMessage(), e);
        }
    }

    private long nextId() {
        return neo4jClient.query("MATCH (n:Node) RETURN coalesce(max(n.id), 0) as maxId")
                .fetchAs(Long.class)
                .one()
                .map(max -> max + 1)
                .orElse(1L);
    }

    private static TreeNode buildHierarchy(List<NodeRow> rows) {
        if (rows.isEmpty()) {
            return null;
        }

        Map<Long, TreeNode> byId = new HashMap<>();
        for (NodeRow row : rows) {
            byId.put(row.id(), new TreeNode(row.id(), row.value()));
        }

        TreeNode root = null;
        for (NodeRow row : rows) {
            TreeNode node = byId.get(row.id());
            if (row.parentId() == null) {
                root = node;
            } else {
                TreeNode parent = byId.get(row.parentId());
                if (parent != null) {
                    parent.addChild(node);
                }
            }
        }
        return root;
    }

    private static void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El valor no puede estar vacío");
        }
    }

    private record NodeRow(Long id, String value, Long parentId) {
    }
}


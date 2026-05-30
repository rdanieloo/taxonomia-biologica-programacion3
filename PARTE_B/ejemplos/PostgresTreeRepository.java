package com.grupo.taxonomia.taxonomia_api.storage.postgres;

import com.grupo.taxonomia.core.exception.InvalidTreeStateException;
import com.grupo.taxonomia.core.exception.NodeNotFoundException;
import com.grupo.taxonomia.core.exception.ParentNodeNotFoundException;
import com.grupo.taxonomia.core.model.TreeNode;
import com.grupo.taxonomia.core.repository.TreeRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "app.storage", havingValue = "postgres")
public class PostgresTreeRepository implements TreeRepository {

    private static final String INSERT =
            "INSERT INTO nodes (\"value\", parent_id) VALUES (?, ?)";
    private static final String SELECT_BY_ID =
            "SELECT id, \"value\", parent_id FROM nodes WHERE id = ?";
    private static final String SELECT_ALL =
            "SELECT id, \"value\", parent_id FROM nodes ORDER BY id";
    private static final String UPDATE_VALUE =
            "UPDATE nodes SET \"value\" = ? WHERE id = ?";
    private static final String DELETE_BY_ID =
            "DELETE FROM nodes WHERE id = ?";
    private static final String COUNT_ROOTS =
            "SELECT COUNT(*) FROM nodes WHERE parent_id IS NULL";
    private static final String SELECT_CHILDREN =
            "SELECT id, \"value\", parent_id FROM nodes WHERE parent_id = ?";
    private static final String SELECT_ROOT =
            "SELECT id, \"value\", parent_id FROM nodes WHERE parent_id IS NULL LIMIT 1";

    private final JdbcTemplate jdbcTemplate;

    public PostgresTreeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public TreeNode createRoot(String value) {
        validateValue(value);
        Integer roots = jdbcTemplate.queryForObject(COUNT_ROOTS, Integer.class);
        if (roots != null && roots > 0) {
            throw new InvalidTreeStateException("La raíz del árbol ya existe");
        }
        return insertNode(value, null);
    }

    @Override
    @Transactional
    public TreeNode addChild(Long parentId, String value) {
        validateValue(value);
        if (findById(parentId).isEmpty()) {
            throw new ParentNodeNotFoundException(parentId);
        }
        return insertNode(value, parentId);
    }

    @Override
    public Optional<TreeNode> findById(Long id) {
        try {
            NodeRow row = jdbcTemplate.queryForObject(SELECT_BY_ID, nodeRowMapper(), id);
            return Optional.of(toShallowNode(row));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public TreeNode getTree() {
        List<NodeRow> rows = jdbcTemplate.query(SELECT_ALL, nodeRowMapper());
        return buildHierarchy(rows);
    }

    @Override
    @Transactional
    public boolean deleteNode(Long nodeId) {
        if (findById(nodeId).isEmpty()) {
            throw new NodeNotFoundException(nodeId);
        }
        jdbcTemplate.update(DELETE_BY_ID, nodeId);
        return true;
    }

    @Override
    @Transactional
    public boolean updateNode(Long nodeId, String newValue) {
        validateValue(newValue);
        int updated = jdbcTemplate.update(UPDATE_VALUE, newValue, nodeId);
        if (updated == 0) {
            throw new NodeNotFoundException(nodeId);
        }
        return true;
    }

    @Transactional
    public void clear() {
        jdbcTemplate.update("DELETE FROM nodes");
        jdbcTemplate.execute("ALTER SEQUENCE IF EXISTS nodes_id_seq RESTART WITH 1");
    }

    public List<TreeNode> findChildren(Long parentId) {
        return jdbcTemplate.query(SELECT_CHILDREN, nodeRowMapper(), parentId).stream()
                .map(this::toShallowNode)
                .toList();
    }

    public Optional<TreeNode> findRoot() {
        try {
            NodeRow row = jdbcTemplate.queryForObject(SELECT_ROOT, nodeRowMapper());
            return Optional.of(toShallowNode(row));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<TreeNode> findAllFlat() {
        return jdbcTemplate.query(SELECT_ALL, nodeRowMapper()).stream()
                .map(this::toShallowNode)
                .toList();
    }

    private TreeNode insertNode(String value, Long parentId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, value);
            if (parentId == null) {
                ps.setObject(2, null);
            } else {
                ps.setLong(2, parentId);
            }
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("No se pudo obtener el id generado");
        }
        return new TreeNode(key.longValue(), value);
    }

    private TreeNode buildHierarchy(List<NodeRow> rows) {
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

    private TreeNode toShallowNode(NodeRow row) {
        return new TreeNode(row.id(), row.value());
    }

    private static RowMapper<NodeRow> nodeRowMapper() {
        return (rs, rowNum) -> new NodeRow(
                rs.getLong("id"),
                rs.getString("value"), // columna "value" en SQL
                rs.getObject("parent_id") != null ? rs.getLong("parent_id") : null
        );
    }

    private static void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El valor no puede estar vacío");
        }
    }

    private record NodeRow(Long id, String value, Long parentId) {
    }
}

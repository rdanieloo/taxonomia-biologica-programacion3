package com.grupo.taxonomia.taxonomia_api.storage.mongo;

import com.grupo.taxonomia.core.exception.InvalidTreeStateException;
import com.grupo.taxonomia.core.exception.NodeNotFoundException;
import com.grupo.taxonomia.core.exception.ParentNodeNotFoundException;
import com.grupo.taxonomia.core.model.TreeNode;
import com.grupo.taxonomia.core.repository.TreeRepository;
import org.bson.Document;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
@ConditionalOnProperty(name = "app.storage", havingValue = "mongo")
public class MongoTreeRepository implements TreeRepository {

    private static final String COLLECTION = "nodes";

    private final MongoTemplate mongoTemplate;

    public MongoTreeRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @Transactional
    public TreeNode createRoot(String value) {
        validateValue(value);
        try {
            long roots = mongoTemplate.count(new Query(Criteria.where("parentId").is(null)), COLLECTION);
            if (roots > 0) {
                throw new InvalidTreeStateException("La raíz del árbol ya existe");
            }

            long id = nextId();
            Document doc = new Document("_id", id)
                    .append("value", value)
                    .append("parentId", null);
            mongoTemplate.insert(doc, COLLECTION);
            return new TreeNode(id, value);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error creando raíz en MongoDB: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public TreeNode addChild(Long parentId, String value) {
        validateValue(value);
        try {
            if (findById(parentId).isEmpty()) {
                throw new ParentNodeNotFoundException(parentId);
            }

            long id = nextId();
            Document doc = new Document("_id", id)
                    .append("value", value)
                    .append("parentId", parentId);
            mongoTemplate.insert(doc, COLLECTION);
            return new TreeNode(id, value);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error agregando hijo en MongoDB: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<TreeNode> findById(Long id) {
        try {
            Query q = new Query(Criteria.where("_id").is(id));
            Document doc = mongoTemplate.findOne(q, Document.class, COLLECTION);
            if (doc == null) {
                return Optional.empty();
            }
            return Optional.of(new TreeNode(doc.getLong("_id"), doc.getString("value")));
        } catch (DataAccessException e) {
            throw new RuntimeException("Error buscando nodo en MongoDB: " + e.getMessage(), e);
        }
    }

    @Override
    public TreeNode getTree() {
        try {
            List<Document> docs = mongoTemplate.findAll(Document.class, COLLECTION);
            return buildHierarchy(docs);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error obteniendo árbol desde MongoDB: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean deleteNode(Long nodeId) {
        try {
            if (findById(nodeId).isEmpty()) {
                throw new NodeNotFoundException(nodeId);
            }

            List<Document> docs = mongoTemplate.findAll(Document.class, COLLECTION);
            Set<Long> toDelete = collectDescendants(nodeId, docs);
            Query q = new Query(Criteria.where("_id").in(toDelete));
            mongoTemplate.remove(q, COLLECTION);
            return true;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error eliminando nodo en MongoDB: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean updateNode(Long nodeId, String newValue) {
        validateValue(newValue);
        try {
            Query q = new Query(Criteria.where("_id").is(nodeId));
            Update u = new Update().set("value", newValue);
            var res = mongoTemplate.updateFirst(q, u, COLLECTION);
            if (res.getMatchedCount() == 0) {
                throw new NodeNotFoundException(nodeId);
            }
            return true;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error actualizando nodo en MongoDB: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void clear() {
        try {
            mongoTemplate.dropCollection(COLLECTION);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error limpiando MongoDB: " + e.getMessage(), e);
        }
    }

    public List<TreeNode> findChildren(Long parentId) {
        try {
            Query q = new Query(Criteria.where("parentId").is(parentId));
            List<Document> docs = mongoTemplate.find(q, Document.class, COLLECTION);
            return docs.stream()
                    .map(d -> new TreeNode(d.getLong("_id"), d.getString("value")))
                    .toList();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error buscando hijos en MongoDB: " + e.getMessage(), e);
        }
    }

    public Optional<TreeNode> findParent(Long childId) {
        try {
            Query q = new Query(Criteria.where("_id").is(childId));
            Document child = mongoTemplate.findOne(q, Document.class, COLLECTION);
            if (child == null || child.get("parentId") == null) {
                return Optional.empty();
            }
            return findById(((Number) child.get("parentId")).longValue());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error buscando padre en MongoDB: " + e.getMessage(), e);
        }
    }

    public Optional<TreeNode> findRoot() {
        try {
            Query q = new Query(Criteria.where("parentId").is(null)).limit(1);
            Document doc = mongoTemplate.findOne(q, Document.class, COLLECTION);
            if (doc == null) {
                return Optional.empty();
            }
            return Optional.of(new TreeNode(doc.getLong("_id"), doc.getString("value")));
        } catch (DataAccessException e) {
            throw new RuntimeException("Error buscando raíz en MongoDB: " + e.getMessage(), e);
        }
    }

    private long nextId() {
        Query q = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        Document doc = mongoTemplate.findOne(q, Document.class, COLLECTION);
        if (doc == null) {
            return 1L;
        }
        return doc.getLong("_id") + 1;
    }

    private static TreeNode buildHierarchy(List<Document> docs) {
        if (docs == null || docs.isEmpty()) {
            return null;
        }

        Map<Long, TreeNode> byId = new HashMap<>();
        Map<Long, Long> parentById = new HashMap<>();

        for (Document doc : docs) {
            Long id = doc.getLong("_id");
            String value = doc.getString("value");
            byId.put(id, new TreeNode(id, value));
            Object parentObj = doc.get("parentId");
            if (parentObj != null) {
                parentById.put(id, ((Number) parentObj).longValue());
            }
        }

        TreeNode root = null;
        for (Map.Entry<Long, TreeNode> entry : byId.entrySet()) {
            Long id = entry.getKey();
            TreeNode node = entry.getValue();
            Long parentId = parentById.get(id);
            if (parentId == null) {
                root = node;
            } else {
                TreeNode parent = byId.get(parentId);
                if (parent != null) {
                    parent.addChild(node);
                }
            }
        }
        return root;
    }

    private static Set<Long> collectDescendants(Long nodeId, List<Document> docs) {
        Map<Long, Set<Long>> childrenByParent = new HashMap<>();
        for (Document doc : docs) {
            Object parentObj = doc.get("parentId");
            if (parentObj == null) {
                continue;
            }
            long parentId = ((Number) parentObj).longValue();
            long id = doc.getLong("_id");
            childrenByParent.computeIfAbsent(parentId, k -> new HashSet<>()).add(id);
        }

        Set<Long> result = new HashSet<>();
        ArrayDeque<Long> queue = new ArrayDeque<>();
        queue.add(nodeId);
        while (!queue.isEmpty()) {
            Long current = queue.poll();
            if (!result.add(current)) {
                continue;
            }
            for (Long child : childrenByParent.getOrDefault(current, Set.of())) {
                queue.add(child);
            }
        }
        return result;
    }

    private static void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El valor no puede estar vacío");
        }
    }
}


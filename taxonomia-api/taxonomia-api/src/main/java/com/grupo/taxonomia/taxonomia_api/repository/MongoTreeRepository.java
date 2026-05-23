package com.grupo.taxonomia.taxonomia_api.repository;

import com.grupo.taxonomia.core.model.TreeNode;
import com.grupo.taxonomia.core.repository.TreeRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

public class MongoTreeRepository implements TreeRepository {

    private final MongoTemplate mongoTemplate;
    private static final String COLLECTION = "taxa";

    public MongoTreeRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public TreeNode createRoot(String value) {
        mongoTemplate.dropCollection(COLLECTION);

        MongoTaxonDoc doc = new MongoTaxonDoc();
        doc.setId(1L);
        doc.setValue(value);
        doc.setParentId(null);
        mongoTemplate.save(doc, COLLECTION);

        return new TreeNode(1L, value, null);
    }

    @Override
    public TreeNode addChild(Long parentId, String value) {
        List<MongoTaxonDoc> all = mongoTemplate.findAll(MongoTaxonDoc.class, COLLECTION);

        Long newId = all.stream()
                .mapToLong(MongoTaxonDoc::getId)
                .max()
                .orElse(0L) + 1;

        MongoTaxonDoc doc = new MongoTaxonDoc();
        doc.setId(newId);
        doc.setValue(value);
        doc.setParentId(parentId);
        mongoTemplate.save(doc, COLLECTION);

        return findById(newId).orElseThrow();
    }

    @Override
    public Optional<TreeNode> findById(Long id) {
        return searchInTree(getTree(), id);
    }

    @Override
    public TreeNode getTree() {
        List<MongoTaxonDoc> all = mongoTemplate.findAll(MongoTaxonDoc.class, COLLECTION);
        if (all.isEmpty()) return null;

        Map<Long, TreeNode> nodeMap = new HashMap<>();
        for (MongoTaxonDoc doc : all) {
            nodeMap.put(doc.getId(), new TreeNode(doc.getId(), doc.getValue(), null));
        }

        TreeNode root = null;
        for (MongoTaxonDoc doc : all) {
            TreeNode node = nodeMap.get(doc.getId());
            if (doc.getParentId() == null) {
                root = node;
            } else {
                TreeNode parent = nodeMap.get(doc.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }
        return root;
    }

    private Optional<TreeNode> searchInTree(TreeNode node, Long id) {
        if (node == null) return Optional.empty();
        if (node.getId().equals(id)) return Optional.of(node);
        for (TreeNode child : node.getChildren()) {
            Optional<TreeNode> result = searchInTree(child, id);
            if (result.isPresent()) return result;
        }
        return Optional.empty();
    }
}

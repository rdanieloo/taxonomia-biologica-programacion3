package com.grupo.taxonomia.core.repository.memory;

import com.grupo.taxonomia.core.model.TreeNode;
import com.grupo.taxonomia.core.repository.TreeRepository;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class MemoryTreeRepository implements TreeRepository {

    private final Map<Long, TreeNode> nodes = new HashMap<>();
    private Long idSequence = 1L;
    private TreeNode root;

    @Override
    public TreeNode createRoot(String value) {
        if (root != null) {
            throw new IllegalStateException("Root already exists");
        }

        TreeNode node = new TreeNode(idSequence++, value, null);
        root = node;
        nodes.put(node.getId(), node);
        return node;
    }

    @Override
    public TreeNode addChild(Long parentId, String value) {
        TreeNode parent = nodes.get(parentId);

        if (parent == null) {
            throw new IllegalArgumentException("Parent not found");
        }

        TreeNode child = new TreeNode(idSequence++, value, parent);
        parent.getChildren().add(child);
        nodes.put(child.getId(), child);

        return child;
    }

    @Override
    public Optional<TreeNode> findById(Long id) {
        return Optional.ofNullable(nodes.get(id));
    }

    @Override
    public TreeNode getTree() {
        return root;
    }
}
package com.grupo.taxonomia.core.repository.memory;

import com.grupo.taxonomia.core.exception.InvalidTreeStateException;
import com.grupo.taxonomia.core.exception.NodeNotFoundException;
import com.grupo.taxonomia.core.exception.ParentNodeNotFoundException;
import com.grupo.taxonomia.core.model.TreeNode;
import com.grupo.taxonomia.core.repository.TreeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryTreeRepository implements TreeRepository {

    private final Map<Long, TreeNode> nodes = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);
    private volatile TreeNode root;

    @Override
    public TreeNode createRoot(String value) {
        if (root != null) {
            throw new InvalidTreeStateException("La raíz del árbol ya existe");
        }
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El valor no puede estar vacío");
        }

        TreeNode node = new TreeNode(idSequence.incrementAndGet(), value);
        root = node;
        nodes.put(node.getId(), node);
        return node;
    }

    @Override
    public TreeNode addChild(Long parentId, String value) {
        TreeNode parent = nodes.get(parentId);
        if (parent == null) {
            throw new ParentNodeNotFoundException(parentId);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El valor no puede estar vacío");
        }

        TreeNode child = new TreeNode(idSequence.incrementAndGet(), value);
        parent.addChild(child);
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

    @Override
    public boolean deleteNode(Long nodeId) {
        TreeNode node = nodes.get(nodeId);
        if (node == null) {
            throw new NodeNotFoundException(nodeId);
        }

        List<Long> toRemove = new ArrayList<>();
        collectIds(node, toRemove);

        if (node.getParent() != null) {
            node.getParent().removeChild(node);
        } else {
            root = null;
        }

        toRemove.forEach(nodes::remove);
        return true;
    }

    @Override
    public boolean updateNode(Long nodeId, String newValue) {
        TreeNode node = nodes.get(nodeId);
        if (node == null) {
            throw new NodeNotFoundException(nodeId);
        }
        if (newValue == null || newValue.isBlank()) {
            throw new IllegalArgumentException("El valor no puede estar vacío");
        }
        node.setValue(newValue);
        return true;
    }

    private void collectIds(TreeNode node, List<Long> ids) {
        ids.add(node.getId());
        for (TreeNode child : node.getChildren()) {
            collectIds(child, ids);
        }
    }
}

package com.grupo.taxonomia.core.model.strategy.collections;

import com.grupo.taxonomia.core.exception.InvalidTreeStateException;
import com.grupo.taxonomia.core.exception.NodeNotFoundException;
import com.grupo.taxonomia.core.exception.ParentNodeNotFoundException;
import com.grupo.taxonomia.core.model.TreeNode;
import com.grupo.taxonomia.core.model.strategy.TreeAlgorithmStrategy;
import com.grupo.taxonomia.core.repository.TreeRepository;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Motor de árbol basado en Collections del JDK (HashMap, Stack, ArrayDeque, HashSet).
 * Implementa persistencia en memoria ({@link TreeRepository}) y consultas ({@link TreeAlgorithmStrategy}).
 */
public class CollectionsTreeStrategy implements TreeAlgorithmStrategy, TreeRepository {

    private Long rootId;
    private final Map<Long, NodeData> nodes = new HashMap<>();
    private final Map<Long, List<Long>> children = new HashMap<>();
    private final Map<Long, Long> parents = new HashMap<>();
    private final AtomicLong nodeIdCounter = new AtomicLong(0);

    private TreeRepository externalRepository;

    public void setExternalRepository(TreeRepository externalRepository) {
        this.externalRepository = externalRepository;
    }

    private void syncFromExternalIfNeeded() {
        if (externalRepository != null) {
            rebuildFromTree(externalRepository.getTree());
        }
    }

    private void rebuildFromTree(TreeNode root) {
        nodes.clear();
        children.clear();
        parents.clear();
        rootId = null;
        nodeIdCounter.set(0);
        if (root != null) {
            indexTree(root, null);
        }
    }

    private void indexTree(TreeNode node, Long parentId) {
        long id = node.getId();
        nodeIdCounter.updateAndGet(current -> Math.max(current, id));
        nodes.put(id, new NodeData(id, node.getValue()));
        children.putIfAbsent(id, new ArrayList<>());
        if (parentId == null) {
            rootId = id;
        } else {
            parents.put(id, parentId);
            children.computeIfAbsent(parentId, k -> new ArrayList<>()).add(id);
        }
        for (TreeNode child : node.getChildren()) {
            indexTree(child, id);
        }
    }

    // --- TreeRepository ---

    @Override
    public TreeNode createRoot(String value) {
        validateValue(value);
        if (rootId != null) {
            throw new InvalidTreeStateException("La raíz del árbol ya existe");
        }
        long id = nodeIdCounter.incrementAndGet();
        nodes.put(id, new NodeData(id, value));
        children.put(id, new ArrayList<>());
        rootId = id;
        return buildTreeNode(id);
    }

    @Override
    public TreeNode addChild(Long parentId, String value) {
        validateValue(value);
        if (!nodes.containsKey(parentId)) {
            throw new ParentNodeNotFoundException(parentId);
        }
        long childId = nodeIdCounter.incrementAndGet();
        nodes.put(childId, new NodeData(childId, value));
        children.putIfAbsent(childId, new ArrayList<>());
        children.get(parentId).add(childId);
        parents.put(childId, parentId);
        return buildTreeNode(childId);
    }

    @Override
    public Optional<TreeNode> findById(Long id) {
        syncFromExternalIfNeeded();
        if (!nodes.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(buildTreeNode(id));
    }

    @Override
    public TreeNode getTree() {
        syncFromExternalIfNeeded();
        if (rootId == null) {
            return null;
        }
        return buildTreeNode(rootId);
    }

    @Override
    public boolean deleteNode(Long nodeId) {
        syncFromExternalIfNeeded();
        if (!nodes.containsKey(nodeId)) {
            throw new NodeNotFoundException(nodeId);
        }
        List<Long> toDelete = new ArrayList<>();
        collectDescendantIds(nodeId, toDelete);
        Long parent = parents.get(nodeId);
        if (parent != null) {
            children.get(parent).remove(nodeId);
            parents.remove(nodeId);
        } else {
            rootId = null;
        }
        for (Long id : toDelete) {
            nodes.remove(id);
            children.remove(id);
            parents.remove(id);
        }
        return true;
    }

    @Override
    public boolean updateNode(Long nodeId, String newValue) {
        syncFromExternalIfNeeded();
        validateValue(newValue);
        NodeData node = nodes.get(nodeId);
        if (node == null) {
            throw new NodeNotFoundException(nodeId);
        }
        node.setValue(newValue);
        return true;
    }

    // --- TreeAlgorithmStrategy (map-based; ignora parámetro root si hay mapa cargado) ---

    @Override
    public List<TreeNode> dfs(TreeNode root) {
        syncFromExternalIfNeeded();
        List<TreeNode> result = new ArrayList<>();
        if (rootId == null) {
            return result;
        }
        Deque<Long> stack = new ArrayDeque<>();
        stack.push(rootId);
        while (!stack.isEmpty()) {
            Long nodeId = stack.pop();
            result.add(buildTreeNode(nodeId));
            List<Long> childIds = children.getOrDefault(nodeId, List.of());
            for (int i = childIds.size() - 1; i >= 0; i--) {
                stack.push(childIds.get(i));
            }
        }
        return result;
    }

    @Override
    public List<TreeNode> bfs(TreeNode root) {
        syncFromExternalIfNeeded();
        List<TreeNode> result = new ArrayList<>();
        if (rootId == null) {
            return result;
        }
        ArrayDeque<Long> queue = new ArrayDeque<>();
        queue.offer(rootId);
        while (!queue.isEmpty()) {
            Long nodeId = queue.poll();
            result.add(buildTreeNode(nodeId));
            queue.addAll(children.getOrDefault(nodeId, List.of()));
        }
        return result;
    }

    @Override
    public int calculateHeight(TreeNode root) {
        syncFromExternalIfNeeded();
        if (rootId == null) {
            return 0;
        }
        return heightHelper(rootId);
    }

    private int heightHelper(Long nodeId) {
        List<Long> childIds = children.getOrDefault(nodeId, List.of());
        if (childIds.isEmpty()) {
            return 0;
        }
        int maxChild = 0;
        for (Long childId : childIds) {
            maxChild = Math.max(maxChild, heightHelper(childId));
        }
        return maxChild + 1;
    }

    @Override
    public boolean validateNoCycles(TreeNode root) {
        syncFromExternalIfNeeded();
        if (rootId == null) {
            return true;
        }
        Set<Long> visited = new HashSet<>();
        return !hasCycle(rootId, visited);
    }

    private boolean hasCycle(Long nodeId, Set<Long> visited) {
        if (!visited.add(nodeId)) {
            return true;
        }
        for (Long childId : children.getOrDefault(nodeId, List.of())) {
            if (hasCycle(childId, visited)) {
                return true;
            }
        }
        visited.remove(nodeId);
        return false;
    }

    @Override
    public int calculateDepth(TreeNode root, Long nodeId) {
        syncFromExternalIfNeeded();
        if (!nodes.containsKey(nodeId)) {
            return -1;
        }
        int depth = 0;
        Long current = nodeId;
        while (current != null && !current.equals(rootId)) {
            current = parents.get(current);
            depth++;
        }
        return depth;
    }

    @Override
    public List<TreeNode> getAncestors(TreeNode root, Long nodeId) {
        syncFromExternalIfNeeded();
        if (!nodes.containsKey(nodeId)) {
            throw new NodeNotFoundException(nodeId);
        }
        List<TreeNode> ancestors = new ArrayList<>();
        Long current = parents.get(nodeId);
        while (current != null) {
            ancestors.add(buildTreeNode(current));
            current = parents.get(current);
        }
        return ancestors;
    }

    @Override
    public TreeNode getSubtree(TreeNode root, Long nodeId) {
        syncFromExternalIfNeeded();
        if (!nodes.containsKey(nodeId)) {
            return null;
        }
        return buildTreeNode(nodeId);
    }

    @Override
    public List<TreeNode> getPathToNode(TreeNode root, Long nodeId) {
        syncFromExternalIfNeeded();
        if (!nodes.containsKey(nodeId)) {
            throw new NodeNotFoundException(nodeId);
        }
        List<TreeNode> path = new ArrayList<>();
        Long current = nodeId;
        while (current != null) {
            path.add(0, buildTreeNode(current));
            current = parents.get(current);
        }
        return path;
    }

    private TreeNode buildTreeNode(Long nodeId) {
        NodeData data = nodes.get(nodeId);
        TreeNode node = new TreeNode(data.getId(), data.getValue());
        for (Long childId : children.getOrDefault(nodeId, List.of())) {
            TreeNode child = buildTreeNode(childId);
            node.addChild(child);
        }
        return node;
    }

    private void collectDescendantIds(Long nodeId, List<Long> ids) {
        ids.add(nodeId);
        for (Long childId : children.getOrDefault(nodeId, new ArrayList<>())) {
            collectDescendantIds(childId, ids);
        }
    }

    private static void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El valor no puede estar vacío");
        }
    }
}

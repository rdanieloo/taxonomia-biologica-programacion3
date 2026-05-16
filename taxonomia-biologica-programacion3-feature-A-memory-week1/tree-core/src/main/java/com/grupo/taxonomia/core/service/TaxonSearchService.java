package com.grupo.taxonomia.core.service;

import com.grupo.taxonomia.core.model.TreeNode;
import com.grupo.taxonomia.core.repository.neo4j.Neo4jTreeRepositoryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaxonSearchService {
    private final Neo4jTreeRepositoryAdapter treeRepository;

    @Autowired
    public TaxonSearchService(Neo4jTreeRepositoryAdapter treeRepository) {
        this.treeRepository = treeRepository;
    }

    /**
     * Busca un nodo taxonómico por su ID.
     */
    public Optional<TreeNode> findById(Long id) {
        return treeRepository.findById(id);
    }

    /**
     * Obtiene el árbol taxonómico completo (raíz y descendencia).
     */
    public TreeNode getTree() {
        return treeRepository.getTree();
    }

    /**
     * Busca nodos por valor exacto (nombre científico, etc.).
     */
    public Optional<TreeNode> findByValue(String value) {
        TreeNode root = getTree();
        return findByValueRecursive(root, value);
    }

    private Optional<TreeNode> findByValueRecursive(TreeNode node, String value) {
        if (node == null) return Optional.empty();
        if (value.equalsIgnoreCase(node.getValue())) {
            return Optional.of(node);
        }
        for (TreeNode child : node.getChildren()) {
            Optional<TreeNode> found = findByValueRecursive(child, value);
            if (found.isPresent()) return found;
        }
        return Optional.empty();
    }
}

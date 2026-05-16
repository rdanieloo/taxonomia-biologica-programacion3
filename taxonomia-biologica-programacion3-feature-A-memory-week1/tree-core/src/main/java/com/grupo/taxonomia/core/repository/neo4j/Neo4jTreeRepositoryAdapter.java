package com.grupo.taxonomia.core.repository.neo4j;

import com.grupo.taxonomia.core.model.TreeNode;
import com.grupo.taxonomia.core.repository.TreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Neo4jTreeRepositoryAdapter implements TreeRepository {
    private final Neo4jTreeRepository neo4jTreeRepository;

    @Autowired
    public Neo4jTreeRepositoryAdapter(Neo4jTreeRepository neo4jTreeRepository) {
        this.neo4jTreeRepository = neo4jTreeRepository;
    }

    @Override
    public TreeNode createRoot(String value) {
        TaxonEntity root = new TaxonEntity();
        root.setValue(value);
        root.setParent(null);
        root = neo4jTreeRepository.save(root);
        return toTreeNode(root);
    }

    @Override
    public TreeNode addChild(Long parentId, String value) {
        Optional<TaxonEntity> parentOpt = neo4jTreeRepository.findById(parentId);
        if (parentOpt.isEmpty()) {
            throw new IllegalArgumentException("Parent not found");
        }
        TaxonEntity parent = parentOpt.get();
        TaxonEntity child = new TaxonEntity();
        child.setValue(value);
        child.setParent(parent);
        child = neo4jTreeRepository.save(child);
        // Actualizar hijos en memoria (opcional, para reflejar la relación en el objeto)
        parent.getChildren().add(child);
        neo4jTreeRepository.save(parent);
        return toTreeNode(child);
    }

    @Override
    public Optional<TreeNode> findById(Long id) {
        return neo4jTreeRepository.findById(id).map(this::toTreeNode);
    }

    @Override
    public TreeNode getTree() {
        // Buscar el nodo raíz (sin padre)
        List<TaxonEntity> roots = neo4jTreeRepository.findAll().stream()
                .filter(t -> t.getParent() == null)
                .collect(Collectors.toList());
        if (roots.isEmpty()) {
            return null;
        }
        return toTreeNode(roots.get(0));
    }

    private TreeNode toTreeNode(TaxonEntity entity) {
        if (entity == null) return null;
        TreeNode node = new TreeNode(entity.getId(), entity.getValue(), null);
        if (entity.getChildren() != null) {
            for (TaxonEntity child : entity.getChildren()) {
                TreeNode childNode = toTreeNode(child);
                if (childNode != null) {
                    node.getChildren().add(childNode);
                }
            }
        }
        return node;
    }
}

package com.grupo.taxonomia.taxonomia_api.core.service;

import com.grupo.taxonomia.core.model.TreeNode;
import com.grupo.taxonomia.core.model.TreeNodeDTO;
import com.grupo.taxonomia.core.repository.TreeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import com.grupo.taxonomia.core.model.strategy.TreeAlgorithmStrategy;

@Service
public class TreeService {

    private final TreeRepository repo;
    private final TreeAlgorithmStrategy strategy;

    public TreeService(
            TreeRepository repo,
            TreeAlgorithmStrategy strategy
    ) {
        this.repo = repo;
        this.strategy = strategy;
    }
    

    public TreeNodeDTO createRoot(String value) {
        TreeNode node = repo.createRoot(value);
        return toDTO(node);
    }

   
    public TreeNodeDTO addChild(Long parentId, String value) {
        TreeNode node = repo.addChild(parentId, value);
        return toDTO(node);
    }

    
    public TreeNodeDTO getTree() {
        return toDTO(repo.getTree());
    }

    
    private TreeNodeDTO toDTO(TreeNode node) {

        if (node == null) return null;

        List<TreeNodeDTO> childrenDTO = node.getChildren()
                .stream()
                .map(this::toDTO)
                .toList();

        return new TreeNodeDTO(
                node.getId(),
                node.getValue(),
                childrenDTO
        );
    }
}
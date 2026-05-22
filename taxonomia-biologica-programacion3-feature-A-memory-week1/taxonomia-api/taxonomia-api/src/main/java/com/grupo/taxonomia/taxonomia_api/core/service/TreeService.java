package com.grupo.taxonomia.taxonomia_api.core.service;

import com.grupo.taxonomia.core.model.TreeNode;
import com.grupo.taxonomia.core.model.TreeNodeDTO;
import com.grupo.taxonomia.core.repository.TreeRepository;
import org.springframework.stereotype.Service;
import com.grupo.taxonomia.core.model.strategy.TreeAlgorithmStrategy;


import java.util.List;


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
    public List<TreeNodeDTO> dfsTraversal() {

        return strategy.dfs(repo.getTree())
                .stream()
                .map(this::toDTO)
                .toList();
    }
    
    public List<TreeNodeDTO> bfsTraversal() {

        return strategy.bfs(repo.getTree())
                .stream()
                .map(this::toDTO)
                .toList();
    }
    
    public int getHeight() {
        return strategy.calculateHeight(repo.getTree());
    }
    
    public boolean validateTree() {
        return strategy.validateNoCycles(repo.getTree());
    }
    
    public int getDepth(Long nodeId) {
        return strategy.calculateDepth(repo.getTree(), nodeId);
    }
    
    public List<TreeNodeDTO> getAncestors(Long nodeId) {

        return strategy.getAncestors(repo.getTree(), nodeId)
                .stream()
                .map(this::toDTO)
                .toList();
    }
    public TreeNodeDTO getSubtree(Long nodeId) {

        TreeNode subtree =
                strategy.getSubtree(repo.getTree(), nodeId);

        return toDTO(subtree);
    }
    public List<TreeNodeDTO> getPathToNode(Long nodeId) {

        return strategy.getPathToNode(repo.getTree(), nodeId)
                .stream()
                .map(this::toDTO)
                .toList();
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

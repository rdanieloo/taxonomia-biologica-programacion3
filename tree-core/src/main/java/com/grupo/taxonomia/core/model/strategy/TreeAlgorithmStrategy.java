package com.grupo.taxonomia.core.model.strategy;

import com.grupo.taxonomia.core.model.TreeNode;
import java.util.List;

public interface TreeAlgorithmStrategy {

    List<TreeNode> dfs(TreeNode root);

    List<TreeNode> bfs(TreeNode root);

    int calculateHeight(TreeNode root);

    boolean validateNoCycles(TreeNode root);

    int calculateDepth(TreeNode root, Long nodeId);
    
    List<TreeNode> getAncestors(TreeNode root, Long nodeId);
    
    TreeNode getSubtree(TreeNode root, Long nodeId);
    
    List<TreeNode> getPathToNode(TreeNode root, Long nodeId);
}
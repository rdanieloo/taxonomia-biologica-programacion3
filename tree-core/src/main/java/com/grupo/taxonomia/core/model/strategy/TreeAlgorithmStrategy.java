package com.grupo.taxonomia.core.model.strategy;

import com.grupo.taxonomia.core.model.TreeNode;
import java.util.List;

public interface TreeAlgorithmStrategy {

    List<TreeNode> dfs(TreeNode root);

    List<TreeNode> bfs(TreeNode root);

    int calculateHeight(TreeNode root);

    boolean validateNoCycles(TreeNode root);

}
package com.grupo.taxonomia.core.model.strategy.custom;

import com.grupo.taxonomia.core.model.TreeNode;
import com.grupo.taxonomia.core.model.strategy.TreeAlgorithmStrategy;


import java.util.ArrayList;
import java.util.List;

public class CustomTreeStrategy implements TreeAlgorithmStrategy {

    @Override
    public List<TreeNode> dfs(TreeNode root) {
        return new ArrayList<>();
    }

    @Override
    public List<TreeNode> bfs(TreeNode root) {
        return new ArrayList<>();
    }

    @Override
    public int calculateHeight(TreeNode root) {
        return 0;
    }

    @Override
    public boolean validateNoCycles(TreeNode root) {
        return true;
    }
}


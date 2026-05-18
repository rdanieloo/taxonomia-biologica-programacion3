package com.grupo.taxonomia.core.model.strategy.custom;

import com.grupo.taxonomia.core.model.TreeNode;
import com.grupo.taxonomia.core.model.strategy.TreeAlgorithmStrategy;


import java.util.ArrayList;
import java.util.List;

public class CustomTreeStrategy implements TreeAlgorithmStrategy {

	@Override
	public List<TreeNode> dfs(TreeNode root) {

	    List<TreeNode> result = new ArrayList<>();

	    dfsRecursive(root, result);

	    return result;
	}
	private void dfsRecursive(TreeNode node, List<TreeNode> result) {

	    if (node == null) {
	        return;
	    }

	    result.add(node);

	    for (TreeNode child : node.getChildren()) {
	        dfsRecursive(child, result);
	    }
	}

	@Override
	public List<TreeNode> bfs(TreeNode root) {

	    List<TreeNode> result = new ArrayList<>();

	    if (root == null) {
	        return result;
	    }

	    List<TreeNode> queue = new ArrayList<>();

	    queue.add(root);

	    while (!queue.isEmpty()) {

	        TreeNode current = queue.remove(0);

	        result.add(current);

	        queue.addAll(current.getChildren());
	    }

	    return result;
	}

	@Override
	public int calculateHeight(TreeNode root) {

	    if (root == null) {
	        return 0;
	    }

	    int maxHeight = 0;

	    for (TreeNode child : root.getChildren()) {

	        int childHeight = calculateHeight(child);

	        if (childHeight > maxHeight) {
	            maxHeight = childHeight;
	        }
	    }

	    return maxHeight + 1;
	}

	@Override
	public boolean validateNoCycles(TreeNode root) {

	    List<Long> visited = new ArrayList<>();

	    return validateRecursive(root, visited);
	}

	private boolean validateRecursive(TreeNode node,
	                                  List<Long> visited) {

	    if (node == null) {
	        return true;
	    }

	    if (visited.contains(node.getId())) {
	        return false;
	    }

	    visited.add(node.getId());

	    for (TreeNode child : node.getChildren()) {

	        boolean valid = validateRecursive(child, visited);

	        if (!valid) {
	            return false;
	        }
	    }

	    return true;
	}
}


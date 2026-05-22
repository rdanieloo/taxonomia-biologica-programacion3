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
	    @Override
	    public int calculateDepth(TreeNode root, Long nodeId) {

	        return depthRecursive(root, nodeId, 0);
	    }

	    private int depthRecursive(TreeNode node,
	                               Long nodeId,
	                               int currentDepth) {

	        if (node == null) {
	            return -1;
	        }

	        if (node.getId().equals(nodeId)) {
	            return currentDepth;
	        }

	        for (TreeNode child : node.getChildren()) {

	            int result = depthRecursive(
	                    child,
	                    nodeId,
	                    currentDepth + 1
	            );

	            if (result != -1) {
	                return result;
	            }
	        }

	        return -1;
	    }
	    
	    @Override
	    public List<TreeNode> getAncestors(TreeNode root,
	                                       Long nodeId) {

	        List<TreeNode> ancestors = new ArrayList<>();

	        findAncestors(root, nodeId, ancestors);

	        return ancestors;
	    }

	    private boolean findAncestors(TreeNode node,
	                                  Long nodeId,
	                                  List<TreeNode> ancestors) {

	        if (node == null) {
	            return false;
	        }

	        if (node.getId().equals(nodeId)) {
	            return true;
	        }

	        for (TreeNode child : node.getChildren()) {

	            boolean found = findAncestors(
	                    child,
	                    nodeId,
	                    ancestors
	            );

	            if (found) {

	                ancestors.add(node);

	                return true;
	            }
	        }

	        return false;
	    }
	    
	    @Override
	    public TreeNode getSubtree(TreeNode root,
	                               Long nodeId) {

	        return findSubtree(root, nodeId);
	    }

	    private TreeNode findSubtree(TreeNode node,
	                                 Long nodeId) {

	        if (node == null) {
	            return null;
	        }

	        if (node.getId().equals(nodeId)) {
	            return node;
	        }

	        for (TreeNode child : node.getChildren()) {

	            TreeNode result = findSubtree(child, nodeId);

	            if (result != null) {
	                return result;
	            }
	        }

	        return null;
	    }
	    
	    @Override
	    public List<TreeNode> getPathToNode(TreeNode root,
	                                        Long nodeId) {

	        List<TreeNode> path = new ArrayList<>();

	        findPath(root, nodeId, path);

	        return path;
	    }

	    private boolean findPath(TreeNode node,
	                             Long nodeId,
	                             List<TreeNode> path) {

	        if (node == null) {
	            return false;
	        }

	        path.add(node);

	        if (node.getId().equals(nodeId)) {
	            return true;
	        }

	        for (TreeNode child : node.getChildren()) {

	            boolean found = findPath(
	                    child,
	                    nodeId,
	                    path
	            );

	            if (found) {
	                return true;
	            }
	        }

	        path.remove(path.size() - 1);

	        return false;
	    }
	} 
	

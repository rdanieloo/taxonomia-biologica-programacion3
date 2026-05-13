package com.grupo.taxonomia.core.model;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    private String id;
    private String value;
    private TreeNode parent;
    private List<TreeNode> children = new ArrayList<>();

    public TreeNode() {
    }

    public TreeNode(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public TreeNode getParent() {
        return parent;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public void addChild(TreeNode child) {
        child.setParent(this);
        this.children.add(child);
    }
}
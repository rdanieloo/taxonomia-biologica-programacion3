package com.grupo.taxonomia.core.model;


import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    private Long id;
    private String value;
    private TreeNode parent;
    private List<TreeNode> children = new ArrayList<>();

    public TreeNode(Long id, String value, TreeNode parent) {
        this.id = id;
        this.value = value;
        this.parent = parent;
    }

    public Long getId() {
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
}
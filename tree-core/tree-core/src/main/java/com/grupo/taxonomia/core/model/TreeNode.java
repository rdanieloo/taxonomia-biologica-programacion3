package com.grupo.taxonomia.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String value;
    private TreeNode parent;
    private final List<TreeNode> children = new ArrayList<>();

    public TreeNode(Long id, String value) {
        this(id, value, null);
    }

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

    public void setValue(String value) {
        this.value = value;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void addChild(TreeNode child) {
        if (child == null) {
            return;
        }
        child.setParent(this);
        if (!children.contains(child)) {
            children.add(child);
        }
    }

    public void removeChild(TreeNode child) {
        if (child != null) {
            children.remove(child);
            child.setParent(null);
        }
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public int getChildCount() {
        return children.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TreeNode)) {
            return false;
        }
        TreeNode treeNode = (TreeNode) o;
        return Objects.equals(id, treeNode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TreeNode{id='" + id + "', value='" + value + "'}";
    }
}

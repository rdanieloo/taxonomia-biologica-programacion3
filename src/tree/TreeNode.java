package tree;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    private int id;
    private String value;
    private TreeNode parent;
    private List<TreeNode> children;

    public TreeNode(int id, String value) {
        this.id = id;
        this.value = value;
        this.parent = null;
        this.children = new ArrayList<>();
    }

    public TreeNode(int id, String value, TreeNode parent) {
        this.id = id;
        this.value = value;
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        child.setParent(this);
        this.children.add(child);
    }

    @Override
    public String toString() {
        String parentId = (parent != null) ? String.valueOf(parent.getId()) : "null";
        return "TreeNode{id=" + id + ", value='" + value + "', parentId=" + parentId
                + ", children=" + children.size() + "}";
    }
}
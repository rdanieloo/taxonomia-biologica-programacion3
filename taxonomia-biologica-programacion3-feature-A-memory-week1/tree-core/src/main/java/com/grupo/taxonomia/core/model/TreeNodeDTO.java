package com.grupo.taxonomia.core.model;

import java.util.List;

public class TreeNodeDTO {

    private Long id;
    private String value;
    private List<TreeNodeDTO> children;

    public TreeNodeDTO(Long id, String value, List<TreeNodeDTO> children) {
        this.id = id;
        this.value = value;
        this.children = children;
    }

    public Long getId() { return id; }

    public String getValue() { return value; }

    public List<TreeNodeDTO> getChildren() { return children; }
}
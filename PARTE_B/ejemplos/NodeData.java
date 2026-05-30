package com.grupo.taxonomia.core.model.strategy.collections;

/**
 * Datos de un nodo sin referencias padre/hijo (relaciones en mapas de {@link CollectionsTreeStrategy}).
 */
public class NodeData {

    private final Long id;
    private String value;

    public NodeData(Long id, String value) {
        this.id = id;
        this.value = value;
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
}

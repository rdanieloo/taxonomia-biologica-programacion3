package com.grupo.taxonomia.core.repository.neo4j;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("Taxon")
public class TaxonEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String value;

    @Relationship(type = "PARENT", direction = Relationship.Direction.INCOMING)
    private TaxonEntity parent;

    @Relationship(type = "PARENT", direction = Relationship.Direction.OUTGOING)
    private List<TaxonEntity> children = new ArrayList<>();

    public TaxonEntity() {}

    public TaxonEntity(Long id, String value, TaxonEntity parent) {
        this.id = id;
        this.value = value;
        this.parent = parent;
    }

    public Long getId() { return id; }
    public String getValue() { return value; }
    public TaxonEntity getParent() { return parent; }
    public List<TaxonEntity> getChildren() { return children; }
    public void setId(Long id) { this.id = id; }
    public void setValue(String value) { this.value = value; }
    public void setParent(TaxonEntity parent) { this.parent = parent; }
    public void setChildren(List<TaxonEntity> children) { this.children = children; }
}

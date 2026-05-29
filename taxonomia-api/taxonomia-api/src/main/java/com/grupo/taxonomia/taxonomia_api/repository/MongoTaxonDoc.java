package com.grupo.taxonomia.taxonomia_api.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "taxa")
public class MongoTaxonDoc {

    @Id
    private Long id;
    private String value;
    private Long parentId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
}

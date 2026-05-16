package com.grupo.taxonomia.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "taxa")
public class TaxonDocument {

    @Id
    private String id;

    private String value;
    private String commonName;
    private String rank;
    private String conservationStatus;
    private String parentId;
    private List<String> childrenIds = new ArrayList<>();

    public TaxonDocument() {}

    public TaxonDocument(String value, String commonName,
                         String rank, String parentId) {
        this.value = value;
        this.commonName = commonName;
        this.rank = rank;
        this.parentId = parentId;
        this.conservationStatus = "LC";
    }
}
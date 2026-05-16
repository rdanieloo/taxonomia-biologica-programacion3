package com.grupo.taxonomia.repository.mongo;

import com.grupo.taxonomia.model.mongo.TaxonDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MongoTaxonRepository
        extends MongoRepository<TaxonDocument, String> {

    List<TaxonDocument> findByParentId(String parentId);

    Optional<TaxonDocument> findByParentIdIsNull();

    Optional<TaxonDocument> findByValue(String value);
}
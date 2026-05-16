package com.grupo.taxonomia.core.repository.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface Neo4jTreeRepository extends Neo4jRepository<TaxonEntity, Long> {
    // Métodos personalizados pueden agregarse aquí si es necesario
}
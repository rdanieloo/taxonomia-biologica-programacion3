package com.grupo.taxonomia.taxonomia_api.config;

import com.grupo.taxonomia.core.repository.TreeRepository;
import com.grupo.taxonomia.taxonomia_api.repository.MongoTreeRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@ConditionalOnProperty(name = "app.storage", havingValue = "mongo")
public class MongoConfig {

    @Bean
    public TreeRepository treeRepository(MongoTemplate mongoTemplate) {
        return new MongoTreeRepository(mongoTemplate);
    }
}

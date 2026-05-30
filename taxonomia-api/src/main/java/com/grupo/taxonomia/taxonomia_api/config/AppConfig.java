package com.grupo.taxonomia.taxonomia_api.config;

<<<<<<< HEAD
import com.grupo.taxonomia.core.model.strategy.TreeAlgorithmStrategy;
import com.grupo.taxonomia.core.model.strategy.collections.CollectionsTreeStrategy;
import com.grupo.taxonomia.core.model.strategy.custom.CustomTreeStrategy;
import com.grupo.taxonomia.core.repository.TreeRepository;
import com.grupo.taxonomia.core.repository.memory.MemoryTreeRepository;
import com.grupo.taxonomia.taxonomia_api.storage.mongo.MongoTreeRepository;
import com.grupo.taxonomia.taxonomia_api.storage.neo4j.Neo4jTreeRepository;
import com.grupo.taxonomia.taxonomia_api.storage.postgres.PostgresTreeRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {

    @Bean
    @ConditionalOnProperty(name = "app.tree-strategy", havingValue = "custom", matchIfMissing = true)
    public TreeAlgorithmStrategy customTreeAlgorithmStrategy() {
        return new CustomTreeStrategy();
    }

    @Bean
    @ConditionalOnProperty(name = "app.tree-strategy", havingValue = "collections")
    public CollectionsTreeStrategy collectionsTreeStrategy(
            Environment env,
            org.springframework.beans.factory.ObjectProvider<PostgresTreeRepository> postgresProvider,
            org.springframework.beans.factory.ObjectProvider<Neo4jTreeRepository> neo4jProvider,
            org.springframework.beans.factory.ObjectProvider<MongoTreeRepository> mongoProvider) {
        CollectionsTreeStrategy strategy = new CollectionsTreeStrategy();
        String storage = env.getProperty("app.storage", "memory");
        if ("postgres".equals(storage)) {
            postgresProvider.ifAvailable(strategy::setExternalRepository);
        } else if ("neo4j".equals(storage)) {
            neo4jProvider.ifAvailable(strategy::setExternalRepository);
        } else if ("mongo".equals(storage)) {
            mongoProvider.ifAvailable(strategy::setExternalRepository);
        }
        return strategy;
    }

    @Bean
    @ConditionalOnProperty(name = "app.tree-strategy", havingValue = "collections")
    public TreeAlgorithmStrategy collectionsAlgorithmStrategy(CollectionsTreeStrategy strategy) {
        return strategy;
    }

    @Bean
    @ConditionalOnExpression("'${app.storage:memory}' == 'memory' and '${app.tree-strategy:custom}' == 'custom'")
    public TreeRepository memoryTreeRepository() {
        return new MemoryTreeRepository();
    }

    @Bean
    @ConditionalOnExpression("'${app.tree-strategy}' == 'collections' and '${app.storage:memory}' == 'memory'")
    public TreeRepository collectionsTreeRepository(CollectionsTreeStrategy strategy) {
        return strategy;
    }
}
=======
import com.grupo.taxonomia.core.repository.TreeRepository;

import com.grupo.taxonomia.core.repository.memory.MemoryTreeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.grupo.taxonomia.core.model.strategy.TreeAlgorithmStrategy;
import com.grupo.taxonomia.core.model.strategy.custom.CustomTreeStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	
    @Bean
    
    @ConditionalOnProperty(
            name = "app.tree-strategy",
            havingValue = "custom"
    )
    
    
    public TreeRepository treeRepository() {
        return new MemoryTreeRepository();
    }
    
    @Bean
    @ConditionalOnProperty(
            name = "app.tree-strategy",
            havingValue = "custom"
    )
    public TreeAlgorithmStrategy treeAlgorithmStrategy() {
        return new CustomTreeStrategy();
    }
}
>>>>>>> 7f431411b01b3421119b7fe706c8e630a4afb091

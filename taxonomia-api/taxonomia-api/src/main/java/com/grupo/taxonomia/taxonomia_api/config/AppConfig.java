package com.grupo.taxonomia.taxonomia_api.config;

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
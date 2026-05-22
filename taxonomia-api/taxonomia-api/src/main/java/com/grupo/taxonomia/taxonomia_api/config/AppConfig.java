package com.grupo.taxonomia.taxonomia_api.config;

import com.grupo.taxonomia.core.repository.TreeRepository;
import com.grupo.taxonomia.core.repository.memory.MemoryTreeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public TreeRepository treeRepository() {
        return new MemoryTreeRepository();
    }
}
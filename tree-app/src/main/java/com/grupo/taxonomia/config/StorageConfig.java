package com.grupo.taxonomia.config;

import org.springframework.context.annotation.Configuration;

/**
 * Rol C - Selector de persistencia
 *
 * app.storage=memory    MemoryTreeRepository  (Rol A)
 * app.storage=postgres  PostgresTreeRepository (Rol B)
 * app.storage=mongo     MongoTreeRepository    (Rol C)
 */
@Configuration
public class StorageConfig {
    // Activacion por @ConditionalOnProperty en cada repositorio
}
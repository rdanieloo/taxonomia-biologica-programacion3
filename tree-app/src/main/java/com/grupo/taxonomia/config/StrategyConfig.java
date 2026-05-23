//Carlos Ramos
//09052314141
package com.grupo.taxonomia.config;

import org.springframework.context.annotation.Configuration;

/**
 * Rol C - Selector del motor de algoritmos
 *
 * app.tree-strategy=custom       CustomTreeStrategy  (Rol A)
 * app.tree-strategy=collections  CollectionsTreeStrategy (Rol B)
 *
 * Combinaciones:
 *   custom + memory        custom + postgres        custom + mongo
 *   collections + memory   collections + postgres   collections + mongo
 */
@Configuration
public class StrategyConfig {
    // Activacion por @ConditionalOnProperty en cada estrategia
}
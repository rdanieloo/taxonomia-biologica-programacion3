package com.grupo.taxonomia.taxonomia_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Taxonomia Biologica API")
                        .version("1.0.0")
                        .description("API para gestionar un arbol de taxonomia biologica en memoria")
                );
    }

    @Bean
    public GroupedOpenApi treeGroup() {
        return GroupedOpenApi.builder()
                .group("tree")
                .pathsToMatch("/tree/**")
                .build();
    }

    @Bean
    public GroupedOpenApi nodesGroup() {
        return GroupedOpenApi.builder()
                .group("nodes")
                .pathsToMatch("/nodes/**")
                .build();
    }

    @Bean
    public GroupedOpenApi traversalGroup() {
        return GroupedOpenApi.builder()
                .group("traversal")
                .pathsToMatch("/traversal/**")
                .build();
    }
}
# Archivos a entregar — Integrante A

## Código (`tree-core`)

| Archivo | Ruta |
|---------|------|
| TreeNode | `tree-core/.../model/TreeNode.java` |
| TreeNodeDTO | `tree-core/.../model/TreeNodeDTO.java` |
| TreeAlgorithmStrategy | `tree-core/.../strategy/TreeAlgorithmStrategy.java` |
| CustomTreeStrategy | `tree-core/.../strategy/custom/CustomTreeStrategy.java` |
| TreeRepository | `tree-core/.../repository/TreeRepository.java` |
| MemoryTreeRepository | `tree-core/.../repository/memory/MemoryTreeRepository.java` |
| Excepciones | `tree-core/.../exception/*.java` |
| TaxonomiaBiologica | `tree-core/.../domain/TaxonomiaBiologica.java` |

## Tests (`tree-core`)

| Archivo | Ruta |
|---------|------|
| CustomTreeStrategyTest | `tree-core/src/test/.../CustomTreeStrategyTest.java` |
| MemoryTreeRepositoryTest | `tree-core/src/test/.../MemoryTreeRepositoryTest.java` |

## Código (`taxonomia-api`)

| Archivo | Ruta |
|---------|------|
| Application | `taxonomia-api/.../TaxonomiaApiApplication.java` |
| TreeQueryController | `taxonomia-api/.../controller/TreeQueryController.java` |
| GlobalExceptionHandler | `taxonomia-api/.../controller/GlobalExceptionHandler.java` |
| TreeService | `taxonomia-api/.../core/service/TreeService.java` |
| TreeNodeMapper | `taxonomia-api/.../mapper/TreeNodeMapper.java` |
| AppConfig (beans A) | `taxonomia-api/.../config/AppConfig.java` |
| OpenApiConfig | `taxonomia-api/.../config/OpenApiConfig.java` |

## Recursos

| Archivo | Ruta |
|---------|------|
| OpenAPI | `taxonomia-api/src/main/resources/openapi.yaml` |
| Config base | `taxonomia-api/src/main/resources/application.properties` |
| POM padre | `pom.xml` |
| POM core | `tree-core/pom.xml` |
| POM API | `taxonomia-api/pom.xml` |

## Documentación

- `MOTOR_CUSTOM.md`
- `ARQUITECTURA_PARTE_A.md`
- `GUIA_PRUEBAS_PARTE_A.md`
- `REPORTE_VALIDACION_PARTE_A.md`

## Copias de referencia en esta carpeta

- `ejemplos/` — fragmentos del código actual
- `documentacion/` — copia de los `.md` de la Parte A

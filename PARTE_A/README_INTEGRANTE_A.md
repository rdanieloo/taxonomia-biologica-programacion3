# Integrante A — Motor Custom + Persistencia en Memoria

## Responsabilidades

1. **Interfaz del motor** — `TreeAlgorithmStrategy`
2. **Motor custom** — `CustomTreeStrategy` (nodos explícitos, solo JDK)
3. **Persistencia memoria** — `MemoryTreeRepository`
4. **Modelo de dominio** — `TreeNode`, excepciones, DTOs en `tree-core`
5. **API REST base** — OpenAPI, controlador, servicio en `taxonomia-api`
6. **Arquitectura multimódulo** — `pom.xml` padre + `tree-core` + `taxonomia-api`

## Módulos que tocas

| Módulo | Carpeta |
|--------|---------|
| Core (árbol) | `tree-core/` |
| API | `taxonomia-api/` |

## Rutas reales del proyecto (no `tree-algorithm` / `tree-app`)

```
tree-core/src/main/java/com/grupo/taxonomia/core/
├── model/
│   ├── TreeNode.java
│   ├── TreeNodeDTO.java
│   └── strategy/
│       ├── TreeAlgorithmStrategy.java
│       └── custom/CustomTreeStrategy.java
├── repository/
│   ├── TreeRepository.java
│   └── memory/MemoryTreeRepository.java
├── exception/...
└── domain/TaxonomiaBiologica.java

taxonomia-api/src/main/java/com/grupo/taxonomia/taxonomia_api/
├── TaxonomiaApiApplication.java
├── controller/TreeQueryController.java
├── controller/GlobalExceptionHandler.java
├── core/service/TreeService.java
├── mapper/TreeNodeMapper.java
└── config/AppConfig.java (beans custom + memory)

taxonomia-api/src/main/resources/
├── openapi.yaml
└── application.properties
```

## Entregables

- Código funcional en las rutas anteriores
- Tests: `CustomTreeStrategyTest`, `MemoryTreeRepositoryTest`
- Documentación en raíz y en `PARTE_A/documentacion/`
- PR contra `main` con revisión de B y C antes de cambios que rompan contratos

## Documentación obligatoria

| Archivo | Ubicación en repo |
|---------|-------------------|
| MOTOR_CUSTOM.md | raíz |
| ARQUITECTURA_PARTE_A.md | raíz |
| GUIA_PRUEBAS_PARTE_A.md | raíz |
| REPORTE_VALIDACION_PARTE_A.md | raíz |

## Comandos útiles

```bash
cd tree-core && mvn test
cd taxonomia-api && mvn spring-boot:run
# API: http://localhost:8084/swagger-ui.html
```

## Rama sugerida

```bash
git checkout -b feature/A-motor-custom-memoria
```

## Enlaces

- [CHECKLIST_A.md](./CHECKLIST_A.md)
- [ARCHIVOS_A_ENTREGAR.md](./ARCHIVOS_A_ENTREGAR.md)
- [Ejemplos](./ejemplos/)
- [Documentación](./documentacion/)
- [Instrucciones generales del equipo](../INSTRUCCIONES_GENERALES.md)

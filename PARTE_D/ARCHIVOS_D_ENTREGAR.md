# Archivos a entregar — Integrante D

## Infraestructura (raíz)

| Archivo | Descripción |
|---------|-------------|
| `docker-compose.yml` | Orquestación de servicios |
| `Dockerfile` | Build y runtime de `taxonomia-api` |
| `.env.example` | Plantilla de variables |
| `.dockerignore` | Exclusiones del build |
| `init-mongo.sh` | Índices Mongo al primer arranque |
| `docker-compose.override.yml.example` | Overrides locales opcionales |

## Scripts

| Archivo | Descripción |
|---------|-------------|
| `scripts/validate.ps1` | Validación Windows |
| `scripts/validate.sh` | Validación Linux/macOS |

## Documentación

| Archivo | Descripción |
|---------|-------------|
| `README.md` | Quick start + puertos + combinaciones |
| `DOCKER_DEVOPS.md` | Arquitectura Docker |
| `MANTENIMIENTO.md` | Backups, limpieza, producción |
| `REPORTE_VALIDACION_PARTE_D.md` | Checklist de validación |

## Cambios mínimos en código (si aplica)

| Archivo | Cambio |
|---------|--------|
| `taxonomia-api/pom.xml` | `spring-boot-starter-actuator` |
| `application.properties` | Exposición de `/actuator/health` |

## En esta carpeta

- `ejemplos/` — copia de compose, Dockerfile, validate
- `documentacion/` — copia de los `.md` DevOps

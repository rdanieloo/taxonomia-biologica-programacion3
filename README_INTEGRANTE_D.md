# Integrante D — Docker, DevOps y Validación del Stack

## Responsabilidades

1. **Docker Compose** — app + PostgreSQL + Neo4j + MongoDB
2. **Dockerfile** — build multimódulo Maven, runtime Java 17
3. **Variables de entorno** — `.env.example` (no versionar `.env`)
4. **README** — guía de ejecución Docker y Maven
5. **Scripts** — `scripts/validate.ps1`, `scripts/validate.sh`
6. **Documentación DevOps** — operación y mantenimiento

## Regla importante

**No modificar lógica de negocio** salvo `spring-boot-starter-actuator` y propiedades necesarias para health checks.

## Archivos en raíz del repo

```
docker-compose.yml
Dockerfile
.env.example
.dockerignore
init-mongo.sh
scripts/validate.ps1
scripts/validate.sh
README.md (sección Docker)
DOCKER_DEVOPS.md
MANTENIMIENTO.md
REPORTE_VALIDACION_PARTE_D.md
```

## Comandos

```bash
cp .env.example .env
docker compose up --build
docker compose --profile db up --build
powershell -File scripts/validate.ps1
```

## Rama sugerida

```bash
git checkout -b feature/D-docker-devops
```

## Enlaces

- [CHECKLIST_D.md](./CHECKLIST_D.md)
- [ARCHIVOS_D_ENTREGAR.md](./ARCHIVOS_D_ENTREGAR.md)
- [Ejemplos](./ejemplos/)
- [Documentación](./documentacion/)

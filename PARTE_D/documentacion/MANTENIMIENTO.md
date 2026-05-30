# Guía de Mantenimiento — DevOps (Parte D)

## Tareas diarias

### Estado del stack

```bash
docker compose ps
```

Todos los servicios activos deben mostrar `healthy` o `running`.

### Revisar errores

```bash
docker compose logs --tail=100 app
docker compose logs app | findstr /i exception   # Windows
docker compose logs app | grep -i Exception      # Linux/macOS
```

## Tareas semanales

### Limpieza de recursos Docker

```bash
docker image prune -f
docker volume prune -f
docker network prune -f
```

### Backup PostgreSQL

```bash
docker compose exec postgres pg_dump -U postgres taxonomia_db > backup_taxonomia.sql
```

### Restaurar PostgreSQL

```bash
docker compose exec -T postgres psql -U postgres taxonomia_db < backup_taxonomia.sql
```

## Tareas mensuales

### Actualizar imágenes base

```bash
docker compose --profile db pull
docker compose build --no-cache app
docker compose --profile memory up --build
```

### Consumo de recursos

```bash
docker stats
```

Si la JVM queda corta de memoria, aumente `JAVA_OPTS` en `.env` (ej. `-Xmx1024m`).

## Recuperación ante fallos

### Reiniciar un servicio

```bash
docker compose restart app
docker compose restart postgres
```

### Reinicio completo con datos limpios

```bash
docker compose --profile memory --profile db down -v
docker compose --profile memory --profile db up --build
```

## Checklist pre-producción

- [ ] Contraseñas fuertes en `.env` (no valores por defecto)
- [ ] HTTPS delante de la API
- [ ] Logs centralizados
- [ ] Backups automáticos de PostgreSQL
- [ ] Monitoreo de `/actuator/health`
- [ ] Límites de memoria CPU en contenedores

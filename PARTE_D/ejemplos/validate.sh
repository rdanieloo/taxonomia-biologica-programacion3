#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

check() { echo -e "${GREEN}✓${NC} $1"; }
warn() { echo -e "${YELLOW}⚠${NC} $1"; }
fail() { echo -e "${RED}✗${NC} $1"; exit 1; }

echo "================================"
echo "Validación del Stack (Parte D)"
echo "================================"
echo ""

echo "1️⃣  Requisitos..."
command -v docker >/dev/null || fail "Docker no instalado"
check "Docker instalado"

if docker compose version >/dev/null 2>&1; then
  COMPOSE="docker compose"
elif command -v docker-compose >/dev/null; then
  COMPOSE="docker-compose"
else
  fail "Docker Compose no disponible"
fi
check "Docker Compose: $COMPOSE"

if [[ ! -f .env ]]; then
  cp .env.example .env
  warn "Se creó .env desde .env.example"
fi

echo ""
echo "2️⃣  Puertos..."
for port in 8080 5432 7687 7474 27017; do
  if command -v nc >/dev/null && nc -z 127.0.0.1 "$port" 2>/dev/null; then
    warn "Puerto $port en uso"
  else
    check "Puerto $port disponible (o nc no instalado)"
  fi
done

echo ""
echo "3️⃣  docker-compose.yml..."
$COMPOSE config >/dev/null
check "Sintaxis YAML válida"

echo ""
echo "4️⃣  Build..."
$COMPOSE build app
check "Build de aplicación exitoso"

echo ""
echo "5️⃣  Modo memoria..."
$COMPOSE up -d app
check "App levantada"

echo ""
echo "6️⃣  Health check..."
for _ in $(seq 1 18); do
  if curl -fsS "http://localhost:8080/actuator/health" | grep -q '"status":"UP"'; then
    check "Actuator UP"
    break
  fi
  sleep 5
done
curl -fsS "http://localhost:8080/actuator/health" | grep -q '"status":"UP"' || fail "Health check falló"

echo ""
echo "7️⃣  API..."
if curl -fsS "http://localhost:8080/tree" >/dev/null; then
  check "GET /tree OK"
else
  warn "GET /tree no respondió (revisar logs)"
fi

echo ""
echo "8️⃣  Limpieza..."
$COMPOSE down
check "Stack detenido"

echo ""
echo "================================"
echo -e "${GREEN}✓ VALIDACIÓN COMPLETADA${NC}"
echo "================================"

# Validación del stack Docker — Windows (Integrante D)
$ErrorActionPreference = "Stop"

function Ok($msg) { Write-Host "[OK] $msg" -ForegroundColor Green }
function Warn($msg) { Write-Host "[!!] $msg" -ForegroundColor Yellow }
function Fail($msg) { Write-Host "[XX] $msg" -ForegroundColor Red; exit 1 }

$root = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
Set-Location $root

Write-Host "================================"
Write-Host "Validación del Stack (Parte D)"
Write-Host "================================`n"

Write-Host "1. Requisitos..."
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) { Fail "Docker no instalado" }
Ok "Docker instalado"

$composeCmd = $null
if (Get-Command docker -ErrorAction SilentlyContinue) {
    docker compose version 2>$null | Out-Null
    if ($LASTEXITCODE -eq 0) { $composeCmd = "docker compose" }
}
if (-not $composeCmd -and (Get-Command docker-compose -ErrorAction SilentlyContinue)) {
    $composeCmd = "docker-compose"
}
if (-not $composeCmd) { Fail "Docker Compose no disponible" }
Ok "Docker Compose: $composeCmd"

if (-not (Test-Path ".env")) {
    Copy-Item ".env.example" ".env"
    Warn "Se creó .env desde .env.example"
}

Write-Host "`n2. Puertos..."
foreach ($port in @(8080, 5432, 7687, 7474, 27017)) {
    $inUse = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
    if ($inUse) { Warn "Puerto $port en uso" } else { Ok "Puerto $port disponible" }
}

Write-Host "`n3. Sintaxis docker-compose..."
Invoke-Expression "$composeCmd config" | Out-Null
if ($LASTEXITCODE -ne 0) { Fail "docker compose config inválido" }
Ok "YAML válido"

Write-Host "`n4. Build de la aplicación..."
Invoke-Expression "$composeCmd build app"
if ($LASTEXITCODE -ne 0) { Fail "Build falló" }
Ok "Build exitoso"

Write-Host "`n5. Levantar modo memoria..."
Invoke-Expression "$composeCmd up -d app"
if ($LASTEXITCODE -ne 0) { Fail "No se pudo levantar app" }
Ok "Contenedor app iniciado"

Write-Host "`n6. Esperando health check (hasta 90s)..."
$healthy = $false
for ($i = 0; $i -lt 18; $i++) {
    Start-Sleep -Seconds 5
    try {
        $r = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -TimeoutSec 5
        if ($r.status -eq "UP") { $healthy = $true; break }
    } catch { }
}
if (-not $healthy) { Fail "Actuator /actuator/health no respondió UP" }
Ok "Health check UP"

Write-Host "`n7. Prueba rápida API..."
try {
    $tree = Invoke-RestMethod -Uri "http://localhost:8080/tree" -TimeoutSec 10
    Ok "GET /tree respondió"
} catch {
    Warn "GET /tree: $($_.Exception.Message) (puede ser árbol vacío en primer arranque)"
}

Write-Host "`n8. Limpieza..."
Invoke-Expression "$composeCmd down"
Ok "Stack detenido"

Write-Host "`n================================"
Write-Host "VALIDACIÓN COMPLETADA" -ForegroundColor Green
Write-Host "================================"
Write-Host "Próximos pasos:"
Write-Host "  docker compose up --build"
Write-Host "  docker compose --profile db up --build"
Write-Host "  http://localhost:8080/swagger-ui.html"

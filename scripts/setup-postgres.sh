#!/usr/bin/env bash
# PostgreSQL 16 para o Sistema de Moeda Estudantil.
# Mantém postgresql16 — corrige apenas as bibliotecas que faltam.

set -euo pipefail

DB_NAME="moeda_estudantil"
DB_USER="moeda"
DB_PASS="moeda"
PG_SERVICE="postgresql16"
PG_BIN="/opt/postgresql16/bin"
PG_DATA="/var/lib/postgres/data16"

echo "=== Setup PostgreSQL 16 (sem trocar de versão) ==="
echo ""

check_libs() {
  if ldd "$PG_BIN/postgres" 2>/dev/null | grep -q 'not found'; then
    echo "Bibliotecas ainda faltando:"
    ldd "$PG_BIN/postgres" 2>/dev/null | grep 'not found' || true
    return 1
  fi
  return 0
}

install_libs() {
  echo "→ Instalando libxml2-legacy (fornece libxml2.so.2)..."
  sudo pacman -S --needed --noconfirm libxml2-legacy

  if ! pacman -Q icu75 >/dev/null 2>&1; then
    echo "→ Instalando icu75 do AUR (fornece libicui18n.so.75 e libicuuc.so.75)..."
    if command -v yay >/dev/null 2>&1; then
      yay -S --needed --noconfirm icu75
    elif command -v paru >/dev/null 2>&1; then
      paru -S --needed --noconfirm icu75
    else
      echo "ERRO: yay ou paru necessário para instalar icu75."
      echo "  sudo pacman -S yay"
      echo "  yay -S icu75"
      return 1
    fi
  fi
}

setup_docker() {
  if ! docker info >/dev/null 2>&1; then
    echo "Docker não está rodando. Abra o Docker Desktop e tente:"
    echo "  docker compose up -d"
    return 1
  fi
  cd "$(dirname "$0")/.."
  docker compose up -d
  echo "PostgreSQL 16 no Docker iniciado (postgres:16-alpine, porta 5432)."
}

setup_native() {
  if ! pacman -Q postgresql16 >/dev/null 2>&1; then
    echo "postgresql16 não instalado."
    return 1
  fi

  if ! check_libs; then
    install_libs
    echo ""
    if ! check_libs; then
      echo "Ainda há bibliotecas faltando. Verifique a saída acima."
      return 1
    fi
    echo "Bibliotecas OK."
  else
    echo "Bibliotecas já compatíveis."
  fi

  if [ ! -f "$PG_DATA/PG_VERSION" ]; then
    echo "→ Inicializando cluster em $PG_DATA ..."
    sudo mkdir -p /var/lib/postgres
    sudo chown postgres:postgres /var/lib/postgres
    sudo -u postgres "$PG_BIN/initdb" -D "$PG_DATA" --encoding=UTF8 --locale=C
  fi

  echo "→ Iniciando $PG_SERVICE ..."
  sudo systemctl enable --now "$PG_SERVICE"
  sleep 2

  if ! systemctl is-active --quiet "$PG_SERVICE"; then
    echo "Serviço não subiu. Veja: sudo journalctl -u $PG_SERVICE -n 20"
    return 1
  fi

  echo "→ Criando usuário e banco..."
  sudo -u postgres "$PG_BIN/psql" -v ON_ERROR_STOP=0 <<SQL
DO \$\$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = '${DB_USER}') THEN
    CREATE ROLE ${DB_USER} WITH LOGIN PASSWORD '${DB_PASS}';
  END IF;
END
\$\$;
SELECT 'CREATE DATABASE ${DB_NAME} OWNER ${DB_USER}'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '${DB_NAME}')\gexec
GRANT ALL PRIVILEGES ON DATABASE ${DB_NAME} TO ${DB_USER};
SQL

  echo ""
  echo "PostgreSQL 16 pronto!"
  echo "  Serviço: $PG_SERVICE"
  echo "  Banco:   $DB_NAME | Usuário: $DB_USER | Senha: $DB_PASS | Porta: 5432"
}

case "${1:-native}" in
  docker) setup_docker ;;
  native) setup_native ;;
  *)
    echo "Uso: $0 [native|docker]"
    echo ""
    echo "  native  — postgresql16 do sistema (padrão)"
    echo "  docker  — postgres:16-alpine via Docker (mesma versão 16)"
    exit 1
    ;;
esac

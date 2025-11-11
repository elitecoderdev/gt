#!/bin/bash

# Script para compilar y desplegar el servicio de validación GTIME
# Uso: ./build-and-deploy.sh [JBOSS_HOME]

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Directorio del proyecto
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_NAME="gtime-valida-ingreso"
WAR_FILE="target/${PROJECT_NAME}.war"

# Verificar si se proporcionó JBOSS_HOME
if [ -z "$1" ]; then
    if [ -z "$JBOSS_HOME" ]; then
        echo -e "${RED}Error: Debe proporcionar JBOSS_HOME como argumento o variable de entorno${NC}"
        echo "Uso: $0 [JBOSS_HOME]"
        exit 1
    fi
else
    export JBOSS_HOME="$1"
fi

echo -e "${GREEN}=== Compilando proyecto ${PROJECT_NAME} ===${NC}"
cd "$PROJECT_DIR"

# Verificar que Maven esté instalado
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Error: Maven no está instalado${NC}"
    exit 1
fi

# Compilar el proyecto
echo "Ejecutando: mvn clean package"
mvn clean package

if [ ! -f "$WAR_FILE" ]; then
    echo -e "${RED}Error: No se generó el archivo WAR${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Compilación exitosa${NC}"
echo "Archivo WAR: $PROJECT_DIR/$WAR_FILE"

# Verificar que JBOSS_HOME existe
if [ ! -d "$JBOSS_HOME" ]; then
    echo -e "${RED}Error: JBOSS_HOME no existe: $JBOSS_HOME${NC}"
    exit 1
fi

DEPLOY_DIR="$JBOSS_HOME/standalone/deployments"

if [ ! -d "$DEPLOY_DIR" ]; then
    echo -e "${RED}Error: Directorio de despliegue no existe: $DEPLOY_DIR${NC}"
    exit 1
fi

# Preguntar si desea desplegar
echo ""
echo -e "${YELLOW}¿Desea desplegar el WAR en $DEPLOY_DIR? (s/n)${NC}"
read -r response

if [[ "$response" =~ ^[Ss]$ ]]; then
    echo -e "${GREEN}=== Desplegando ${PROJECT_NAME} ===${NC}"
    
    # Copiar el WAR al directorio de despliegue
    cp "$WAR_FILE" "$DEPLOY_DIR/"
    
    echo -e "${GREEN}✓ Despliegue completado${NC}"
    echo "El servidor detectará automáticamente el archivo y lo desplegará"
    echo ""
    echo "Para verificar el despliegue, revise los logs del servidor:"
    echo "tail -f $JBOSS_HOME/standalone/log/server.log"
else
    echo -e "${YELLOW}Despliegue cancelado${NC}"
    echo "Puede desplegar manualmente copiando:"
    echo "  $PROJECT_DIR/$WAR_FILE"
    echo "  a"
    echo "  $DEPLOY_DIR/"
fi


#!/bin/bash

# Script de despliegue para GTIME Courier con CDK

set -e

echo "🚀 Iniciando despliegue de GTIME Courier con CDK..."

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Verificar que CDK está instalado
if ! command -v cdk &> /dev/null; then
    echo "❌ CDK no está instalado. Instalando..."
    npm install -g aws-cdk
fi

# Verificar que estamos en el directorio correcto
if [ ! -f "cdk.json" ]; then
    echo "❌ Error: No se encontró cdk.json. Ejecuta este script desde el directorio cdk-deploy"
    exit 1
fi

# Instalar dependencias si no existen
if [ ! -d "node_modules" ]; then
    echo "📦 Instalando dependencias..."
    npm install
fi

# Compilar TypeScript
echo "🔨 Compilando TypeScript..."
npm run build

# Verificar configuración
echo "✅ Verificando configuración..."
if [ ! -f "cdk.context.json" ]; then
    echo -e "${YELLOW}⚠️  Advertencia: No se encontró cdk.context.json${NC}"
    echo "Creando archivo de ejemplo..."
    cat > cdk.context.json.example << EOF
{
  "databaseHost": "your-rds-host.region.rds.amazonaws.com",
  "databasePort": 1521,
  "databaseName": "DOCUMENTOS",
  "databaseSecretArn": "arn:aws:secretsmanager:region:account:secret:db-credentials",
  "eventBusName": "gtime-courier-bus",
  "environment": "dev"
}
EOF
    echo "Por favor, configura cdk.context.json con tus valores antes de continuar"
    exit 1
fi

# Bootstrap CDK (solo si es necesario)
echo "🔧 Verificando bootstrap de CDK..."
if ! aws cloudformation describe-stacks --stack-name CDKToolkit &> /dev/null; then
    echo "📦 Ejecutando bootstrap de CDK..."
    cdk bootstrap
fi

# Sintetizar CloudFormation
echo "📝 Sintetizando CloudFormation..."
cdk synth

# Mostrar diferencias
echo "🔍 Mostrando diferencias..."
cdk diff

# Confirmar despliegue
read -p "¿Deseas proceder con el despliegue? (y/n): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "❌ Despliegue cancelado"
    exit 1
fi

# Desplegar
echo -e "${GREEN}🚀 Desplegando stack...${NC}"
cdk deploy --require-approval never

echo -e "${GREEN}✅ Despliegue completado exitosamente!${NC}"






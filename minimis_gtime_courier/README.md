# minimis-gtime-courier
## Aplicación moderna AWS para procesamiento de documentos GTIME Courier

Aplicación serverless desarrollada en TypeScript para AWS Lambda (ECR) con base de datos Oracle 11g usando TypeORM.

### Estructura del Proyecto

```
minimis_gtime_courier/
├── src/
│   ├── handlers/          # Lambda handlers
│   ├── services/          # Lógica de negocio
│   ├── entities/          # Entidades TypeORM
│   ├── repositories/      # Repositorios de datos
│   ├── utils/            # Utilidades
│   ├── types/            # Tipos TypeScript
│   └── config/           # Configuración
├── infrastructure/        # Infraestructura como código
├── tests/                # Tests
├── Dockerfile            # Imagen para ECR
└── package.json
```

### Requisitos

- Node.js >= 18.x
- Docker (para build de imagen)
- AWS CLI configurado
- SAM CLI (opcional, para despliegue)

### Instalación

```bash
npm install
```

### Desarrollo

```bash
# Compilar TypeScript
npm run build

# Ejecutar tests
npm run test

# Linting
npm run lint
```

### Build y Despliegue

```bash
# Build de imagen Docker
npm run docker:build

# Build con SAM
npm run sam:build

# Desplegar
npm run sam:deploy
```

### Configuración

Las variables de entorno se configuran en AWS Lambda:
- `DB_HOST`: Host de RDS Oracle
- `DB_PORT`: Puerto (default: 1521)
- `DB_NAME`: Nombre de la base de datos
- `DB_USER`: Usuario (se obtiene de Secrets Manager)
- `DB_SECRET_ARN`: ARN del secreto en Secrets Manager





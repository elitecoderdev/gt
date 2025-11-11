# Estructura del Proyecto minimis_gtime_courier

## Descripción
Aplicación moderna serverless desarrollada en TypeScript para AWS Lambda (ECR) con base de datos Oracle 11g usando TypeORM.

## Arquitectura

### Componentes principales:
1. **Orchestrator Handler** (`src/handlers/orchestrator.handler.ts`)
   - Recibe documentos JSON desde API Gateway
   - Valida estructura básica
   - Publica eventos en EventBridge

2. **Validator Handler** (`src/handlers/validator.handler.ts`)
   - Procesa eventos de documento recibido
   - Ejecuta validaciones generales y específicas
   - Publica resultados en SQS

3. **Processor Handler** (`src/handlers/processor.handler.ts`)
   - Consume mensajes de SQS
   - Persiste documentos en Oracle usando TypeORM
   - Maneja rollback en caso de error

4. **Tracer Handler** (`src/handlers/tracer.handler.ts`)
   - Registra eventos de trazabilidad en CloudWatch Logs

### Entidades TypeORM
- `DocDocumento`: Encabezado del documento
- `DocParticipacion`: Participantes
- `DocFechaDocumento`: Fechas asociadas
- `DocLocacion`: Locaciones geográficas
- `DocObservacion`: Observaciones
- `DocRelacion`: Referencias cruzadas
- `DocTransporte`: Datos de transporte
- `DocTranItemTransporte`: Items de transporte
- `DocTransGuiaCourier`: Guías courier
- `DocTransbordo`: Transbordos
- `DocVistoBueno`: Vistos buenos
- `DocCargo`: Cargos
- `DocEstado`: Estados del documento
- `DocImagen`: Imágenes asociadas

## Configuración

### Variables de Entorno Requeridas:
- `DB_HOST`: Host de RDS Oracle
- `DB_PORT`: Puerto (default: 1521)
- `DB_NAME`: Nombre de la base de datos
- `DB_SECRET_ARN`: ARN del secreto en Secrets Manager
- `EVENT_BUS_NAME`: Nombre del EventBridge bus (opcional)

### Build y Despliegue

1. **Build de la imagen Docker:**
```bash
npm install
npm run build
docker build -t gtime-courier:latest .
```

2. **Push a ECR:**
```bash
aws ecr get-login-password --region <region> | docker login --username AWS --password-stdin <account>.dkr.ecr.<region>.amazonaws.com
docker tag gtime-courier:latest <account>.dkr.ecr.<region>.amazonaws.com/gtime-courier-lambda:latest
docker push <account>.dkr.ecr.<region>.amazonaws.com/gtime-courier-lambda:latest
```

3. **Desplegar con SAM:**
```bash
sam build
sam deploy --guided
```

## Notas Importantes

- La imagen Docker incluye Oracle Instant Client para conexión a Oracle 11g
- Los drivers Oracle están pre-instalados en la imagen
- TypeORM maneja el mapeo objeto-relacional
- Las conexiones a la base de datos se gestionan mediante connection pooling
- Los secretos de base de datos se obtienen de AWS Secrets Manager






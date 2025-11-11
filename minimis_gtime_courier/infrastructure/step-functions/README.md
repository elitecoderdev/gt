# Step Functions Implementation

## Resumen

Las Step Functions están implementadas en el template SAM para orquestar los flujos de validación y persistencia.

## Step Functions Implementadas

### 1. ValidationStateMachine
**Ubicación:** `infrastructure/template.yaml` (línea ~226)

**Propósito:** Orquestar las validaciones de documentos en paralelo

**Flujo:**
1. `ValidarGenerales` - Validaciones generales (secuencial)
2. `ValidarNivel1` - Validaciones paralelas nivel 1 (Map state)
3. `ValidarNivel2` - Validaciones paralelas nivel 2 (Map state)
4. `ValidarNivel3` - Validaciones paralelas nivel 3 (Map state)
5. `AgregarResultados` - Agregar todos los resultados
6. `PublicarResultado` - Publicar en SQS

**Trigger:** EventBridge Rule cuando se recibe evento `documento.recibido`

### 2. PersistenceStateMachine
**Ubicación:** `infrastructure/template.yaml` (línea ~340)

**Propósito:** Orquestar la persistencia paralela de todas las entidades

**Flujo:**
1. `PersistirDocumento` - Persistir encabezado principal
2. `PersistirRelaciones` - Persistir todas las relaciones en paralelo (Map state)
3. `AgregarResultadosPersistencia` - Agregar resultados
4. `EvaluarResultado` - Choice state para evaluar éxito
5. `ActualizarEstado` o `Rollback` según resultado

**Trigger:** SQS Queue (puede ser invocado directamente o desde otra Step Function)

## IAM Role

**StepFunctionsRole** - Role con permisos para:
- Invocar funciones Lambda (Validator y Processor)
- Enviar mensajes a SQS
- EventBridge PutTargets

## EventBridge Integration

**ValidationStepFunctionRule** - Rule que dispara la Step Function de validación cuando se recibe un evento `documento.recibido`

## Archivos JSON de Referencia

Los archivos JSON en `infrastructure/step-functions/` son referencias de la estructura:
- `validation-state-machine.json` - Estructura de la Step Function de validación
- `persistence-state-machine.json` - Estructura de la Step Function de persistencia

**Nota:** Estos archivos son solo para referencia. La definición real está en el template.yaml usando `DefinitionString`.

## Cambios Necesarios en el Código

Para que las Step Functions funcionen correctamente, los handlers Lambda necesitan:

1. **Validator Handler:** Debe poder procesar diferentes tipos de validación según el parámetro `validationType`
2. **Processor Handler:** Debe poder procesar diferentes acciones según el parámetro `action` (persistDocument, aggregate, rollback, updateEstado)

## Próximos Pasos

1. Actualizar handlers para soportar invocación desde Step Functions
2. Implementar lógica de agregación de resultados
3. Implementar lógica de rollback transaccional
4. Testing de las Step Functions






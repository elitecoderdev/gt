# Historias de Usuario - Migración GTIME a AWS

## Resumen

Este documento contiene **76 historias de usuario** organizadas para la migración del sistema GTIME a AWS. Las historias están estructuradas en formato CSV para facilitar su importación en herramientas de gestión de proyectos (Jira, Azure DevOps, Trello, etc.).

## Estructura del CSV

El archivo `historias-usuario-migracion.csv` contiene las siguientes columnas:

### Columnas

1. **Sistema**: Nombre del sistema (GTIME AWS)
2. **Dominio**: Área funcional (Migración)
3. **Hito**: Categoría de la historia (Infraestructura, Modelos, Orquestador, Validación, Procesamiento, etc.)
4. **Historia de Usuario**: ID y título de la historia (formato: US-XXX)
5. **Como**: Rol que realiza la acción
6. **Quiero**: Acción que se quiere realizar
7. **Para**: Beneficio o razón de la acción
8. **Criterios de Aceptación**: Criterios específicos que deben cumplirse
9. **Prioridad**: Alta, Media, Baja
10. **Estimación (días)**: Días estimados para completar la historia
11. **Complejidad**: Baja, Media, Alta
12. **Estado**: Estado actual (Pendiente)
13. **Dependencias**: IDs de historias de las que depende (separadas por espacios)

## Distribución por Hitos

### Infraestructura (3 historias)
- Configuración de VPC y networking
- Configuración de RDS Oracle
- Configuración de roles IAM

### Modelos (2 historias)
- Migración de modelos comunes
- Adaptación para AWS Lambda

### Orquestador (4 historias)
- Lambda Orchestrator Handler
- Transformación XML a Gtime
- API Gateway
- EventBridge

### Validación (15 historias)
- Lambda de validaciones generales
- 12 Lambdas específicas de validación (fechas, participaciones, items, cargos, observaciones, vistos buenos, emisor, referencias, transbordos, locaciones, RUT)
- Step Function de validaciones
- Lambda agregadora de resultados
- SQS para cola de validaciones

### Procesamiento (20 historias)
- Lambda evaluadora de resultados
- 14 Lambdas de persistencia (encabezado, participaciones, fechas, locaciones, observaciones, referencias, transporte, guía courier, transbordos, vistos buenos, cargos, items, estado, imagen)
- Step Function de persistencia
- Lambda agregadora de persistencia
- Rollback de transacciones
- Actualización de estado de cola

### Trazabilidad (3 historias)
- Lambda Tracer
- CloudWatch Logs
- X-Ray

### Integración (3 historias)
- Integración Step Function validación con EventBridge
- Integración Step Function procesamiento con SQS
- WireTap para trazabilidad

### Reencolador (1 historia)
- EventBridge Scheduled Rule para reencolado

### Testing (5 historias)
- Tests unitarios para Orchestrator
- Tests unitarios para validaciones
- Tests unitarios para persistencia
- Tests de integración end-to-end
- Ambiente de pruebas

### Monitoreo (3 historias)
- CloudWatch Metrics y Alarms
- CloudWatch Dashboard
- Alertas SNS

### Seguridad (3 historias)
- Secrets Manager
- Encriptación
- Autenticación API Gateway

### CI/CD (3 historias)
- Pipeline CI/CD
- CodeBuild
- CodeDeploy

### Optimización (4 historias)
- Optimización de tamaño de deployment
- Provisioned Concurrency
- RDS Proxy
- Optimización SQL

### Documentación (3 historias)
- Documentación de arquitectura
- Runbooks operativos
- Guía de desarrollo

### Migración de Datos (1 historia)
- Script de migración de datos históricos

### Go-Live (3 historias)
- Migración piloto
- Migración gradual
- Validación final

## Estadísticas

- **Total de historias**: 76
- **Total de días estimados**: ~350 días (equivalente a ~16 semanas con 1 desarrollador, o ~8 semanas con 2 desarrolladores)
- **Historias de Alta Prioridad**: 35
- **Historias de Media Prioridad**: 36
- **Historias de Baja Prioridad**: 5

## Uso del CSV

### Importar en Jira

1. Exportar el CSV a formato compatible con Jira
2. Usar Jira Import/Export o plugins de importación CSV
3. Mapear columnas según corresponda

### Importar en Azure DevOps

1. Convertir CSV a formato Work Items
2. Usar Azure DevOps Import tool
3. Mapear campos según corresponda

### Importar en Trello

1. Usar herramientas de importación CSV como Trello CSV Import
2. Crear listas por Hito
3. Crear tarjetas por historia

### Uso Manual

El CSV puede abrirse directamente en Excel, Google Sheets, o cualquier herramienta de hojas de cálculo para:
- Filtrar por prioridad
- Filtrar por hito
- Visualizar dependencias
- Estimar tiempos por sprint
- Asignar recursos

## Orden Sugerido de Ejecución

Las historias están diseñadas para ejecutarse en el siguiente orden general:

1. **Fase 1**: Infraestructura (US-001 a US-003)
2. **Fase 2**: Modelos (US-004 a US-005)
3. **Fase 3**: Orquestador (US-006 a US-009)
4. **Fase 4**: Validación (US-010 a US-024)
5. **Fase 5**: Procesamiento (US-025 a US-043)
6. **Fase 6**: Trazabilidad (US-044 a US-046)
7. **Fase 7**: Integración (US-047 a US-049)
8. **Fase 8**: Testing y Go-Live (US-051 a US-075)

Las dependencias están indicadas en la columna "Dependencias" de cada historia.

## Notas Importantes

1. **Dependencias**: Revisar siempre la columna "Dependencias" antes de comenzar una historia
2. **Estimaciones**: Las estimaciones son aproximadas y pueden variar según el equipo
3. **Prioridad**: Revisar y ajustar prioridades según necesidades del proyecto
4. **Criterios de Aceptación**: Completar con más detalle según necesidades específicas
5. **Estado**: Actualizar el estado conforme se avance en la migración

## Mantenimiento

Este CSV debe actualizarse regularmente con:
- Cambios en el estado de las historias
- Ajustes en estimaciones basados en avance real
- Nuevas historias identificadas durante la migración
- Cambios en dependencias descubiertas

---

**Archivo**: `historias-usuario-migracion.csv`  
**Versión**: 1.0  
**Fecha**: 2024





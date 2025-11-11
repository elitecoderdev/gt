# Resumen Ejecutivo - Migración GTIME a AWS

## Sistema Actual

**Arquitectura:** JBoss EAP 6.4 + Apache Camel + RabbitMQ + Oracle Database

**Componentes:**
- **gtime-orchestator**: Consume RabbitMQ, transforma XML a objetos Java
- **gtime-valida-ingreso**: Valida documentos (15+ validaciones)
- **gtime-processor**: Persiste datos en Oracle (14 tablas)
- **gtime-trazador**: Almacena logs y eventos
- **gtime-cron-reencolador**: Reencola mensajes fallidos

**Flujo:** RabbitMQ → Orchestrator → JMS → Validator → JMS → Processor → Oracle DB

**Nota:** El sistema actual procesa XML. La nueva aplicación AWS procesará documentos directamente en formato JSON.

## Propuesta AWS

**Arquitectura:** Serverless con AWS Lambda + Step Functions + RDS Oracle

**Componentes AWS:**
- **API Gateway**: Punto de entrada REST
- **Lambda Orchestrator**: Reemplaza gtime-orchestator
- **Step Functions + Lambda Validator**: Reemplaza gtime-valida-ingreso
- **Step Functions + Lambda Processor**: Reemplaza gtime-processor
- **CloudWatch Logs**: Reemplaza gtime-trazador
- **EventBridge + SQS**: Reemplaza JMS/RabbitMQ
- **RDS Oracle**: Base de datos (migración directa)

## Beneficios Clave

✅ **Escalabilidad automática** - Sin gestión de servidores  
✅ **Costos variables** - Pay-per-use vs servidores fijos  
✅ **Alta disponibilidad** - Multi-AZ nativo  
✅ **Despliegue simplificado** - CI/CD integrado  
✅ **Monitoreo integrado** - CloudWatch + X-Ray  

## Plan de Migración

1. **Fase 1-2:** Análisis y migración de modelos (3-4 semanas)
2. **Fase 3:** Migración Orchestrator (1-2 semanas)
3. **Fase 4:** Migración Validator (2-3 semanas)
4. **Fase 5:** Migración Processor (2-3 semanas)
5. **Fase 6:** Integración y testing (2 semanas)
6. **Fase 7:** Despliegue producción (1 semana)

**Total estimado:** 11-15 semanas

## Riesgos Principales

⚠️ **Cold starts Lambda** → Mitigación: Provisioned Concurrency  
⚠️ **Timeout en procesamiento** → Mitigación: Step Functions  
⚠️ **Curva de aprendizaje** → Mitigación: Capacitación y documentación  

## Costos Estimados

**Actual (mensual):**
- Servidores JBoss: ~$500-1000
- Mantenimiento: ~$200-500
- **Total:** ~$700-1500/mes

**AWS (mensual - estimado):**
- Lambda: ~$50-100
- API Gateway: ~$20-50
- Step Functions: ~$30-60
- RDS Oracle: ~$200-500
- CloudWatch: ~$50-100
- **Total:** ~$350-810/mes

**Ahorro estimado:** 30-50% dependiendo del volumen

## Próximos Pasos

1. ✅ Análisis de arquitectura completado
2. ⏳ Aprobación de arquitectura propuesta
3. ⏳ Setup de ambiente AWS de pruebas
4. ⏳ Migración piloto (10% de la carga)
5. ⏳ Migración gradual (100% de la carga)

---

**Documentos relacionados:**
- `informe-arquitectura-migracion-aws.md` - Análisis completo
- `diagramas-arquitectura.md` - Diagramas visuales


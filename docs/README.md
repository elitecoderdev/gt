# Índice de Documentación - Sistema GTIME

## Documentos Disponibles

### 1. [Informe de Arquitectura y Migración a AWS](./informe-arquitectura-migracion-aws.md)
**Descripción:** Análisis completo de la arquitectura actual del sistema GTIME y propuesta detallada de migración a AWS.

**Contenido:**
- Arquitectura actual (componentes, flujo, tecnologías)
- Estructura de mensajes (entrada, intermedios, salida)
- Validaciones implementadas (generales y específicas)
- Persistencias en base de datos
- Relaciones entre componentes
- Propuesta de arquitectura AWS detallada
- Plan de migración por fases (8 fases)
- Consideraciones técnicas y limitaciones
- Ventajas y riesgos de la migración
- Recomendaciones adicionales

**Audiencia:** Arquitectos, desarrolladores, gestores de proyecto

---

### 2. [Diagramas de Arquitectura](./diagramas-arquitectura.md)
**Descripción:** Diagramas visuales en formato Mermaid que ilustran la arquitectura actual y propuesta.

**Contenido:**
- Diagrama de flujo actual (JBoss + Camel)
- Arquitectura de componentes actual
- Arquitectura propuesta AWS
- Flujo de validaciones en Step Functions
- Flujo de persistencia en Step Functions
- Secuencia de procesamiento
- Modelo de datos simplificado
- Matriz de migración
- Comparación de tecnologías

**Audiencia:** Arquitectos, desarrolladores, stakeholders técnicos

---

### 3. [Resumen Ejecutivo](./resumen-ejecutivo.md)
**Descripción:** Resumen conciso para presentaciones ejecutivas y toma de decisiones.

**Contenido:**
- Resumen del sistema actual
- Propuesta AWS resumida
- Beneficios clave
- Plan de migración (timeline)
- Riesgos principales
- Costos estimados
- Próximos pasos

**Audiencia:** Gerentes, directores, stakeholders de negocio

---

### 4. [Historias de Usuario - Migración](./historias-usuario-migracion.csv) y [Documentación](./README-historias-usuario.md)
**Descripción:** 76 historias de usuario organizadas para la migración completa a AWS, en formato CSV para importación en herramientas de gestión de proyectos.

**Contenido:**
- CSV con todas las historias de usuario
- Columnas: Sistema, Dominio, Hito, Historia de Usuario, Como, Quiero, Para, Criterios de Aceptación, Prioridad, Estimación, Complejidad, Estado, Dependencias
- 76 historias organizadas por hitos (Infraestructura, Modelos, Orquestador, Validación, Procesamiento, etc.)
- Dependencias entre historias documentadas
- Estimaciones y prioridades asignadas

**Audiencia:** Gestores de proyecto, Product Owners, Scrum Masters, desarrolladores

---

## Estructura del Sistema Actual

### Módulos Principales

1. **gtime-orchestator** - Orquestador principal
2. **gtime-valida-ingreso** - Módulo de validación
3. **gtime-processor** - Procesador y persistencia
4. **gtime-trazador** - Trazabilidad y logs
5. **gtime-cron-reencolador-rabbitmq** - Reencolador de mensajes
6. **gtime-modelo** - Modelos compartidos
7. **gtime-trazador-vo** - Value Objects de trazabilidad

### Tecnologías Utilizadas

- **Runtime:** JBoss EAP 6.4
- **Integration:** Apache Camel
- **Messaging:** RabbitMQ, JMS (HornetQ)
- **Database:** Oracle Database
- **Language:** Java
- **Framework:** Spring Framework

---

## Arquitectura Propuesta AWS

### Servicios AWS Principales

- **API Gateway** - Punto de entrada
- **AWS Lambda** - Funciones serverless
- **Step Functions** - Orquestación de flujos
- **EventBridge** - Sistema de eventos
- **SQS** - Colas de mensajes
- **RDS Oracle** - Base de datos
- **CloudWatch** - Monitoreo y logs
- **X-Ray** - Trazabilidad distribuida

---

## Guía de Uso

### Para Arquitectos de Software
1. Leer: `informe-arquitectura-migracion-aws.md` (secciones 1-8)
2. Revisar: `diagramas-arquitectura.md` (todos los diagramas)
3. Consultar: `informe-arquitectura-migracion-aws.md` (secciones 9-15)

### Para Desarrolladores
1. Leer: `informe-arquitectura-migracion-aws.md` (secciones 2-6)
2. Revisar: `diagramas-arquitectura.md` (diagramas de flujo y secuencia)
3. Consultar: `informe-arquitectura-migracion-aws.md` (anexos)

### Para Gestores de Proyecto
1. Leer: `resumen-ejecutivo.md` (completo)
2. Consultar: `informe-arquitectura-migracion-aws.md` (sección 9: Plan de Migración)
3. Importar: `historias-usuario-migracion.csv` en herramienta de gestión de proyectos
4. Revisar: `README-historias-usuario.md` para entender estructura y uso

### Para Stakeholders Ejecutivos
1. Leer: `resumen-ejecutivo.md` (completo)
2. Revisar: `diagramas-arquitectura.md` (arquitectura AWS)

---

## Próximos Pasos Recomendados

1. ✅ **Completado:** Análisis de arquitectura
2. ⏳ **Pendiente:** Revisión y aprobación de arquitectura propuesta
3. ⏳ **Pendiente:** Setup de ambiente AWS de desarrollo/pruebas
4. ⏳ **Pendiente:** Migración piloto (10% de carga)
5. ⏳ **Pendiente:** Migración gradual (100% de carga)

---

## Información de Contacto

Para consultas sobre esta documentación o el proceso de migración, contactar al equipo de arquitectura.

---

**Última actualización:** 2024  
**Versión:** 1.1

---

## Archivos en el Directorio

- `informe-arquitectura-migracion-aws.md` - Análisis completo de arquitectura
- `diagramas-arquitectura.md` - Diagramas visuales Mermaid
- `resumen-ejecutivo.md` - Resumen para ejecutivos
- `historias-usuario-migracion.csv` - 76 historias de usuario en CSV
- `README-historias-usuario.md` - Documentación de historias de usuario
- `README.md` - Este índice


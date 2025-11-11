# Informe de Arquitectura y Migración a AWS
## Sistema GTIME - Procesamiento de Guías Courier

**Fecha:** 2024  
**Sistema:** GTIME - Sistema de Procesamiento de Documentos de Guías Courier  
**Arquitectura Actual:** JBoss EAP 6.4 + Apache Camel + RabbitMQ + Oracle Database

---

## 1. Resumen Ejecutivo

El sistema GTIME es una aplicación empresarial desarrollada sobre JBoss EAP 6.4 que utiliza Apache Camel para orquestar el procesamiento de documentos de guías courier. El sistema actual procesa documentos XML recibidos a través de RabbitMQ, realiza validaciones exhaustivas y persiste los datos en una base de datos Oracle.

**Nota importante:** La nueva aplicación AWS procesará documentos directamente en formato JSON, eliminando la necesidad de transformación XML.

Este documento describe la arquitectura actual, las relaciones entre componentes, el formato de mensajes y propone una arquitectura moderna en AWS para migrar la aplicación.

---

## 2. Arquitectura Actual

### 2.1 Componentes del Sistema

El sistema está compuesto por los siguientes módulos principales:

#### 2.1.1 **gtime-orchestator**
- **Propósito:** Punto de entrada del sistema. Consume mensajes de RabbitMQ y orquesta el flujo inicial.
- **Tecnología:** Apache Camel + Spring Framework
- **Funcionalidad:**
  - Consume mensajes JSON desde RabbitMQ
  - Transforma XML a objetos Java (Gtime) - **Nota: En AWS se procesará directamente JSON**
  - Encola mensajes en JMS para procesamiento posterior

#### 2.1.2 **gtime-valida-ingreso**
- **Propósito:** Módulo de validación de documentos entrantes.
- **Tecnología:** Apache Camel + Spring Framework
- **Funcionalidad:**
  - Valida estructura general del documento
  - Valida fechas, participantes, items, cargos, observaciones, vistos buenos
  - Valida emisor, referencias, transbordos, locaciones, RUT
  - Genera respuesta con resultado de validaciones

#### 2.1.3 **gtime-processor**
- **Propósito:** Procesamiento y persistencia de documentos validados.
- **Tecnología:** Apache Camel + Spring Framework + JPA/Hibernate
- **Funcionalidad:**
  - Procesa documentos validados exitosamente
  - Persiste datos en múltiples tablas de Oracle
  - Maneja rollback en caso de errores
  - Actualiza estado de cola de mensajes
  - Genera trazabilidad de operaciones

#### 2.1.4 **gtime-trazador**
- **Propósito:** Persistencia de logs y eventos del sistema.
- **Tecnología:** Apache Camel + Spring Framework
- **Funcionalidad:**
  - Almacena detalles de log de eventos
  - Registra errores y advertencias

#### 2.1.5 **gtime-cron-reencolador-rabbitmq**
- **Propósito:** Servicio de reencolado de mensajes fallidos.
- **Tecnología:** Apache Camel + Quartz Scheduler
- **Funcionalidad:**
  - Ejecuta cada 5 segundos
  - Reencola mensajes que fallaron en el procesamiento

#### 2.1.6 **gtime-modelo**
- **Propósito:** Modelos de datos compartidos entre módulos.
- **Contenido:** Value Objects (VO), DTOs, entidades

#### 2.1.7 **gtime-trazador-vo**
- **Propósito:** Value Objects para trazabilidad.
- **Contenido:** Modelos de log y eventos

---

## 3. Flujo de Procesamiento

### 3.1 Diagrama de Flujo

```
┌─────────────────────────────────────────────────────────────────┐
        │                    ENTRADA: API Gateway                        │
│  PayloadVO: {id: "123", documento: {...JSON...}}              │
└───────────────────────┬─────────────────────────────────────────┘
                        │
                        ▼
        ┌───────────────────────────────┐
        │   Lambda Orchestrator          │
        │   - Consume API Gateway        │
        │   - Valida JSON                │
        │   - Parsea JSON a objeto Gtime │
        └───────────────┬───────────────┘
                        │
                        │ JMS Queue: jmsGtimeDesencoladorQueue
                        ▼
        ┌───────────────────────────────┐
        │   gtime-valida-ingreso        │
        │   - Validaciones Generales    │
        │   - Validaciones Específicas  │
        │   - Paralelas (multicast)     │
        └───────────────┬───────────────┘
                        │
                        │ JMS Queue: jmsGtimeProcesaValidacionesQueue
                        ▼
        ┌───────────────────────────────┐
        │   gtime-processor              │
        │   - Evalúa resultado          │
        │   - Si OK: Persiste datos     │
        │   - Si NOK: Actualiza queue   │
        └───────────────┬───────────────┘
                        │
            ┌───────────┴───────────┐
            │                       │
            ▼                       ▼
    ┌───────────────┐     ┌──────────────────┐
    │ Persistencia  │     │ Actualiza Queue  │
    │ - Header      │     │ - Estado ERROR   │
    │ - Participac. │     │ - Log Eventos    │
    │ - Fechas      │     └──────────────────┘
    │ - Locaciones  │
    │ - Observacion.│
    │ - Items       │
    │ - Cargos      │
    │ - Transbordos │
    │ - VistosBuenos│
    │ - Estado      │
    │ - Imagen      │
    └───────┬───────┘
            │
            │ WireTap (trazabilidad)
            ▼
    ┌───────────────┐
    │ gtime-trazador│
    │ - Persiste    │
    │   DetalleLog  │
    └───────────────┘
```

### 3.2 Colas JMS Utilizadas

1. **jmsGtimeDesencoladorQueue**: Orquestador → Validador
2. **jmsGtimeProcesaValidacionesQueue**: Validador → Processor
3. **jmsTrazaGtime**: Processor → Trazador (WireTap)
4. **jmsCambioEstadoAdmMsgQueue**: Processor → Sistema de gestión de cola

---

## 4. Estructura de Mensajes

### 4.1 Mensaje de Entrada - Sistema Actual (RabbitMQ)

El sistema actual recibe mensajes JSON desde RabbitMQ con la siguiente estructura:

```json
{
  "id": "123456",
  "mensaje": "<Documento>...</Documento>"
}
```

**Clase:** `PayloadVO`
- `id`: Identificador único del payload
- `mensaje`: Contenido XML del documento (sistema actual)

### 4.2 Mensaje de Entrada - Nueva Aplicación AWS (API Gateway)

**IMPORTANTE:** La nueva aplicación AWS procesará documentos directamente en formato JSON, eliminando la necesidad de transformación XML a JSON.

El sistema AWS recibirá documentos JSON directamente desde API Gateway con la siguiente estructura:

```json
{
  "id": "123456",
  "documento": {
    "nroEncabezado": "...",
    "user": "...",
    "tipo": "...",
    "version": "...",
    "numeroReferencia": "...",
    "totalBultos": "...",
    "totalPeso": "...",
    "totalVolumen": "...",
    "totalItem": "...",
    "valorDeclarado": "...",
    "monedaValor": "...",
    "unidadPeso": "...",
    "unidadVolumen": "...",
    "parcial": "...",
    "tipoOperacion": "...",
    "fechas": [...],
    "participaciones": [...],
    "locaciones": [...],
    "referencias": [...],
    "items": [...],
    "cargos": [...],
    "observaciones": [...],
    "transbordos": [...],
    "vistosBuenos": [...]
  }
}
```

**Clase:** `PayloadVO` (adaptado)
- `id`: Identificador único del payload
- `documento`: Objeto Gtime en formato JSON

### 4.3 Estructura XML del Documento - Sistema Actual (Referencia)

El XML contenido en `mensaje` (sistema actual) tiene la siguiente estructura raíz:

```xml
<Documento>
  <nroEncabezado>...</nroEncabezado>
  <user>...</user>
  <tipo>...</tipo>
  <version>...</version>
  <numero-referencia>...</numero-referencia>
  <total-bultos>...</total-bultos>
  <total-peso>...</total-peso>
  <total-volumen>...</total-volumen>
  <total-item>...</total-item>
  <valor-declarado>...</valor-declarado>
  <moneda-valor>...</moneda-valor>
  <unidad-peso>...</unidad-peso>
  <unidad-volumen>...</unidad-volumen>
  <parcial>...</parcial>
  <tipo-operacion>...</tipo-operacion>
  <Fechas>...</Fechas>
  <Participaciones>...</Participaciones>
  <Locaciones>...</Locaciones>
  <Referencias>...</Referencias>
  <Items>...</Items>
  <Cargos>...</Cargos>
  <Observaciones>...</Observaciones>
  <Transbordos>...</Transbordos>
  <VistosBuenos>...</VistosBuenos>
</Documento>
```

**Clase Java:** `Gtime` (paquete `cl.gob.sna.gtime.api.vo`)

**Nota:** En la nueva aplicación AWS, el documento llegará directamente como objeto JSON siguiendo la estructura de la clase `Gtime`, sin necesidad de transformación XML.

### 4.4 Mensaje Intermedio (Validación)

Después de la validación, el mensaje se transforma en `DocumentoResponse`:

```java
public class DocumentoResponse {
    private String nroDocumento;
    private List<Validacion> listValidaciones;
    private Gtime gtime;
    private String user;
    private String xml; // Nota: En AWS este campo puede contener el JSON original o ser opcional
    private List<Persistencia> listEstadoPersistencia;
    private String estadoFinal;
    private Date fem;
    private String tipoManifiesto;
    private String viaTransporte;
    private boolean contieneFEM;
}
```

---

## 5. Validaciones Implementadas

### 5.1 Validaciones Generales (Secuenciales)

1. **Login Digitador**: Valida usuario que digitó el documento
2. **Número Referencia**: Valida formato y existencia
3. **Total Bultos**: Valida suma de bultos
4. **Total Peso**: Valida suma de pesos
5. **Unidad Peso**: Valida unidad de medida
6. **Total Volumen**: Valida suma de volúmenes
7. **Unidad Volumen**: Valida unidad de medida
8. **Total Item**: Valida cantidad de items
9. **Valor Declarado**: Valida formato y rango
10. **Moneda Valor**: Valida código de moneda
11. **Parcial**: Valida si es parcial o completo
12. **Tipo Operación**: Valida tipo de operación permitida
13. **RUT Consignatario**: Valida formato RUT chileno

### 5.2 Validaciones Específicas (Paralelas - Nivel 1)

Ejecutadas en paralelo mediante `multicast`:

1. **Validar Fechas**: Valida formato y coherencia temporal
2. **Validar Participaciones**: Valida participantes del documento
3. **Validar Items**: Valida items y productos
4. **Validar Cargos**: Valida cargos asociados
5. **Validar Observaciones**: Valida observaciones permitidas
6. **Validar Vistos Buenos**: Valida vistos buenos requeridos

### 5.3 Validaciones Específicas (Paralelas - Nivel 2)

1. **Validar Emisor**: Valida emisor del documento
2. **Validar Referencias**: Valida referencias cruzadas

### 5.4 Validaciones Específicas (Paralelas - Nivel 3)

1. **Validar Transbordos**: Valida transbordos declarados
2. **Validar Locaciones**: Valida ubicaciones geográficas
3. **Validar RUT**: Valida formato y dígito verificador

---

## 6. Persistencias en Base de Datos

El módulo `gtime-processor` persiste datos en las siguientes tablas (Oracle):

1. **DOC_DOCUMENTOS**: Encabezado del documento
2. **DOC_PARTICIPACIONES**: Participantes del documento
3. **DOC_FECHA_DOCUMENTO**: Fechas asociadas
4. **DOC_LOCACION**: Ubicaciones geográficas
5. **DOC_OBSERVACION**: Observaciones
6. **DOC_RELACION**: Referencias cruzadas
7. **DOC_TRAN_ITEM_TRANSPORTE**: Items de transporte
8. **DOC_TRANSPORTE**: Información de transporte
9. **DOC_TRANS_GUIA_COURIER**: Guía courier específica
10. **DOC_TRANSBORDOS**: Transbordos
11. **DOC_VISTOS_BUENOS**: Vistos buenos
12. **DOC_CARGOS**: Cargos asociados
13. **DOC_ESTADO**: Estado del documento
14. **DOC_IMAGEN**: Imágenes asociadas

---

## 7. Relaciones entre Componentes

### 7.1 Dependencias de Comunicación

```
RabbitMQ
  ↓
gtime-orchestator (consume)
  ↓ (JMS)
gtime-valida-ingreso (consume)
  ↓ (JMS)
gtime-processor (consume)
  ├─→ gtime-trazador (WireTap - JMS)
  └─→ Base de Datos Oracle
```

### 7.2 Dependencias de Módulos Maven

```
gtime-modelo (común)
  ├─→ gtime-orchestator
  ├─→ gtime-valida-ingreso
  ├─→ gtime-processor
  └─→ gtime-trazador

gtime-trazador-vo
  └─→ gtime-trazador
```

### 7.3 Configuración de Datasources

Todos los módulos utilizan JNDI para acceder a la base de datos:
- **JNDI Name:** `java:jboss/datasources/POraGTIME`
- **Base de Datos:** Oracle Database
- **Configuración:** JBoss EAP 6.4 Datasource

---

## 8. Propuesta de Arquitectura AWS

### 8.1 Arquitectura de Referencia

```
┌─────────────────────────────────────────────────────────────────┐
│                         AWS Architecture                         │
└─────────────────────────────────────────────────────────────────┘

┌──────────────┐
│  API Gateway │  ← Punto de entrada REST/HTTP
└──────┬───────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────┐
│                  AWS Lambda (Orchestrator)                   │
│  - Recibe mensajes JSON desde API Gateway                   │
│  - Valida estructura JSON                                   │
│  - Parsea JSON a objeto Gtime                               │
└──────┬───────────────────────────────────────────────────────┘
       │
       │ Publica evento
       ▼
┌─────────────────────────────────────────────────────────────┐
│              Amazon EventBridge / SQS                        │
│  - Cola de mensajes para validación                        │
│  - Manejo de eventos                                        │
└──────┬───────────────────────────────────────────────────────┘
       │
       │ Trigger
       ▼
┌─────────────────────────────────────────────────────────────┐
│        AWS Lambda (Validador) - Step Functions               │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │ Step 1: Validaciones Generales (Secuencial)            │ │
│  │ Step 2: Validaciones Paralelas Nivel 1                 │ │
│  │ Step 3: Validaciones Paralelas Nivel 2                 │ │
│  │ Step 4: Validaciones Paralelas Nivel 3                 │ │
│  └─────────────────────────────────────────────────────────┘ │
└──────┬───────────────────────────────────────────────────────┘
       │
       │ Publica evento
       ▼
┌─────────────────────────────────────────────────────────────┐
│              Amazon EventBridge / SQS                        │
│  - Cola de mensajes para procesamiento                     │
└──────┬───────────────────────────────────────────────────────┘
       │
       │ Trigger
       ▼
┌─────────────────────────────────────────────────────────────┐
│          AWS Lambda (Processor) - Step Functions            │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │ Step 1: Evalúa resultado validación                     │ │
│  │ Step 2: Si OK → Persiste en DB (paralelo)              │ │
│  │ Step 3: Si NOK → Actualiza estado                      │ │
│  │ Step 4: Genera trazabilidad                             │ │
│  └─────────────────────────────────────────────────────────┘ │
└──────┬───────────────────────────────────────────────────────┘
       │
       ├─→ ┌──────────────────────────────────────────────┐
       │   │         Amazon RDS (Oracle)                    │
       │   │  - Persistencia de datos                      │
       │   └──────────────────────────────────────────────┘
       │
       └─→ ┌──────────────────────────────────────────────┐
           │     Amazon CloudWatch Logs                     │
           │  - Trazabilidad y logs                         │
           └──────────────────────────────────────────────┘
```

### 8.2 Componentes AWS Propuestos

#### 8.2.1 **API Gateway**
- **Propósito:** Punto de entrada RESTful para recibir documentos
- **Configuración:**
  - REST API o HTTP API
  - Autenticación mediante AWS Cognito o API Keys
  - Rate limiting y throttling
  - Integración con Lambda

#### 8.2.2 **AWS Lambda Functions**

**1. Lambda Orchestrator**
- **Runtime:** Java 17 (Amazon Corretto)
- **Memoria:** 512 MB - 1 GB
- **Timeout:** 30 segundos
- **Funcionalidad:**
  - Recibe payload JSON directamente desde API Gateway
  - Valida estructura JSON del documento
  - Parsea JSON a objeto Gtime usando Jackson
  - Valida campos requeridos y tipos de datos
  - Publica evento en EventBridge

**2. Lambda Validator (Múltiples funciones)**
- **Runtime:** Java 17
- **Memoria:** 512 MB - 2 GB
- **Timeout:** 60 segundos
- **Funcionalidad:**
  - Validaciones generales (función única)
  - Validaciones específicas (funciones separadas para paralelismo)
  - Agregación de resultados

**3. Lambda Processor**
- **Runtime:** Java 17
- **Memoria:** 1 GB - 3 GB
- **Timeout:** 300 segundos
- **Funcionalidad:**
  - Evalúa resultado de validaciones
  - Persiste datos en RDS
  - Maneja rollback si es necesario
  - Actualiza estado en DynamoDB

**4. Lambda Tracer**
- **Runtime:** Java 17
- **Memoria:** 256 MB
- **Timeout:** 30 segundos
- **Funcionalidad:**
  - Persiste logs en CloudWatch
  - Opcionalmente en DynamoDB para búsqueda

#### 8.2.3 **AWS Step Functions**
- **Propósito:** Orquestar el flujo de validaciones y procesamiento
- **Estados:**
  - **Validaciones Generales:** Secuencial
  - **Validaciones Paralelas:** Map state para paralelismo
  - **Procesamiento:** Condicional según resultado
  - **Persistencia:** Múltiples pasos paralelos

#### 8.2.4 **Amazon EventBridge**
- **Propósito:** Sistema de eventos para desacoplar componentes
- **Eventos:**
  - `gtime.documento.recibido`
  - `gtime.documento.validado`
  - `gtime.documento.procesado`
  - `gtime.documento.error`

#### 8.2.5 **Amazon SQS**
- **Propósito:** Colas de mensajes para procesamiento asíncrono
- **Colas:**
  - `gtime-validacion-queue` (DLQ habilitada)
  - `gtime-procesamiento-queue` (DLQ habilitada)
  - `gtime-retry-queue` (para reintentos)

#### 8.2.6 **Amazon RDS (Oracle)**
- **Propósito:** Base de datos para persistencia
- **Configuración:**
  - Multi-AZ para alta disponibilidad
  - Automated backups habilitados
  - Read replicas para consultas
  - Parámetro groups optimizados

#### 8.2.7 **Amazon DynamoDB** (Opcional)
- **Propósito:** Tabla de estado de documentos y metadata
- **Uso:**
  - Estado de documentos
  - Cache de validaciones frecuentes
  - Búsqueda rápida por ID

#### 8.2.8 **Amazon CloudWatch**
- **Propósito:** Monitoreo y trazabilidad
- **Servicios:**
  - CloudWatch Logs: Logs de Lambda
  - CloudWatch Metrics: Métricas de performance
  - CloudWatch Alarms: Alertas automáticas
  - CloudWatch X-Ray: Trazabilidad distribuida

#### 8.2.9 **AWS Secrets Manager**
- **Propósito:** Gestión de credenciales
- **Almacena:**
  - Credenciales de base de datos
  - API keys
  - Certificados

#### 8.2.10 **Amazon S3** (Opcional)
- **Propósito:** Almacenamiento de documentos JSON originales
- **Uso:**
  - Archivo histórico de documentos
  - Auditoría y compliance
  - Backup de documentos procesados

---

## 9. Plan de Migración

### 9.1 Fase 1: Preparación y Análisis (2-3 semanas)

1. **Inventario completo:**
   - Documentar todas las validaciones
   - Mapear todas las tablas de BD
   - Identificar dependencias externas

2. **Setup AWS:**
   - Crear VPC y subnets
   - Configurar RDS Oracle
   - Crear roles IAM necesarios

3. **Análisis de código:**
   - Identificar código reutilizable
   - Identificar código a refactorizar
   - Crear pruebas unitarias

### 9.2 Fase 2: Migración de Modelos (1 semana)

1. **Migrar modelos:**
   - Copiar clases VO/DTO
   - Adaptar para AWS Lambda
   - Eliminar dependencias de JBoss

2. **Configurar dependencias:**
   - Actualizar pom.xml para Lambda
   - Configurar logging (SLF4J + Logback)

### 9.3 Fase 3: Migración de Orchestrator (1-2 semanas)

1. **Crear Lambda Orchestrator:**
   - Convertir rutas Camel a código Java
   - Implementar handler Lambda
   - Integrar con EventBridge

2. **Testing:**
   - Pruebas unitarias
   - Pruebas de integración local
   - Deploy en AWS y pruebas

### 9.4 Fase 4: Migración de Validador (2-3 semanas)

1. **Crear Lambda Validator:**
   - Migrar validaciones generales
   - Migrar validaciones específicas
   - Implementar Step Functions para orquestación

2. **Paralelización:**
   - Configurar Map state en Step Functions
   - Optimizar funciones Lambda para paralelismo

3. **Testing:**
   - Pruebas de cada validación
   - Pruebas de integración completa

### 9.5 Fase 5: Migración de Processor (2-3 semanas)

1. **Crear Lambda Processor:**
   - Migrar lógica de persistencia
   - Adaptar repositorios para RDS
   - Implementar rollback

2. **Step Functions:**
   - Crear state machine para persistencia paralela
   - Manejo de errores y rollback

3. **Testing:**
   - Pruebas de persistencia
   - Pruebas de rollback
   - Pruebas de carga

### 9.6 Fase 6: Migración de Trazador (1 semana)

1. **Crear Lambda Tracer:**
   - Migrar lógica de trazabilidad
   - Integrar con CloudWatch Logs
   - Opcional: DynamoDB para búsqueda

### 9.7 Fase 7: Integración y Testing (2 semanas)

1. **Integración completa:**
   - Conectar todos los componentes
   - Configurar EventBridge rules
   - Configurar SQS queues

2. **Testing end-to-end:**
   - Pruebas de carga
   - Pruebas de error handling
   - Pruebas de performance

3. **Optimización:**
   - Ajustar memoria y timeout de Lambdas
   - Optimizar consultas SQL
   - Configurar auto-scaling

### 9.8 Fase 8: Despliegue y Monitoreo (1 semana)

1. **Despliegue:**
   - Configurar CI/CD con AWS CodePipeline
   - Deploy en ambiente de producción
   - Configurar monitoreo y alertas

2. **Documentación:**
   - Documentar arquitectura AWS
   - Crear runbooks operativos
   - Capacitar equipo

---

## 10. Consideraciones Técnicas

### 10.1 Limitaciones de Lambda

- **Timeout máximo:** 15 minutos
- **Memoria máxima:** 10 GB
- **Tamaño de deployment:** 250 MB (uncompressed), 50 MB (compressed)
- **Concurrent executions:** Configurable (default 1000)

**Mitigaciones:**
- Usar Step Functions para procesos largos
- Optimizar tamaño de deployment
- Usar Lambda Layers para dependencias comunes
- Considerar ECS/Fargate para procesos muy largos

### 10.2 Conectividad a Base de Datos

- **RDS en VPC:** Lambdas deben estar en VPC para acceder a RDS
- **Connection pooling:** Configurar adecuadamente
- **Cold starts:** Considerar RDS Proxy para reducir latencia

### 10.3 Manejo de Errores

- **Dead Letter Queues (DLQ):** Configurar para todas las colas
- **Retry policies:** Configurar en SQS y Step Functions
- **Error handling:** Implementar en cada Lambda
- **Rollback:** Implementar en Step Functions

### 10.4 Seguridad

- **IAM Roles:** Principio de menor privilegio
- **VPC:** Aislar recursos en VPC privada
- **Secrets Manager:** No hardcodear credenciales
- **Encryption:** En tránsito y en reposo
- **Network:** Security groups y NACLs

### 10.5 Monitoreo y Observabilidad

- **CloudWatch Logs:** Centralizar logs
- **CloudWatch Metrics:** Métricas personalizadas
- **X-Ray:** Trazabilidad distribuida
- **Dashboards:** Crear dashboards operativos
- **Alarms:** Configurar alertas críticas

### 10.6 Costos

**Estimación mensual (aproximada):**

- **Lambda:** $0.20 por millón de requests + $0.0000166667 por GB-segundo
- **API Gateway:** $3.50 por millón de requests
- **Step Functions:** $0.025 por 1000 transiciones
- **EventBridge:** $1.00 por millón de eventos
- **SQS:** Primer millón gratis, luego $0.40 por millón
- **RDS Oracle:** Depende de instancia (~$200-2000/mes)
- **CloudWatch:** $0.50 por GB de logs

**Optimizaciones:**
- Usar Lambda Reserved Concurrency para controlar costos
- Configurar auto-scaling adecuado en RDS
- Archivar logs antiguos a S3 Glacier

---

## 11. Ventajas de la Migración a AWS

### 11.1 Escalabilidad

- **Auto-scaling automático:** Lambda escala automáticamente
- **Sin gestión de servidores:** No hay que manejar instancias
- **Elasticidad:** Escala según demanda

### 11.2 Costos

- **Pay-per-use:** Solo pagas por lo que usas
- **Sin costos de infraestructura ociosa:** No hay servidores idle
- **Reducción de costos operativos:** Menos mantenimiento

### 11.3 Disponibilidad

- **Alta disponibilidad:** Multi-AZ automático
- **Resiliencia:** Manejo automático de fallos
- **Backup automático:** RDS con backups automáticos

### 11.4 Desarrollo

- **Despliegue rápido:** CI/CD simplificado
- **Testing:** Fácil crear ambientes de prueba
- **Modularidad:** Funciones independientes

### 11.5 Observabilidad

- **CloudWatch integrado:** Logs y métricas centralizados
- **X-Ray:** Trazabilidad distribuida
- **Dashboards:** Visualización fácil

---

## 12. Riesgos y Mitigaciones

### 12.1 Riesgos Técnicos

| Riesgo | Mitigación |
|--------|------------|
| Cold starts en Lambda | Usar Provisioned Concurrency, optimizar código |
| Timeout en procesamiento | Usar Step Functions, dividir en pasos |
| Conexiones a BD | Usar RDS Proxy, connection pooling |
| Pérdida de datos | DLQ, backups automáticos, replicación |

### 12.2 Riesgos de Migración

| Riesgo | Mitigación |
|--------|------------|
| Downtime durante migración | Migración gradual, blue-green deployment |
| Incompatibilidades | Testing exhaustivo, ambiente de pruebas |
| Pérdida de funcionalidad | Análisis detallado, pruebas de regresión |

### 12.3 Riesgos Operacionales

| Riesgo | Mitigación |
|--------|------------|
| Curva de aprendizaje | Capacitación, documentación |
| Dependencia de AWS | Multi-cloud strategy (opcional) |
| Costos imprevistos | Budget alerts, cost optimization |

---

## 13. Recomendaciones Adicionales

### 13.1 Arquitectura Híbrida (Transición)

Durante la migración, considerar arquitectura híbrida:
- Sistema actual sigue funcionando
- Nuevo sistema en AWS procesa nuevos documentos
- Migración gradual de carga

### 13.2 Modernización Incremental

- Considerar migrar a microservicios más pequeños
- Evaluar uso de contenedores (ECS/Fargate) para procesos pesados
- Considerar NoSQL (DynamoDB) para algunos casos de uso

### 13.3 DevOps y CI/CD

- Implementar CI/CD desde el inicio
- Infrastructure as Code (Terraform o CloudFormation)
- Automated testing en pipeline

### 13.4 Documentación

- Documentar arquitectura AWS
- Crear diagramas actualizados
- Documentar procedimientos operativos
- Crear runbooks para incidentes comunes

---

## 14. Conclusión

La migración del sistema GTIME a AWS ofrece oportunidades significativas de mejora en escalabilidad, costos y mantenibilidad. La arquitectura propuesta aprovecha servicios gestionados de AWS para reducir la complejidad operacional mientras mantiene la funcionalidad actual.

La migración debe realizarse de forma incremental y cuidadosa, con testing exhaustivo en cada fase. Se recomienda comenzar con un piloto pequeño antes de migrar toda la carga de trabajo.

---

## 15. Anexos

### 15.1 Estructura de Proyecto Propuesta

```
gtime-aws/
├── gtime-common/              # Modelos compartidos
├── gtime-orchestrator-lambda/ # Lambda Orchestrator
├── gtime-validator-lambda/    # Lambda Validator
├── gtime-processor-lambda/    # Lambda Processor
├── gtime-tracer-lambda/       # Lambda Tracer
├── step-functions/            # Step Functions definitions
├── infrastructure/            # Terraform/CloudFormation
├── scripts/                   # Scripts de despliegue
└── docs/                      # Documentación
```

### 15.2 Ejemplo de Handler Lambda

```java
public class OrchestratorHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            APIGatewayProxyRequestEvent input, 
            Context context) {
        
        try {
            // Parsear payload
            PayloadVO payload = objectMapper.readValue(
                input.getBody(), 
                PayloadVO.class
            );
            
            // Parsear JSON a Gtime (el documento ya viene en formato JSON)
            Gtime gtime = objectMapper.readValue(payload.getDocumento(), Gtime.class);
            gtime.setIdPayload(Long.valueOf(payload.getId()));
            
            // Publicar evento en EventBridge
            publishEvent(gtime);
            
            return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("Documento recibido correctamente");
                
        } catch (Exception e) {
            context.getLogger().log("Error: " + e.getMessage());
            return new APIGatewayProxyResponseEvent()
                .withStatusCode(500)
                .withBody("Error procesando documento");
        }
    }
}
```

### 15.3 Ejemplo de Step Function Definition

```json
{
  "Comment": "Validación de documento GTIME",
  "StartAt": "ValidacionesGenerales",
  "States": {
    "ValidacionesGenerales": {
      "Type": "Task",
      "Resource": "arn:aws:lambda:us-east-1:123456789012:function:gtime-validator-general",
      "Next": "ValidacionesParalelas"
    },
    "ValidacionesParalelas": {
      "Type": "Map",
      "ItemsPath": "$.validaciones",
      "MaxConcurrency": 10,
      "Iterator": {
        "StartAt": "EjecutarValidacion",
        "States": {
          "EjecutarValidacion": {
            "Type": "Task",
            "Resource": "arn:aws:lambda:us-east-1:123456789012:function:gtime-validator-specific",
            "End": true
          }
        }
      },
      "Next": "AgregarResultados"
    },
    "AgregarResultados": {
      "Type": "Task",
      "Resource": "arn:aws:lambda:us-east-1:123456789012:function:gtime-aggregator",
      "End": true
    }
  }
}
```

---

**Documento generado:** 2024  
**Versión:** 1.0  
**Autor:** Análisis de Arquitectura GTIME


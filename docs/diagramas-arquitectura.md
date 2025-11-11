# Diagramas de Arquitectura - Sistema GTIME

## 1. Diagrama de Flujo Actual (JBoss + Camel)

```mermaid
graph TB
    A[RabbitMQ<br/>PayloadVO JSON] --> B[gtime-orchestator<br/>Camel Route]
    B -->|Transforma XML→Gtime| C[JMS Queue<br/>jmsGtimeDesencoladorQueue]
    C --> D[gtime-valida-ingreso<br/>Camel Route]
    
    D --> E[Validaciones Generales<br/>Secuencial]
    E --> F[Validaciones Paralelas Nivel 1<br/>Multicast]
    F --> G[Validaciones Paralelas Nivel 2<br/>Multicast]
    G --> H[Validaciones Paralelas Nivel 3<br/>Multicast]
    
    H --> I[JMS Queue<br/>jmsGtimeProcesaValidacionesQueue]
    I --> J[gtime-processor<br/>Camel Route]
    
    J --> K{¿Validación<br/>OK?}
    K -->|Sí| L[Persistencia Paralela<br/>Multicast]
    K -->|No| M[Actualiza Queue<br/>Estado ERROR]
    
    L --> N[Base de Datos Oracle<br/>Múltiples Tablas]
    L -->|WireTap| O[JMS Queue<br/>jmsTrazaGtime]
    M --> O
    
    O --> P[gtime-trazador<br/>Camel Route]
    P --> Q[Persiste DetalleLog<br/>Oracle]
```

## 2. Arquitectura de Componentes Actual

```mermaid
graph LR
    subgraph "Sistema Actual"
        A[gtime-orchestator<br/>JBoss EAP]
        B[gtime-valida-ingreso<br/>JBoss EAP]
        C[gtime-processor<br/>JBoss EAP]
        D[gtime-trazador<br/>JBoss EAP]
        E[gtime-cron-reencolador<br/>JBoss EAP]
    end
    
    subgraph "Infraestructura"
        F[RabbitMQ]
        G[JMS/HornetQ]
        H[Oracle Database]
    end
    
    subgraph "Módulos Comunes"
        I[gtime-modelo]
        J[gtime-trazador-vo]
    end
    
    F -->|Consume| A
    A -->|Encola| G
    G -->|Consume| B
    B -->|Encola| G
    G -->|Consume| C
    C -->|WireTap| G
    G -->|Consume| D
    E -->|Cron| F
    
    A -.->|Depende| I
    B -.->|Depende| I
    C -.->|Depende| I
    D -.->|Depende| I
    D -.->|Depende| J
    
    C --> H
    D --> H
```

## 3. Arquitectura Propuesta AWS

```mermaid
graph TB
    subgraph "Capa de Entrada"
        A[API Gateway<br/>REST/HTTP]
        B[Amazon Cognito<br/>Autenticación]
    end
    
    subgraph "Capa de Procesamiento"
        C[Lambda Orchestrator<br/>Java 17]
        D[EventBridge<br/>Event Bus]
        E[SQS Queue<br/>Validación]
        F[Step Functions<br/>Orquestación Validaciones]
        G[Lambda Validator<br/>Funciones Paralelas]
        H[SQS Queue<br/>Procesamiento]
        I[Step Functions<br/>Orquestación Persistencia]
        J[Lambda Processor<br/>Funciones de Persistencia]
    end
    
    subgraph "Capa de Datos"
        K[Amazon RDS<br/>Oracle Multi-AZ]
        L[Amazon DynamoDB<br/>Estado y Metadata]
        M[Amazon S3<br/>Archivos Originales]
    end
    
    subgraph "Capa de Observabilidad"
        N[CloudWatch Logs]
        O[CloudWatch Metrics]
        P[X-Ray Tracing]
        Q[Lambda Tracer]
    end
    
    subgraph "Infraestructura"
        R[VPC<br/>Red Privada]
        S[Secrets Manager<br/>Credenciales]
        T[IAM Roles<br/>Permisos]
    end
    
    A --> B
    B --> C
    C --> D
    D --> E
    E --> F
    F --> G
    G --> H
    H --> I
    I --> J
    
    J --> K
    J --> L
    C --> M
    
    J -->|WireTap| Q
    Q --> N
    C --> O
    C --> P
    
    C -.->|Lee| S
    J -.->|Lee| S
    C -.->|Usa| T
    J -.->|Usa| T
    
    C --> R
    J --> R
    K --> R
```

## 4. Flujo de Validaciones en Step Functions

```mermaid
stateDiagram-v2
    [*] --> ValidacionesGenerales
    
    ValidacionesGenerales: Validaciones Generales<br/>- Login Digitador<br/>- Número Referencia<br/>- Totales (bultos, peso, volumen)<br/>- Unidades<br/>- Valor Declarado<br/>- Moneda<br/>- Parcial<br/>- Tipo Operación<br/>- RUT Consignatario
    
    ValidacionesGenerales --> ValidacionesParalelas1
    
    ValidacionesParalelas1: Validaciones Paralelas Nivel 1<br/>Map State (Paralelo)
    
    ValidacionesParalelas1 --> ValidacionFechas
    ValidacionesParalelas1 --> ValidacionParticipaciones
    ValidacionesParalelas1 --> ValidacionItems
    ValidacionesParalelas1 --> ValidacionCargos
    ValidacionesParalelas1 --> ValidacionObservaciones
    ValidacionesParalelas1 --> ValidacionVistosBuenos
    
    ValidacionFechas --> AgregarResultados1
    ValidacionParticipaciones --> AgregarResultados1
    ValidacionItems --> AgregarResultados1
    ValidacionCargos --> AgregarResultados1
    ValidacionObservaciones --> AgregarResultados1
    ValidacionVistosBuenos --> AgregarResultados1
    
    AgregarResultados1 --> ValidacionesParalelas2
    
    ValidacionesParalelas2: Validaciones Paralelas Nivel 2<br/>Map State (Paralelo)
    
    ValidacionesParalelas2 --> ValidacionEmisor
    ValidacionesParalelas2 --> ValidacionReferencias
    
    ValidacionEmisor --> AgregarResultados2
    ValidacionReferencias --> AgregarResultados2
    
    AgregarResultados2 --> ValidacionesParalelas3
    
    ValidacionesParalelas3: Validaciones Paralelas Nivel 3<br/>Map State (Paralelo)
    
    ValidacionesParalelas3 --> ValidacionTransbordos
    ValidacionesParalelas3 --> ValidacionLocaciones
    ValidacionesParalelas3 --> ValidacionRut
    
    ValidacionTransbordos --> AgregarResultadosFinal
    ValidacionLocaciones --> AgregarResultadosFinal
    ValidacionRut --> AgregarResultadosFinal
    
    AgregarResultadosFinal --> [*]
```

## 5. Flujo de Persistencia en Step Functions

```mermaid
stateDiagram-v2
    [*] --> EvaluarResultado
    
    EvaluarResultado: Evaluar Resultado<br/>Validación
    
    EvaluarResultado --> ValidacionOK: Si OK
    EvaluarResultado --> ValidacionNOK: Si NOK
    
    ValidacionOK: Documento Aceptado
    
    ValidacionOK --> PersistenciaParalela
    
    PersistenciaParalela: Persistencia Paralela<br/>Map State
    
    PersistenciaParalela --> PersistHeader
    PersistenciaParalela --> PersistParticipaciones
    PersistenciaParalela --> PersistFechas
    PersistenciaParalela --> PersistLocaciones
    PersistenciaParalela --> PersistObservaciones
    PersistenciaParalela --> PersistRelaciones
    PersistenciaParalela --> PersistTransporte
    PersistenciaParalela --> PersistTransGuiaCourier
    PersistenciaParalela --> PersistTransbordos
    PersistenciaParalela --> PersistVistosBuenos
    PersistenciaParalela --> PersistCargos
    PersistenciaParalela --> PersistItems
    PersistenciaParalela --> PersistEstado
    PersistenciaParalela --> PersistImagen
    
    PersistHeader --> AgregarPersistencias
    PersistParticipaciones --> AgregarPersistencias
    PersistFechas --> AgregarPersistencias
    PersistLocaciones --> AgregarPersistencias
    PersistObservaciones --> AgregarPersistencias
    PersistRelaciones --> AgregarPersistencias
    PersistTransporte --> AgregarPersistencias
    PersistTransGuiaCourier --> AgregarPersistencias
    PersistTransbordos --> AgregarPersistencias
    PersistVistosBuenos --> AgregarPersistencias
    PersistCargos --> AgregarPersistencias
    PersistItems --> AgregarPersistencias
    PersistEstado --> AgregarPersistencias
    PersistImagen --> AgregarPersistencias
    
    AgregarPersistencias --> VerificarPersistencia
    
    VerificarPersistencia --> PersistenciaExitosa: Si exitosa
    VerificarPersistencia --> PersistenciaFallida: Si fallida
    
    PersistenciaExitosa --> Trazabilidad
    PersistenciaFallida --> Rollback
    
    Rollback: Rollback<br/>Transacciones
    
    ValidacionNOK: Documento Rechazado
    ValidacionNOK --> ActualizarEstado
    
    ActualizarEstado: Actualizar Estado<br/>en Queue
    
    Trazabilidad: Generar Trazabilidad<br/>CloudWatch Logs
    
    Rollback --> Trazabilidad
    ActualizarEstado --> Trazabilidad
    Trazabilidad --> [*]
```

## 6. Estructura de Mensajes

### 6.1 Mensaje de Entrada (API Gateway)

**IMPORTANTE:** La nueva aplicación AWS procesa documentos directamente en formato JSON.

```json
{
  "id": "123456789",
  "documento": {
    "nroEncabezado": "GTIME-2024-001",
    "user": "usuario123",
    "tipo": "GUIA_COURIER",
    "version": "1.0",
    "numeroReferencia": "REF-001",
    "totalBultos": "10",
    "totalPeso": "150.5",
    "totalVolumen": "25.3",
    "totalItem": "5",
    "valorDeclarado": "1000.00",
    "monedaValor": "USD",
    "unidadPeso": "KG",
    "unidadVolumen": "M3",
    "parcial": "N",
    "tipoOperacion": "IMPORTACION",
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

### 6.2 Objeto Gtime (Después de Parsing JSON)

```json
{
  "idPayload": 123456789,
  "startTime": 1700000000000,
  "nroEncabezado": "GTIME-2024-001",
  "user": "usuario123",
  "tipo": "GUIA_COURIER",
  "version": "1.0",
  "numeroReferencia": "REF-001",
  "totalBultos": "10",
  "totalPeso": "150.5",
  "totalVolumen": "25.3",
  "totalItem": "5",
  "valorDeclarado": "1000.00",
  "monedaValor": "USD",
  "unidadPeso": "KG",
  "unidadVolumen": "M3",
  "parcial": "N",
  "tipoOperacion": "IMPORTACION",
  "fechas": { ... },
  "participaciones": { ... },
  "locaciones": { ... },
  "referencias": { ... },
  "items": { ... },
  "cargos": { ... },
  "observaciones": { ... },
  "transbordos": { ... },
  "vistosBuenos": { ... }
}
```

**Nota:** El documento llega directamente como JSON desde API Gateway, sin necesidad de transformación XML.

### 6.3 DocumentoResponse (Después de Validación)

```json
{
  "nroDocumento": "GTIME-2024-001",
  "listValidaciones": [
    {
      "tipo": "ERROR",
      "texto": "Validación fallida: ...",
      "campo": "totalPeso"
    },
    {
      "tipo": "WARNING",
      "texto": "Advertencia: ...",
      "campo": "observaciones"
    }
  ],
  "gtime": { ... },
  "estadoFinal": "RECHAZADO",
  "listEstadoPersistencia": [ ... ]
}
```

## 7. Secuencia de Procesamiento

```mermaid
sequenceDiagram
    participant Client
    participant API as API Gateway
    participant LO as Lambda Orchestrator
    participant EB as EventBridge
    participant SF1 as Step Functions (Validación)
    participant LV as Lambda Validator
    participant SQS as SQS Queue
    participant SF2 as Step Functions (Persistencia)
    participant LP as Lambda Processor
    participant RDS as RDS Oracle
    participant CW as CloudWatch Logs
    
    Client->>API: POST /documentos (JSON payload)
    API->>LO: Invoke Lambda
    LO->>LO: Validate JSON structure
    LO->>LO: Parse JSON to Gtime object
    LO->>EB: Publish evento: documento.recibido
    LO->>API: Response 200 OK
    
    EB->>SF1: Trigger Step Function
    SF1->>LV: Invoke Validación General
    LV-->>SF1: Resultado
    
    SF1->>LV: Invoke Validaciones Paralelas (6 funciones)
    par Validaciones Paralelas
        LV->>LV: Validar Fechas
        LV->>LV: Validar Participaciones
        LV->>LV: Validar Items
        LV->>LV: Validar Cargos
        LV->>LV: Validar Observaciones
        LV->>LV: Validar Vistos Buenos
    end
    
    LV-->>SF1: Resultados Agregados
    SF1->>LV: Invoke Validaciones Nivel 2
    LV-->>SF1: Resultados
    SF1->>LV: Invoke Validaciones Nivel 3
    LV-->>SF1: Resultados Finales
    
    SF1->>SQS: Enviar a Cola Procesamiento
    SQS->>SF2: Trigger Step Function
    
    SF2->>SF2: Evaluar Resultado Validación
    
    alt Validación OK
        SF2->>LP: Invoke Persistencia (14 funciones paralelas)
        par Persistencias Paralelas
            LP->>RDS: INSERT Header
            LP->>RDS: INSERT Participaciones
            LP->>RDS: INSERT Fechas
            LP->>RDS: INSERT Locaciones
            LP->>RDS: INSERT Items
            LP->>RDS: INSERT Cargos
            LP->>RDS: INSERT Transbordos
            LP->>RDS: INSERT Vistos Buenos
            LP->>RDS: INSERT Estado
            LP->>RDS: INSERT Imagen
        end
        LP-->>SF2: Resultado Persistencia
        SF2->>CW: Log Trazabilidad
    else Validación NOK
        SF2->>RDS: UPDATE Estado Queue = ERROR
        SF2->>CW: Log Trazabilidad Error
    end
    
    SF2-->>Client: Response Final
```

## 8. Modelo de Datos Simplificado

```mermaid
erDiagram
    DOC_DOCUMENTOS ||--o{ DOC_PARTICIPACIONES : tiene
    DOC_DOCUMENTOS ||--o{ DOC_FECHA_DOCUMENTO : tiene
    DOC_DOCUMENTOS ||--o{ DOC_LOCACION : tiene
    DOC_DOCUMENTOS ||--o{ DOC_OBSERVACION : tiene
    DOC_DOCUMENTOS ||--o{ DOC_RELACION : tiene
    DOC_DOCUMENTOS ||--o{ DOC_TRAN_ITEM_TRANSPORTE : tiene
    DOC_DOCUMENTOS ||--o{ DOC_TRANSPORTE : tiene
    DOC_DOCUMENTOS ||--o{ DOC_TRANSBORDOS : tiene
    DOC_DOCUMENTOS ||--o{ DOC_VISTOS_BUENOS : tiene
    DOC_DOCUMENTOS ||--o{ DOC_CARGOS : tiene
    DOC_DOCUMENTOS ||--o{ DOC_ESTADO : tiene
    DOC_DOCUMENTOS ||--o{ DOC_IMAGEN : tiene
    
    DOC_DOCUMENTOS {
        bigint id_documento PK
        string nro_encabezado
        string tipo_documento
        string estado
        datetime fecha_creacion
        bigint id_payload
    }
    
    DOC_PARTICIPACIONES {
        bigint id_participacion PK
        bigint id_documento FK
        string tipo_participante
        string rut
    }
    
    DOC_TRAN_ITEM_TRANSPORTE {
        bigint id_item PK
        bigint id_documento FK
        string descripcion
        decimal cantidad
    }
```

## 9. Matriz de Migración

| Componente Actual | Componente AWS Propuesto | Tipo de Migración |
|-------------------|--------------------------|-------------------|
| RabbitMQ Consumer | API Gateway + Lambda Orchestrator | Reescritura |
| Camel Route (Orchestrator) | Lambda Orchestrator | Conversión |
| Camel Route (Validación) | Step Functions + Lambda Validator | Conversión |
| Camel Route (Processor) | Step Functions + Lambda Processor | Conversión |
| Camel Route (Trazador) | Lambda Tracer + CloudWatch | Conversión |
| JMS Queue | SQS Queue | Reemplazo |
| JBoss EAP | Lambda Functions | Reemplazo |
| Oracle Database | RDS Oracle | Migración |
| HornetQ/JMS | SQS / EventBridge | Reemplazo |
| Cron Job (Reencolador) | EventBridge Scheduled Rule | Conversión |

## 10. Comparación de Tecnologías

| Aspecto | Actual (JBoss + Camel) | Propuesto (AWS Serverless) |
|---------|------------------------|----------------------------|
| **Escalabilidad** | Manual (más instancias JBoss) | Automática (Lambda) |
| **Disponibilidad** | Alta (con configuración) | Alta (nativa AWS) |
| **Costos** | Fijos (servidores) | Variables (pay-per-use) |
| **Mantenimiento** | Alto (servidores, JBoss) | Bajo (gestionado) |
| **Despliegue** | Complejo (WAR/EAR) | Simple (Lambda deployment) |
| **Monitoreo** | Logs JBoss | CloudWatch integrado |
| **Cold Start** | No aplica | Sí (mitigable) |
| **Timeout** | Sin límite | 15 min máximo |

---

**Nota:** Estos diagramas son propuestas visuales para facilitar la comprensión de la arquitectura actual y propuesta. Los diagramas Mermaid pueden renderizarse en herramientas como GitHub, GitLab, o editores que soporten Mermaid.


# GTIME Valida Ingreso - Servicio de Validación

Este módulo implementa el flujo de validación de documentos GTIME para guías courier.

## Arquitectura

El servicio consume mensajes de la cola JMS `jmsGtimeDesencoladorQueue`, ejecuta múltiples validaciones en paralelo y envía los resultados a la cola `jmsGtimeProcesaValidacionesQueue`.

### Flujo de Validación

1. **Recepción**: Consume mensajes JSON de la cola `jmsGtimeDesencoladorQueue`
2. **Validaciones Generales**: Ejecuta validaciones básicas (login, número de referencia, totales, etc.)
3. **Validaciones Paralelas Nivel 1**: 
   - Validación de fechas
   - Validación de participaciones
   - Validación de items
   - Validación de cargos
   - Validación de observaciones
   - Validación de vistos buenos
4. **Validaciones Paralelas Nivel 2**:
   - Validación de emisor
   - Validación de referencias
5. **Validaciones Paralelas Nivel 3**:
   - Validación de transbordos
   - Validación de locaciones
   - Validación de RUT
6. **Respuesta**: Envía resultados a la cola `jmsGtimeProcesaValidacionesQueue`

## Requisitos Previos

### Servidor de Aplicaciones
- JBoss EAP 6.4 o superior (o WildFly equivalente)
- Java JDK 7 o superior

### Configuración del Servidor

#### 1. Datasource

Crear el datasource `java:jboss/datasources/GLOBAL` en la configuración del servidor:

```xml
<datasource jta="false" jndi-name="java:jboss/datasources/GLOBAL" 
            pool-name="java:jboss/datasources/GLOBAL" 
            enabled="true" use-java-context="true">
    <connection-url>jdbc:oracle:thin:@HOST:PORT/SID</connection-url>
    <driver>oracle-ojdbc6</driver>
    <new-connection-sql>
        BEGIN 
            EXECUTE IMMEDIATE 'alter session SET NLS_NUMERIC_CHARACTERS=",."';
            EXECUTE IMMEDIATE 'alter session SET NLS_DATE_FORMAT="DD/MM/RR"';
            EXECUTE IMMEDIATE 'alter session SET NLS_TIMESTAMP_FORMAT = "DD/MM/RR HH24:MI:SSXFF"';
            EXECUTE IMMEDIATE 'alter session SET NLS_SORT = "SPANISH"';
        END;
    </new-connection-sql>
    <pool>
        <min-pool-size>1</min-pool-size>
        <max-pool-size>30</max-pool-size>
    </pool>
    <security>
        <user-name>USUARIO_BD</user-name>
        <password>PASSWORD_BD</password>
    </security>
    <timeout>
        <blocking-timeout-millis>30000</blocking-timeout-millis>
    </timeout>
</datasource>
```

#### 2. Colas JMS

Configurar las siguientes colas JMS en el servidor:

```xml
<jms-queue name="jmsGtimeDesencoladorQueue">
    <entry name="java:/jms/queue/jmsGtimeDesencoladorQueue"/>
</jms-queue>

<jms-queue name="jmsGtimeProcesaValidacionesQueue">
    <entry name="java:/jms/queue/jmsGtimeProcesaValidacionesQueue"/>
</jms-queue>

<jms-queue name="jmsTrazaGtime">
    <entry name="java:/jms/queue/jmsTrazaGtime"/>
</jms-queue>

<jms-queue name="jmsCambioEstadoAdmMsgQueue">
    <entry name="java:/jms/queue/jmsCambioEstadoAdmMsgQueue"/>
</jms-queue>
```

## Compilación

```bash
cd gtime-valida-ingreso
mvn clean package
```

El archivo WAR se generará en: `target/gtime-valida-ingreso.war`

## Despliegue

### Opción 1: Despliegue Manual

1. Copiar el archivo WAR al directorio de despliegue del servidor:
   ```bash
   cp target/gtime-valida-ingreso.war $JBOSS_HOME/standalone/deployments/
   ```

2. El servidor detectará automáticamente el archivo y lo desplegará.

### Opción 2: Usando CLI de JBoss

```bash
$JBOSS_HOME/bin/jboss-cli.sh --connect --command="deploy target/gtime-valida-ingreso.war"
```

## Verificación

Una vez desplegado, verificar en los logs del servidor que el contexto Camel se haya iniciado correctamente:

```
[gtime-valida-ingreso-camel-context] Started
```

## Estructura del Proyecto

- `src/main/java/cl/gob/sna/gtime/validaciones/`: Código fuente Java
  - `genericas/`: Validaciones genéricas reutilizables
  - `ingreso/`: Validaciones específicas de ingreso
  - `repository/`: Repositorios de acceso a datos
- `src/main/resources/spring/`: Configuración Spring/Camel
  - `camel-context.xml`: Rutas principales de Camel
  - `validaciones-genericas-route-context.xml`: Rutas de validaciones genéricas
  - `beans.xml`: Definición de beans Spring
- `src/main/webapp/`: Recursos web (WAR)

## Dependencias Principales

- Apache Camel
- Spring Framework
- Jackson (JSON)
- Oracle JDBC Driver
- JBoss JMS

## Troubleshooting

### El servicio no consume mensajes

1. Verificar que las colas JMS estén creadas correctamente
2. Verificar que el datasource esté configurado y funcionando
3. Revisar los logs del servidor para errores de conexión

### Errores de validación

1. Verificar que las tablas de la base de datos existan
2. Verificar permisos del usuario de base de datos
3. Revisar los logs de la aplicación para detalles específicos

## Notas

- El servicio procesa mensajes en formato JSON
- Las validaciones se ejecutan en paralelo para mejorar el rendimiento
- Los resultados se envían a la cola de procesamiento para continuar el flujo


# Proyecto Camel Cron (JBoss EAP 6.4) 
================================================
# 1 - Crear Property System (Ruta directorio config APP) 

```xml
<property name="configuration-pda" value="${jboss.server.config.dir}/pda"/>
```

# 2 - Property File Servidor 

${jboss.server.config.dir}/pda/standalone/configuration/pda/pda-cron-config.properties

```properties
cl.gob.sna.pda.cron068.quartz.expression=0 0/1 * 1/1 * ? *
```

# 3 - Crear colas JMS en Servidor (standalone-full.xml)

```xml
<jms-destinations>
    <jms-queue name="jmsPDACron068Out">
        <entry name="java:/jms/queue/jmsPDACron068Out"/>
    </jms-queue>
</jms-destinations>
```

# 4 - Agregar DataSource a Servidor (standalone-full.xml)
```xml
<datasource jndi-name="java:jboss/datasources/POraPOSTAL" pool-name="java:jboss/datasources/POraPOSTAL" enabled="true" use-java-context="true">
    <connection-url>jdbc:oracle:thin:@fresno.aduana.cl:1521/aries</connection-url>
    <driver>oracle-ojdbc6</driver>
    <pool>
        <min-pool-size>5</min-pool-size>
        <max-pool-size>20</max-pool-size>
    </pool>
    <security>
        <user-name>POSTAL</user-name>
        <password>.....</password>
    </security>
</datasource>
<datasource jndi-name="java:jboss/datasources/GLOBAL" pool-name="java:jboss/datasources/GLOBAL" enabled="true" use-java-context="true" use-ccm="true">
    <connection-url>jdbc:oracle:thin:@fresno.aduana.cl:1521/aries</connection-url>
    <driver>oracle-ojdbc6</driver>
    <new-connection-sql>alter session SET NLS_DATE_FORMAT="DD/MM/RR"</new-connection-sql>
    <pool>
        <min-pool-size>3</min-pool-size>
        <max-pool-size>50</max-pool-size>
    </pool>
    <security>
        <user-name>GLOBAL</user-name>
        <password>.....</password>
    </security>
    <timeout>
        <idle-timeout-minutes>15</idle-timeout-minutes>
    </timeout>
</datasource>
```
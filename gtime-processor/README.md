Proyecto Arquetipo JBoss EAP 6.4 CXF-Camel
=======================================

# sna-archetype-ws 1.0


# 1- Configuracion DS

1.1 - Se debe crear el datasource `java:jboss/datasources/GLOBAL`

```xml
 <datasource jta="false" jndi-name="java:jboss/datasources/IsidoraExpDS" pool-name="java:jboss/datasources/IsidoraExpDS" enabled="true" use-java-context="true">
<connection-url>jdbc:oracle:thin:@HOST:PORT/SID</connection-url>
<driver>oracle-ojdbc6</driver>
<new-connection-sql>
BEGIN EXECUTE IMMEDIATE 'alter session SET NLS_NUMERIC_CHARACTERS=",."';
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
<user-name>CHANGE_ME</user-name>
<password>CHANGE_ME</password>
</security>
<timeout>
<blocking-timeout-millis>30000</blocking-timeout-millis>
</timeout>
</datasource>
```

1.2- Colas 

```xml
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



# 2 - WSDL 

AMBIENTE LOCAL: `http://localhost:8080/S2mcWS/S2mcService.jws?wsdl` 

# 3- Ejemplo del Mensaje SOAP

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:aduana:Documentos">
   <soapenv:Header/>
   <soapenv:Body>
      <urn:enviaDocumento>
         <login>SRL</login>
         <clave>4639</clave>
         <numeroReferencia>22</numeroReferencia>
         <xml><![CDATA[<Documento tipo="GTIME" version="1.0">
 <valor-declarado>189.99</valor-declarado>
 <moneda-valor>USD</moneda-valor>
 <parcial>N</parcial>
 <user-host-origen>HOSTNAME/IP_ADDRESS</user-host-origen>
 <id-sender>SMS-3.3.20P</id-sender>a
<numero-referencia>${__Random(0000,99999999999)}</numero-referencia>
 <unidad-peso>KGM</unidad-peso>
 <tipo-operacion>I</tipo-operacion>
 <total-peso>1.23</total-peso>
 <tipo-accion>I</tipo-accion>
 <total-item>1</total-item>
 <total-bultos>1</total-bultos>
 <login>lhsadmin</login>
 <Fechas>
  <fecha>
   <valor>11-11-2020 11:52:08</valor>
   <nombre>FEM</nombre>
  </fecha>
 </Fechas>
 <Locaciones>
  <locacion>
   <descripcion>SANTIAGO</descripcion>
   <nombre>PD</nombre>
   <codigo>SCL</codigo>
  </locacion>
  <locacion>
   <descripcion>NEW YORK</descripcion>
   <nombre>PE</nombre>
   <codigo>JFK</codigo>
  </locacion>
 </Locaciones>
 <Participaciones>
  <participacion>
   <nacion-id>CL</nacion-id>
   <valor-id>77491280-0</valor-id>
   <codigo-pais>CL</codigo-pais>
   <nombres>ALL FORWARDER WORLWIDE LTDA</nombres>
   <direccion>AEROPUERTO C.A.M.B. SECCION COURIER</direccion>
   <comuna>SANTIAGO</comuna>
   <nombre>ALM</nombre>
   <tipo-id>RUT</tipo-id>
  </participacion>
  <participacion>
   <nacion-id>CL</nacion-id>
   <valor-id>8463095-0</valor-id>
   <codigo-pais>CL</codigo-pais>
   <nombres>LAN CHILE</nombres>
   <direccion>PROVIDENCIA 2008 P3</direccion>
   <comuna>PROVIDENCIA</comuna>
   <nombre>TRA</nombre>
   <tipo-id>RUT</tipo-id>
  </participacion>
  <participacion>
   <nacion-id>CL</nacion-id>
   <valor-id>18669961-0</valor-id>
   <codigo-pais>CL</codigo-pais>
   <nombres>NELSON ALEJANDRO OLAVARRIA ALISTE</nombres>
   <direccion>SANTO DOMINGO 168 PUENTE ALTO LOCAL 168</direccion>
   <comuna>SANTIAGO</comuna>
   <nombre>CONS</nombre>
   <tipo-id>RUT</tipo-id>
  </participacion>
  <participacion>
   <nacion-id>US</nacion-id>
   <codigo-pais>US</codigo-pais>
   <nombres>AMAZON</nombres>
   <direccion>SIN INFORMACION</direccion>
   <comuna>SIN INFORMACION</comuna>
   <nombre>CNTE</nombre>
   <tipo-id>RUT</tipo-id>
  </participacion>
  <participacion>
   <nacion-id>CL</nacion-id>
   <valor-id>77491280-0</valor-id>
   <codigo-pais>CL</codigo-pais>
   <nombres>ALL FORWARDER WORLWIDE LTDA</nombres>
   <direccion>AEROPUERTO C.A.M.B. SECCION COURIER</direccion>
   <comuna>SANTIAGO</comuna>
   <nombre>EMI</nombre>
   <tipo-id>RUT</tipo-id>
  </participacion>
 </Participaciones>
 <Referencias>
  <referencia>
   <tipo-referencia>REF</tipo-referencia>
   <numero>84668</numero>
   <fecha>11-11-2020</fecha>
   <valor-id-emisor>77491280-0</valor-id-emisor>
   <nac-id-emisor>CL</nac-id-emisor>
   <tipo-id-emisor>RUT</tipo-id-emisor>
   <emisor>ALL FORWARDER WORLWIDE LTDA</emisor>
   <tipo-documento>MFTOC</tipo-documento>
  </referencia>
 </Referencias>
 <Items>
  <item>
   <marcas>1930728676</marcas>
   <numero-item>1</numero-item>
   <peso-bruto>1.23</peso-bruto>
   <cantidad>1</cantidad>
   <unidad-peso>KGM</unidad-peso>
   <tipo-bulto>61</tipo-bulto>
   <ProdItem>
    <proditem>
     <descripcion>MEMORIA PC</descripcion>
     <unidad-medida>UNI</unidad-medida>
     <cantidad>1</cantidad>
     <valor-declarado>189.99</valor-declarado>
     <moneda>USD</moneda>
    </proditem>
   </ProdItem>
  </item>
 </Items>
 <Cargos>
  <cargo>
   <monto>18.18</monto>
   <descripcion>VALOR FLETE TOTAL</descripcion>
   <tipo-cargo>FLET</tipo-cargo>
   <moneda>USD</moneda>
   <cond-pago>P</cond-pago>
  </cargo>
 </Cargos>
 <Observaciones>
  <observacion>
   <nombre>GRAL</nombre>
   <contenido>contenido observacion</contenido>
  </observacion>
</Observaciones>

</Documento>
]]></xml>
      </urn:enviaDocumento>
   </soapenv:Body>
</soapenv:Envelope>

```
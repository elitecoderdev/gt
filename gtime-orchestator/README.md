Proyecto Arquetipo JBoss EAP 6.4 CXF-Camel
=======================================

# sna-archetype-ws 1.0


# 1- Configuracion Security Domain (Seguridad Web Service) 

1.1 - Se debe crear el datasource `java:jboss/datasources/GLOBAL`

```xml
<datasource jndi-name="java:jboss/datasources/GLOBAL" pool-name="java:jboss/datasources/GLOBAL" enabled="true" use-java-context="true">
    <connection-url>jdbc:oracle:thin:@fresno.aduana.cl:1521:aries</connection-url>
    <driver>oracle-ojdbc6</driver>
    <new-connection-sql>alter session SET NLS_DATE_FORMAT="DD/MM/RR"</new-connection-sql>
    <pool>
        <min-pool-size>3</min-pool-size>
        <max-pool-size>5</max-pool-size>
    </pool>
    <security>
        <user-name>GLOBAL</user-name>
    <password>glo123</password>
    </security>
    <timeout>
        <idle-timeout-minutes>15</idle-timeout-minutes>
    </timeout>
</datasource>
```

1.2- Politica de seguridad para autenticacion Basica: 

```xml
	<security-domain name="aduanaSeguridad" cache-type="default">
		<authentication>
			<login-module code="Database" flag="required">
				<module-option name="dsJndiName" value="java:jboss/datasources/GLOBAL"/>
				<module-option name="principalsQuery" value="select PASSWORD from GLOBAL.USUARIO_EXTERNO where login=?"/>
				<module-option name="rolesQuery" value="select tipo, 'Roles' from GLOBAL.USUARIO_EXTERNO where login=?"/>
			</login-module>
		</authentication>
	</security-domain>
```
1.3- Properties para RabbitMQ

Se debe crear dentro de la carpeta de JBOSS -> JBOSS_PATH/configuration un nuevo directorio con el nombre gtime.
Dentro del directorio antes descrito se debe crear un archivo properties con el nombre gtime-rabbit.properties.
El contenido es el siguiente:

```
gtime.rabbit.http.user=qcourier
gtime.rabbit.http.pass=qcourier
gtime.rabbit.http.url=127.0.0.1
gtime.rabbit.http.port=5672
gtime.rabbit.http.queue=gtimeIngreso
gtime.rabbit.http.exchange=gtimeExchange
gtime.rabbit.http.routingkey=ingreso
gtime.rabbit.opt.autodelete=false
gtime.rabbit.opt.autorecovery=true
gtime.rabbit.opt.consumers=3
```


# 2 - WSDL 

AMBIENTE LOCAL: `http://localhost:8080/arquetipo_ws/service/postal/recCCHPreAlerta?wsdl` 

# 3- Ejemplo del Mensaje SOAP

```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:rec="http://www.aduana.cl/xml/esquemas/postal/recepcion-cch/v1/RecCCHPreAlerta_Request">
   <soapenv:Header/>
   <soapenv:Body>
      <rec:recCCHPreAlerta>
         <rec:fechaEnvio>2019-12-09T11:25:39.210-03:00</rec:fechaEnvio>
         <rec:pstPres>
            <rec:pstPre>
               <rec:trackingNumber>000211385804</rec:trackingNumber>
               <rec:reference>ALS11416924</rec:reference>
               <rec:bagLabel>SINOT00019447CL</rec:bagLabel>
               <rec:shipperName>Miss Huang</rec:shipperName>
               <rec:shipZip>430048</rec:shipZip>
               <rec:shipContryCode>056</rec:shipContryCode>
               <rec:consignee>Loreto Oviedo</rec:consignee>
               <rec:address1>SAN FERNANDO 648  </rec:address1>
               <rec:city>EL BOSQUE</rec:city>
               <rec:state>EL BOSQUE</rec:state>
               <rec:zip>8010968</rec:zip>
               <rec:countryCode>056</rec:countryCode>
               <rec:email>loreto.ovi@gmail.com</rec:email>
               <rec:phone>55555555</rec:phone>
               <rec:pieces>1</rec:pieces>
               <rec:totalWeight>0,11</rec:totalWeight>
               <rec:weightUom>KGS</rec:weightUom>
               <rec:totalValue>5,00</rec:totalValue>
               <rec:currency>USD</rec:currency>
               <rec:incoterms>DDP</rec:incoterms>
               <rec:service>28</rec:service>
               <rec:itemDescription>Bluetooth phone Earphones</rec:itemDescription>
               <rec:itemHsCode>4203301090</rec:itemHsCode>
               <rec:itemQuantity>20</rec:itemQuantity>
               <rec:itemValue>19,77000000</rec:itemValue>
               <rec:mawb>045-06549535</rec:mawb>
            </rec:pstPre>
         </rec:pstPres>
      </rec:recCCHPreAlerta>
   </soapenv:Body>
</soapenv:Envelope>

```
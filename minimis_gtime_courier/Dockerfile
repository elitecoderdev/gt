FROM public.ecr.aws/lambda/nodejs:20

# Instalar Oracle Instant Client y dependencias del sistema
RUN yum update -y && \
    yum install -y libaio unzip && \
    yum clean all

# Descargar e instalar Oracle Instant Client
RUN mkdir -p /opt/oracle && \
    cd /opt/oracle && \
    curl -o instantclient-basiclite.zip https://download.oracle.com/otn_software/linux/instantclient/instantclient-basiclite-linux.x64-21.1.0.0.0.zip && \
    curl -o instantclient-sdk.zip https://download.oracle.com/otn_software/linux/instantclient/instantclient-sdk-linux.x64-21.1.0.0.0.zip && \
    unzip instantclient-basiclite.zip && \
    unzip instantclient-sdk.zip && \
    rm -f *.zip && \
    cd /opt/oracle/instantclient_21_1 && \
    ln -s libclntsh.so.21.1 libclntsh.so && \
    ln -s libocci.so.21.1 libocci.so

# Configurar variables de entorno para Oracle
ENV ORACLE_BASE=/opt/oracle/instantclient_21_1
ENV LD_LIBRARY_PATH=$ORACLE_BASE:$LD_LIBRARY_PATH
ENV TNS_ADMIN=$ORACLE_BASE
ENV OCI_LIB_DIR=$ORACLE_BASE
ENV OCI_INC_DIR=$ORACLE_BASE/sdk/include

# Copiar package files
COPY package*.json ./
RUN npm ci --only=production

# Copiar código fuente compilado
COPY dist/ ${LAMBDA_TASK_ROOT}/dist/
COPY src/ ${LAMBDA_TASK_ROOT}/src/

# El handler se configura en la función Lambda
# Los handlers están en dist/handlers/
CMD [ "dist/handlers/orchestrator.handler" ]


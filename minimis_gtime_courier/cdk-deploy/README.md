# CDK Deployment para GTIME Courier

Este directorio contiene la infraestructura como código usando AWS CDK (Cloud Development Kit).

## Estructura

```
cdk-deploy/
├── bin/
│   └── app.ts              # Punto de entrada de CDK
├── lib/
│   └── gtime-courier-stack.ts  # Stack principal con todos los recursos
├── test/                   # Tests de CDK
├── cdk.json                # Configuración de CDK
├── tsconfig.json           # Configuración TypeScript
└── package.json            # Dependencias
```

## Requisitos Previos

1. Node.js >= 18.x
2. AWS CLI configurado
3. Credenciales AWS configuradas
4. CDK CLI instalado globalmente: `npm install -g aws-cdk`

## Instalación

```bash
cd cdk-deploy
npm install
```

## Configuración

Crear archivo `cdk.json` con la configuración:

```json
{
  "app": "npx ts-node --prefer-ts-exts bin/app.ts",
  "watch": {
    "include": ["**"],
    "exclude": [
      "README.md",
      "cdk*.json",
      "**/*.d.ts",
      "**/*.js",
      "tsconfig.json",
      "package*.json",
      "yarn.lock",
      "node_modules"
    ]
  },
  "context": {
    "@aws-cdk/aws-lambda:recognizeLayerVersion": true,
    "@aws-cdk/core:checkSecretUsage": true,
    "@aws-cdk/core:target-partitions": ["aws", "aws-cn"],
    "@aws-cdk-containers/ecs-service-extensions:enableDefaultLogDriver": true,
    "@aws-cdk/aws-ec2:uniqueImdsv2TemplateName": true,
    "@aws-cdk/aws-ecs:arnFormatIncludesClusterName": true,
    "@aws-cdk/aws-iam:minimizePolicies": true,
    "@aws-cdk/core:validateSnapshotRemovalPolicy": true,
    "@aws-cdk/aws-codepipeline:crossAccountKeyAliasStackSafeResourceName": true,
    "@aws-cdk/aws-s3:createDefaultLoggingPolicy": true,
    "@aws-cdk/aws-sns-subscriptions:restrictSqsDescryption": true,
    "@aws-cdk/aws-apigateway:disableCloudWatchRole": true,
    "@aws-cdk/core:enablePartitionLiterals": true,
    "@aws-cdk/aws-events:eventsTargetQueueSameAccount": true,
    "@aws-cdk/aws-iam:standardizedServicePrincipals": true,
    "@aws-cdk/aws-ecs:disableExplicitDeploymentControllerForCircuitBreaker": true,
    "@aws-cdk/aws-iam:importedRoleStackSafeDefaultPolicyName": true,
    "@aws-cdk/aws-s3:serverAccessLogsUseBucketPolicy": true,
    "@aws-cdk/aws-route53-patters:useCertificate": true,
    "@aws-cdk/customresources:installLatestAwsSdkDefault": false,
    "@aws-cdk/aws-rds:databaseProxyUniqueResourceName": true,
    "@aws-cdk/aws-codedeploy:removeAlarmsFromDeploymentGroup": true,
    "@aws-cdk/aws-apigateway:authorizerChangeDeploymentLogicalId": true,
    "@aws-cdk/aws-ec2:launchTemplateDefaultUserData": true,
    "@aws-cdk/aws-secretsmanager:useAttachedSecretResourceNameForArn": true,
    "@aws-cdk/aws-redshift:columnId": true,
    "@aws-cdk/aws-stepfunctions-tasks:enableEmrServicePolicyV2": true,
    "@aws-cdk/aws-ec2:restrictDefaultSecurityGroup": true,
    "@aws-cdk/aws-apigateway:requestValidatorUniqueId": true,
    "@aws-cdk/aws-kms:aliasNameRef": true,
    "@aws-cdk/aws-autoscaling:generateLaunchTemplateInsteadOfLaunchConfig": true,
    "@aws-cdk/core:includePrefixInUniqueNameGeneration": true,
    "@aws-cdk/aws-opensearchservice:enableOpensearchMultiAzWithStandby": true,
    "@aws-cdk/aws-lambda:useLatestRuntimeVersion": true
  }
}
```

## Uso

### Bootstrap CDK (solo la primera vez)

```bash
cdk bootstrap
```

### Ver diferencias

```bash
cdk diff
```

### Sintetizar CloudFormation

```bash
cdk synth
```

### Desplegar

```bash
cdk deploy
```

### Destruir stack

```bash
cdk destroy
```

## Parámetros Requeridos

El stack requiere los siguientes parámetros que deben pasarse como contexto:

```bash
cdk deploy --context databaseHost=your-rds-host.region.rds.amazonaws.com \
           --context databasePort=1521 \
           --context databaseName=DOCUMENTOS \
           --context databaseSecretArn=arn:aws:secretsmanager:region:account:secret:db-credentials \
           --context eventBusName=gtime-courier-bus \
           --context environment=dev
```

O crear un archivo `cdk.context.json`:

```json
{
  "databaseHost": "your-rds-host.region.rds.amazonaws.com",
  "databasePort": 1521,
  "databaseName": "DOCUMENTOS",
  "databaseSecretArn": "arn:aws:secretsmanager:region:account:secret:db-credentials",
  "eventBusName": "gtime-courier-bus",
  "environment": "dev"
}
```

## Recursos Desplegados

- **ECR Repository** - Para imágenes Docker de Lambda
- **EventBridge Bus** - Custom event bus
- **SQS Queues** - Cola de validación con DLQ
- **Lambda Functions** - Orchestrator, Validator, Processor, Tracer
- **Step Functions** - State machines para validación y persistencia
- **API Gateway** - REST API con API Key
- **IAM Roles** - Roles y políticas para Lambda y Step Functions

## Próximos Pasos

1. Configurar parámetros en `cdk.context.json`
2. Ejecutar `cdk bootstrap` si es la primera vez
3. Ejecutar `cdk synth` para validar
4. Ejecutar `cdk deploy` para desplegar





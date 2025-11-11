import * as cdk from 'aws-cdk-lib';
import * as ecr from 'aws-cdk-lib/aws-ecr';
import * as events from 'aws-cdk-lib/aws-events';
import * as targets from 'aws-cdk-lib/aws-events-targets';
import * as lambda from 'aws-cdk-lib/aws-lambda';
import * as apigateway from 'aws-cdk-lib/aws-apigateway';
import * as sqs from 'aws-cdk-lib/aws-sqs';
import * as sfn from 'aws-cdk-lib/aws-stepfunctions';
import * as tasks from 'aws-cdk-lib/aws-stepfunctions-tasks';
import * as iam from 'aws-cdk-lib/aws-iam';
import * as secretsmanager from 'aws-cdk-lib/aws-secretsmanager';
import { Construct } from 'constructs';

export interface GtimeCourierStackProps extends cdk.StackProps {
  databaseHost: string;
  databasePort?: number;
  databaseName: string;
  databaseSecretArn: string;
  eventBusName?: string;
  environment?: string;
}

export class GtimeCourierStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props: GtimeCourierStackProps) {
    super(scope, id, props);

    const environment = props.environment || 'dev';
    const databasePort = props.databasePort || 1521;
    const eventBusName = props.eventBusName || 'gtime-courier-bus';

    // ECR Repository
    const ecrRepository = new ecr.Repository(this, 'LambdaContainerRepository', {
      repositoryName: 'gtime-courier-lambda',
      imageScanOnPush: true,
      lifecycleRules: [
        {
          description: 'Keep last 10 images',
          maxImageCount: 10,
        },
      ],
    });

    // EventBridge Custom Bus
    const eventBus = new events.EventBus(this, 'EventBus', {
      eventBusName,
    });

    // SQS Queue para documentos validados
    const validationDLQ = new sqs.Queue(this, 'ValidationDLQ', {
      queueName: 'gtime-courier-validation-dlq',
      retentionPeriod: cdk.Duration.days(14),
    });

    const validationQueue = new sqs.Queue(this, 'ValidationQueue', {
      queueName: 'gtime-courier-validation-queue',
      visibilityTimeout: cdk.Duration.seconds(300),
      retentionPeriod: cdk.Duration.days(4),
      deadLetterQueue: {
        queue: validationDLQ,
        maxReceiveCount: 3,
      },
    });

    // IAM Role para Lambda Functions
    const lambdaRole = new iam.Role(this, 'LambdaExecutionRole', {
      assumedBy: new iam.ServicePrincipal('lambda.amazonaws.com'),
      managedPolicies: [
        iam.ManagedPolicy.fromAwsManagedPolicyName('service-role/AWSLambdaBasicExecutionRole'),
      ],
    });

    // Permisos para Secrets Manager
    lambdaRole.addToPolicy(
      new iam.PolicyStatement({
        effect: iam.Effect.ALLOW,
        actions: ['secretsmanager:GetSecretValue'],
        resources: [props.databaseSecretArn],
      })
    );

    // Permisos para EventBridge
    lambdaRole.addToPolicy(
      new iam.PolicyStatement({
        effect: iam.Effect.ALLOW,
        actions: ['events:PutEvents'],
        resources: [eventBus.eventBusArn],
      })
    );

    // Permisos para SQS
    lambdaRole.addToPolicy(
      new iam.PolicyStatement({
        effect: iam.Effect.ALLOW,
        actions: ['sqs:SendMessage'],
        resources: [validationQueue.queueArn],
      })
    );

    // Permisos para RDS
    lambdaRole.addToPolicy(
      new iam.PolicyStatement({
        effect: iam.Effect.ALLOW,
        actions: ['rds-db:connect'],
        resources: [`arn:aws:rds-db:${this.region}:${this.account}:dbuser:*/*`],
      })
    );

    // Lambda Function - Orchestrator
    const orchestratorFunction = new lambda.Function(this, 'OrchestratorFunction', {
      functionName: 'gtime-courier-orchestrator',
      runtime: lambda.Runtime.FROM_IMAGE,
      code: lambda.Code.fromEcrImage(ecrRepository, {
        tagOrDigest: 'latest',
        cmd: ['dist.handlers.orchestrator.handler'],
      }),
      handler: lambda.Handler.FROM_IMAGE,
      timeout: cdk.Duration.seconds(30),
      memorySize: 512,
      role: lambdaRole,
      environment: {
        DB_HOST: props.databaseHost,
        DB_PORT: databasePort.toString(),
        DB_NAME: props.databaseName,
        DB_SECRET_ARN: props.databaseSecretArn,
        EVENT_BUS_NAME: eventBusName,
      },
    });

    // Lambda Function - Validator
    const validatorFunction = new lambda.Function(this, 'ValidatorFunction', {
      functionName: 'gtime-courier-validator',
      runtime: lambda.Runtime.FROM_IMAGE,
      code: lambda.Code.fromEcrImage(ecrRepository, {
        tagOrDigest: 'latest',
        cmd: ['dist.handlers.validator.handler'],
      }),
      handler: lambda.Handler.FROM_IMAGE,
      timeout: cdk.Duration.seconds(60),
      memorySize: 512,
      role: lambdaRole,
      environment: {
        DB_HOST: props.databaseHost,
        DB_PORT: databasePort.toString(),
        DB_NAME: props.databaseName,
        DB_SECRET_ARN: props.databaseSecretArn,
      },
    });

    // Lambda Function - Processor
    const processorFunction = new lambda.Function(this, 'ProcessorFunction', {
      functionName: 'gtime-courier-processor',
      runtime: lambda.Runtime.FROM_IMAGE,
      code: lambda.Code.fromEcrImage(ecrRepository, {
        tagOrDigest: 'latest',
        cmd: ['dist.handlers.processor.handler'],
      }),
      handler: lambda.Handler.FROM_IMAGE,
      timeout: cdk.Duration.seconds(300),
      memorySize: 1024,
      role: lambdaRole,
      environment: {
        DB_HOST: props.databaseHost,
        DB_PORT: databasePort.toString(),
        DB_NAME: props.databaseName,
        DB_SECRET_ARN: props.databaseSecretArn,
        EVENT_BUS_NAME: eventBusName,
      },
    });

    // Lambda Function - Tracer
    const tracerFunction = new lambda.Function(this, 'TracerFunction', {
      functionName: 'gtime-courier-tracer',
      runtime: lambda.Runtime.FROM_IMAGE,
      code: lambda.Code.fromEcrImage(ecrRepository, {
        tagOrDigest: 'latest',
        cmd: ['dist.handlers.tracer.handler'],
      }),
      handler: lambda.Handler.FROM_IMAGE,
      timeout: cdk.Duration.seconds(30),
      memorySize: 256,
      role: lambdaRole,
    });

    // IAM Role para Step Functions
    const stepFunctionsRole = new iam.Role(this, 'StepFunctionsRole', {
      roleName: 'gtime-courier-stepfunctions-role',
      assumedBy: new iam.ServicePrincipal('states.amazonaws.com'),
    });

    stepFunctionsRole.addToPolicy(
      new iam.PolicyStatement({
        effect: iam.Effect.ALLOW,
        actions: ['lambda:InvokeFunction'],
        resources: [validatorFunction.functionArn, processorFunction.functionArn],
      })
    );

    stepFunctionsRole.addToPolicy(
      new iam.PolicyStatement({
        effect: iam.Effect.ALLOW,
        actions: ['sqs:SendMessage'],
        resources: [validationQueue.queueArn],
      })
    );

    stepFunctionsRole.addToPolicy(
      new iam.PolicyStatement({
        effect: iam.Effect.ALLOW,
        actions: ['events:PutTargets', 'events:PutRule', 'events:DescribeRule'],
        resources: ['*'],
      })
    );

    // Step Function - Validación
    const validationStateMachine = this.createValidationStateMachine(
      validatorFunction,
      validationQueue,
      stepFunctionsRole
    );

    // Step Function - Persistencia
    const persistenceStateMachine = this.createPersistenceStateMachine(
      processorFunction,
      stepFunctionsRole
    );

    // EventBridge Rule para trigger Step Function de Validación
    new events.Rule(this, 'ValidationStepFunctionRule', {
      eventBus,
      eventPattern: {
        source: ['gtime.courier'],
        detailType: ['documento.recibido'],
      },
      targets: [
        new targets.SfnStateMachine(validationStateMachine, {
          role: stepFunctionsRole,
        }),
      ],
    });

    // EventBridge Rule para Tracer
    new events.Rule(this, 'TraceEventRule', {
      eventBus,
      eventPattern: {
        source: ['gtime.courier'],
      },
      targets: [new targets.LambdaFunction(tracerFunction)],
    });

    // API Gateway
    const api = new apigateway.RestApi(this, 'ApiGateway', {
      restApiName: 'gtime-courier-api',
      description: 'API Gateway for GTIME Courier',
      deployOptions: {
        stageName: environment,
        throttlingRateLimit: 50,
        throttlingBurstLimit: 100,
      },
      defaultCorsPreflightOptions: {
        allowOrigins: apigateway.Cors.ALL_ORIGINS,
        allowMethods: apigateway.Cors.ALL_METHODS,
        allowHeaders: [
          'Content-Type',
          'X-Amz-Date',
          'Authorization',
          'X-Api-Key',
          'X-Amz-Security-Token',
        ],
      },
      apiKeySourceType: apigateway.ApiKeySourceType.HEADER,
    });

    // API Key
    const apiKey = new apigateway.ApiKey(this, 'ApiKey', {
      apiKeyName: 'gtime-courier-api-key',
      enabled: true,
    });

    // Usage Plan
    const usagePlan = new apigateway.UsagePlan(this, 'UsagePlan', {
      usagePlanName: 'gtime-courier-usage-plan',
      apiStages: [
        {
          api,
          stage: api.deploymentStage,
        },
      ],
      throttle: {
        rateLimit: 50,
        burstLimit: 100,
      },
    });

    usagePlan.addApiKey(apiKey);

    // API Gateway Resource
    const documentosResource = api.root.addResource('documentos');
    const integration = new apigateway.LambdaIntegration(orchestratorFunction);
    documentosResource.addMethod('POST', integration, {
      apiKeyRequired: true,
    });

    // Outputs
    new cdk.CfnOutput(this, 'ApiGatewayUrl', {
      value: api.url,
      description: 'API Gateway Endpoint URL',
    });

    new cdk.CfnOutput(this, 'ApiKeyId', {
      value: apiKey.keyId,
      description: 'API Key ID',
    });

    new cdk.CfnOutput(this, 'ECRRepositoryUri', {
      value: ecrRepository.repositoryUri,
      description: 'ECR Repository URI',
    });

    new cdk.CfnOutput(this, 'EventBusName', {
      value: eventBusName,
      description: 'EventBridge Bus Name',
    });

    new cdk.CfnOutput(this, 'ValidationStateMachineArn', {
      value: validationStateMachine.stateMachineArn,
      description: 'Step Function ARN for Validation',
    });

    new cdk.CfnOutput(this, 'PersistenceStateMachineArn', {
      value: persistenceStateMachine.stateMachineArn,
      description: 'Step Function ARN for Persistence',
    });
  }

  private createValidationStateMachine(
    validatorFunction: lambda.Function,
    validationQueue: sqs.Queue,
    role: iam.Role
  ): sfn.StateMachine {
    // Validar Generales
    const validarGenerales = new tasks.LambdaInvoke(this, 'ValidarGenerales', {
      lambdaFunction: validatorFunction,
      payload: sfn.TaskInput.fromObject({
        'gtime.$': '$.gtime',
        validationType: 'generales',
      }),
      resultPath: '$.validacionesGenerales',
    });

    // Validar Nivel 1 (Map State)
    const validarNivel1Map = new sfn.Map(this, 'ValidarNivel1', {
      itemsPath: '$.validacionesNivel1',
      maxConcurrency: 10,
      resultPath: '$.validacionesNivel1',
    });

    const validarNivel1Task = new tasks.LambdaInvoke(this, 'ValidarNivel1Task', {
      lambdaFunction: validatorFunction,
      payload: sfn.TaskInput.fromObject({
        'gtime.$': '$.gtime',
        validationType: 'nivel1',
        'validationItem.$': '$',
      }),
    });

    validarNivel1Map.iterator(validarNivel1Task);

    // Validar Nivel 2 (Map State)
    const validarNivel2Map = new sfn.Map(this, 'ValidarNivel2', {
      itemsPath: '$.validacionesNivel2',
      maxConcurrency: 10,
      resultPath: '$.validacionesNivel2',
    });

    const validarNivel2Task = new tasks.LambdaInvoke(this, 'ValidarNivel2Task', {
      lambdaFunction: validatorFunction,
      payload: sfn.TaskInput.fromObject({
        'gtime.$': '$.gtime',
        validationType: 'nivel2',
        'validationItem.$': '$',
      }),
    });

    validarNivel2Map.iterator(validarNivel2Task);

    // Validar Nivel 3 (Map State)
    const validarNivel3Map = new sfn.Map(this, 'ValidarNivel3', {
      itemsPath: '$.validacionesNivel3',
      maxConcurrency: 10,
      resultPath: '$.validacionesNivel3',
    });

    const validarNivel3Task = new tasks.LambdaInvoke(this, 'ValidarNivel3Task', {
      lambdaFunction: validatorFunction,
      payload: sfn.TaskInput.fromObject({
        'gtime.$': '$.gtime',
        validationType: 'nivel3',
        'validationItem.$': '$',
      }),
    });

    validarNivel3Map.iterator(validarNivel3Task);

    // Agregar Resultados
    const agregarResultados = new tasks.LambdaInvoke(this, 'AgregarResultados', {
      lambdaFunction: validatorFunction,
      payload: sfn.TaskInput.fromObject({
        'validacionesGenerales.$': '$.validacionesGenerales',
        'validacionesNivel1.$': '$.validacionesNivel1',
        'validacionesNivel2.$': '$.validacionesNivel2',
        'validacionesNivel3.$': '$.validacionesNivel3',
        'gtime.$': '$.gtime',
        aggregate: true,
      }),
      resultPath: '$.documentoResponse',
    });

    // Publicar Resultado en SQS
    const publicarResultado = new tasks.SqsSendMessage(this, 'PublicarResultado', {
      queue: validationQueue,
      messageBody: sfn.TaskInput.fromJsonPathAt('$.documentoResponse'),
    });

    // Chain the states
    const definition = validarGenerales
      .next(validarNivel1Map)
      .next(validarNivel2Map)
      .next(validarNivel3Map)
      .next(agregarResultados)
      .next(publicarResultado);

    return new sfn.StateMachine(this, 'ValidationStateMachine', {
      stateMachineName: 'gtime-courier-validation-sm',
      definitionBody: sfn.DefinitionBody.fromChainable(definition),
      role,
      timeout: cdk.Duration.minutes(15),
    });
  }

  private createPersistenceStateMachine(
    processorFunction: lambda.Function,
    role: iam.Role
  ): sfn.StateMachine {
    // Persistir Documento
    const persistirDocumento = new tasks.LambdaInvoke(this, 'PersistirDocumento', {
      lambdaFunction: processorFunction,
      payload: sfn.TaskInput.fromObject({
        'gtime.$': '$.gtime',
        action: 'persistDocument',
      }),
      resultPath: '$.documento',
    });

    // Persistir Relaciones (Map State)
    const persistirRelacionesMap = new sfn.Map(this, 'PersistirRelaciones', {
      itemsPath: '$.persistencias',
      maxConcurrency: 20,
      resultPath: '$.resultadosPersistencia',
    });

    const persistirEntidad = new tasks.LambdaInvoke(this, 'PersistirEntidad', {
      lambdaFunction: processorFunction,
      payload: sfn.TaskInput.fromObject({
        'idDocumento.$': '$.documento.idDocumento',
        'gtime.$': '$.gtime',
        'entityType.$': '$.entityType',
      }),
      retryOnServiceExceptions: true,
    })
      .addRetry({
        errors: ['States.TaskFailed'],
        interval: cdk.Duration.seconds(2),
        maxAttempts: 3,
        backoffRate: 2,
      });

    persistirRelacionesMap.iterator(persistirEntidad);

    // Agregar Resultados Persistencia
    const agregarResultadosPersistencia = new tasks.LambdaInvoke(
      this,
      'AgregarResultadosPersistencia',
      {
        lambdaFunction: processorFunction,
        payload: sfn.TaskInput.fromObject({
          'resultados.$': '$.resultadosPersistencia',
          'documento.$': '$.documento',
          action: 'aggregate',
        }),
        resultPath: '$.resultadoFinal',
      }
    );

    // Evaluar Resultado (Choice State)
    const evaluarResultado = new sfn.Choice(this, 'EvaluarResultado')
      .when(
        sfn.Condition.booleanEquals('$.resultadoFinal.exitoso', false),
        new tasks.LambdaInvoke(this, 'Rollback', {
          lambdaFunction: processorFunction,
          payload: sfn.TaskInput.fromObject({
            'gtime.$': '$.gtime',
            'documento.$': '$.documento',
            action: 'rollback',
          }),
        }).next(new sfn.Fail(this, 'ErrorFinal', { error: 'ProcessingFailed' }))
      )
      .otherwise(
        new tasks.LambdaInvoke(this, 'ActualizarEstado', {
          lambdaFunction: processorFunction,
          payload: sfn.TaskInput.fromObject({
            'idDocumento.$': '$.documento.idDocumento',
            estado: 'PROCESADO',
            action: 'updateEstado',
          }),
        })
      );

    // Chain the states
    const definition = persistirDocumento
      .next(persistirRelacionesMap)
      .next(agregarResultadosPersistencia)
      .next(evaluarResultado);

    return new sfn.StateMachine(this, 'PersistenceStateMachine', {
      stateMachineName: 'gtime-courier-persistence-sm',
      definitionBody: sfn.DefinitionBody.fromChainable(definition),
      role,
      timeout: cdk.Duration.minutes(30),
    });
  }
}


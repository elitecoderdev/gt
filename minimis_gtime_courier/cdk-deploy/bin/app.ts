import * as cdk from 'aws-cdk-lib';
import { GtimeCourierStack } from '../lib/gtime-courier-stack';

const app = new cdk.App();

const context = app.node.tryGetContext('default') || {};

new GtimeCourierStack(app, 'GtimeCourierStack', {
  databaseHost: context.databaseHost || app.node.tryGetContext('databaseHost'),
  databasePort: context.databasePort || app.node.tryGetContext('databasePort') || 1521,
  databaseName: context.databaseName || app.node.tryGetContext('databaseName'),
  databaseSecretArn: context.databaseSecretArn || app.node.tryGetContext('databaseSecretArn'),
  eventBusName: context.eventBusName || app.node.tryGetContext('eventBusName') || 'gtime-courier-bus',
  environment: context.environment || app.node.tryGetContext('environment') || 'dev',
  description: 'GTIME Courier - Aplicación Serverless AWS con TypeScript',
  tags: {
    Project: 'GTIME-Courier',
    Environment: context.environment || app.node.tryGetContext('environment') || 'dev',
  },
});

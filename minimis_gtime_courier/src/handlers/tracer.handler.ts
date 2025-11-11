import { EventBridgeEvent } from 'aws-lambda';
import { Logger } from '@aws-lambda-powertools/logger';
import { Tracer } from '@aws-lambda-powertools/tracer';
import { Metrics } from '@aws-lambda-powertools/metrics';
import { DetalleLog } from '../types/log';

const logger = new Logger({ serviceName: 'gtime-tracer' });
const tracer = new Tracer({ serviceName: 'gtime-tracer' });
const metrics = new Metrics({ namespace: 'GTIMECourier', serviceName: 'tracer' });

export const handler = async (event: EventBridgeEvent<string, any>): Promise<void> => {
  const segment = tracer.getSegment();
  tracer.annotateColdStart();

  try {
    const logDetail: DetalleLog = {
      idPayload: event.detail.idPayload || event.detail.id || 'unknown',
      timestamp: new Date().toISOString(),
      evento: event.detail.evento || event['detail-type'],
      tipoEvento: event['detail-type'],
      metadata: event.detail,
    };

    logger.info('Log event', logDetail);

    // Los logs se envían automáticamente a CloudWatch Logs
    metrics.addMetric('LogEvent', 'Count', 1);
  } catch (error) {
    logger.error('Error logging event', { error });
  } finally {
    segment?.close();
  }
};






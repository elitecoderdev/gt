import { APIGatewayProxyEvent, APIGatewayProxyResult, Context } from 'aws-lambda';
import { Logger } from '@aws-lambda-powertools/logger';
import { Tracer } from '@aws-lambda-powertools/tracer';
import { Metrics } from '@aws-lambda-powertools/metrics';
import { EventBridgeClient, PutEventsCommand } from '@aws-sdk/client-eventbridge';
import { PayloadVO, Gtime, DocumentoRecibidoEvent } from '../types';
import { validateGtimeStructure } from '../utils/validation';

const logger = new Logger({ serviceName: 'gtime-orchestrator' });
const tracer = new Tracer({ serviceName: 'gtime-orchestrator' });
const metrics = new Metrics({ namespace: 'GTIMECourier', serviceName: 'orchestrator' });
const eventBridge = tracer.captureAWSv3Client(new EventBridgeClient({}));

export const handler = async (
  event: APIGatewayProxyEvent,
  context: Context
): Promise<APIGatewayProxyResult> => {
  const segment = tracer.getSegment();
  tracer.annotateColdStart();
  tracer.addServiceNameAnnotation();

  try {
    logger.info('Received request', { requestId: context.requestId });

    // Validar que el body existe
    if (!event.body) {
      return {
        statusCode: 400,
        body: JSON.stringify({ error: 'Request body is required' }),
      };
    }

    // Parsear payload JSON
    const payload: PayloadVO = JSON.parse(event.body);

    // Validar estructura básica
    if (!payload.id || !payload.documento) {
      return {
        statusCode: 400,
        body: JSON.stringify({ error: 'Invalid payload structure. Required: id, documento' }),
      };
    }

    // Validar estructura del documento Gtime
    const validationErrors = validateGtimeStructure(payload.documento);
    if (validationErrors.length > 0) {
      logger.warn('Validation errors', { errors: validationErrors });
      return {
        statusCode: 400,
        body: JSON.stringify({ error: 'Invalid document structure', details: validationErrors }),
      };
    }

    // Preparar objeto Gtime con metadata
    const gtime: Gtime = {
      ...payload.documento,
      idPayload: parseInt(payload.id, 10),
      startTime: Date.now(),
      fechaEncolamiento: new Date().toISOString(),
    };

    // Publicar evento en EventBridge
    const eventDetail: DocumentoRecibidoEvent = {
      source: 'gtime.courier',
      'detail-type': 'documento.recibido',
      detail: {
        idPayload: payload.id,
        gtime,
        timestamp: new Date().toISOString(),
      },
    };

    await eventBridge.send(
      new PutEventsCommand({
        Entries: [
          {
            Source: eventDetail.source,
            DetailType: eventDetail['detail-type'],
            Detail: JSON.stringify(eventDetail.detail),
            EventBusName: process.env.EVENT_BUS_NAME || 'default',
          },
        ],
      })
    );

    metrics.addMetric('DocumentReceived', 'Count', 1);

    logger.info('Document processed successfully', {
      idPayload: payload.id,
      nroEncabezado: gtime.nroEncabezado,
    });

    return {
      statusCode: 200,
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        message: 'Documento recibido correctamente',
        idPayload: payload.id,
      }),
    };
  } catch (error) {
    logger.error('Error processing request', { error });

    metrics.addMetric('ProcessingError', 'Count', 1);

    return {
      statusCode: 500,
      body: JSON.stringify({
        error: 'Internal server error',
        message: error instanceof Error ? error.message : 'Unknown error',
      }),
    };
  } finally {
    segment?.close();
  }
};






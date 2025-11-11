import { SQSEvent, SQSRecord } from 'aws-lambda';
import { Logger } from '@aws-lambda-powertools/logger';
import { Tracer } from '@aws-lambda-powertools/tracer';
import { Metrics } from '@aws-lambda-powertools/metrics';
import { DocumentoResponse } from '../types';
import { PersistenciaService } from '../services/persistencia.service';
import { initializeDatabase, closeDatabase } from '../config/database';

const logger = new Logger({ serviceName: 'gtime-processor' });
const tracer = new Tracer({ serviceName: 'gtime-processor' });
const metrics = new Metrics({ namespace: 'GTIMECourier', serviceName: 'processor' });

export const handler = async (event: SQSEvent): Promise<void> => {
  const segment = tracer.getSegment();
  tracer.annotateColdStart();
  tracer.addServiceNameAnnotation();

  try {
    await initializeDatabase();

    for (const record of event.Records) {
      await processRecord(record);
    }
  } catch (error) {
    logger.error('Error processing records', { error });
    metrics.addMetric('ProcessingError', 'Count', 1);
    throw error;
  } finally {
    await closeDatabase();
    segment?.close();
  }
};

async function processRecord(record: SQSRecord): Promise<void> {
  try {
    logger.info('Processing record', { messageId: record.messageId });

    const documentoResponse: DocumentoResponse = JSON.parse(record.body);

    // Evaluar resultado de validación
    if (documentoResponse.estadoFinal === 'RECHAZADO') {
      logger.info('Document rejected, updating queue status', {
        nroDocumento: documentoResponse.nroDocumento,
      });
      // Actualizar estado en cola
      metrics.addMetric('DocumentRejected', 'Count', 1);
      return;
    }

    // Documento aceptado - proceder con persistencia
    logger.info('Document accepted, starting persistence', {
      nroDocumento: documentoResponse.nroDocumento,
    });

    const persistenciaService = new PersistenciaService();
    const resultado = await persistenciaService.persistirDocumento(documentoResponse.gtime);

    if (resultado.exitoso) {
      logger.info('Document persisted successfully', {
        nroDocumento: documentoResponse.nroDocumento,
      });
      metrics.addMetric('DocumentPersisted', 'Count', 1);
    } else {
      logger.error('Error persisting document', {
        nroDocumento: documentoResponse.nroDocumento,
        errores: resultado.errores,
      });
      metrics.addMetric('PersistenceError', 'Count', 1);
      // Ejecutar rollback si es necesario
      await persistenciaService.rollback(documentoResponse.gtime);
    }
  } catch (error) {
    logger.error('Error processing record', { error, messageId: record.messageId });
    throw error;
  }
}






import { EventBridgeEvent } from 'aws-lambda';
import { Logger } from '@aws-lambda-powertools/logger';
import { Tracer } from '@aws-lambda-powertools/tracer';
import { Metrics } from '@aws-lambda-powertools/metrics';
import { DocumentoRecibidoEvent, Gtime, Validacion, DocumentoResponse } from '../types';
import { ValidacionService } from '../services/validacion.service';

const logger = new Logger({ serviceName: 'gtime-validator' });
const tracer = new Tracer({ serviceName: 'gtime-validator' });
const metrics = new Metrics({ namespace: 'GTIMECourier', serviceName: 'validator' });

export const handler = async (
  event: EventBridgeEvent<'documento.recibido', DocumentoRecibidoEvent['detail']>
): Promise<void> => {
  const segment = tracer.getSegment();
  tracer.annotateColdStart();
  tracer.addServiceNameAnnotation();

  try {
    logger.info('Processing validation', { idPayload: event.detail.idPayload });

    const gtime: Gtime = event.detail.gtime;
    const validacionService = new ValidacionService();

    // Ejecutar validaciones generales (secuenciales)
    const validacionesGenerales = await validacionService.validarGenerales(gtime);

    // Ejecutar validaciones específicas nivel 1 (paralelas)
    const validacionesNivel1 = await validacionService.validarNivel1(gtime);

    // Ejecutar validaciones específicas nivel 2 (paralelas)
    const validacionesNivel2 = await validacionService.validarNivel2(gtime);

    // Ejecutar validaciones específicas nivel 3 (paralelas)
    const validacionesNivel3 = await validacionService.validarNivel3(gtime);

    // Agregar todas las validaciones
    const todasValidaciones: Validacion[] = [
      ...validacionesGenerales,
      ...validacionesNivel1,
      ...validacionesNivel2,
      ...validacionesNivel3,
    ];

    // Determinar estado final
    const tieneErrores = todasValidaciones.some((v) => v.tipo === 'ERROR');
    const estadoFinal: 'ACEPTADO' | 'RECHAZADO' = tieneErrores ? 'RECHAZADO' : 'ACEPTADO';

    // Construir respuesta
    const documentoResponse: DocumentoResponse = {
      nroDocumento: gtime.nroEncabezado,
      listValidaciones: todasValidaciones,
      gtime,
      user: gtime.user,
      estadoFinal,
      contieneFEM: false,
    };

    // Publicar resultado en SQS o EventBridge
    // Esto se manejará en el Step Function

    metrics.addMetric('DocumentValidated', 'Count', 1);
    if (tieneErrores) {
      metrics.addMetric('DocumentRejected', 'Count', 1);
    } else {
      metrics.addMetric('DocumentAccepted', 'Count', 1);
    }

    logger.info('Validation completed', {
      idPayload: event.detail.idPayload,
      estadoFinal,
      totalValidaciones: todasValidaciones.length,
    });
  } catch (error) {
    logger.error('Error validating document', { error, idPayload: event.detail.idPayload });
    metrics.addMetric('ValidationError', 'Count', 1);
    throw error;
  } finally {
    segment?.close();
  }
};






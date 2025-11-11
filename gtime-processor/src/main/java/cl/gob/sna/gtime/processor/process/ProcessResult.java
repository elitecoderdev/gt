package cl.gob.sna.gtime.processor.process;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cl.gob.sna.gtime.vo.TipoValidacion;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Validacion;
 

public class ProcessResult implements Processor {
	Logger log = Logger.getLogger(ProcessResult.class);
	public void process(Exchange exchange) throws Exception {
		DocumentoResponse documento = (DocumentoResponse) exchange.getIn().getBody();

		List<Validacion> listaValidaciones = documento.getListValidaciones();
		List<Validacion> validacionesNoCumplidas = validacionesNoCumplidas(listaValidaciones);

		//Validaciones Totales
		boolean isAceptado = (validacionesNoCumplidas != null && validacionesNoCumplidas.size() == 0);
		//Validar si hay solo Warnings (que no se suman al total de errores pero si deben ser persistidas
		boolean isAceptadoWarn = isValidacionesConWarn(validacionesNoCumplidas);

		if (isAceptadoWarn){
			isAceptado = true;
		}

		exchange.getIn().setHeader("isAceptado", isAceptado);

		documento.getGtime().setEstadoGtime("Aprobado");

		if (!isAceptado){
			exchange.getIn().setHeader("execTime", documento.getGtime().executionTimeMsg());
			documento.getGtime().setEstadoGtime("Rechazado");
		}
		exchange.getIn().setHeader("listaValidacionesFallidas", validacionesNoCumplidas);
		exchange.getIn().setBody(documento);
		String msgLog = "[IdPayload : (" + documento.getGtime().getIdPayload() +") | numReferencia : (" + documento.getGtime().getNumeroReferencia() + ")]";
		exchange.getIn().setHeader("msgLog", msgLog);
	}

	/**
	 *
	 * @param listaValidaciones
	 * @return
	 */
	private List<Validacion> validacionesNoCumplidas(List<Validacion> listaValidaciones){
		List<Validacion> listValidacionesNoCumplidas =  new ArrayList<>();
		for (Validacion validacion : listaValidaciones) {
			if (validacion != null && !validacion.isValido()) {
				listValidacionesNoCumplidas.add(validacion);
			}
		}
		return listValidacionesNoCumplidas;
	}

	/**
	 *
	 * @param validacionesNoCumplidas
	 * @return
	 */
	private boolean isValidacionesConWarn(List<Validacion> validacionesNoCumplidas){
		for (Validacion validacion: validacionesNoCumplidas){
			if (!validacion.getTipo().equals(TipoValidacion.LOCACION_DESCRIPCION_NO_COINCIDE)){
				return false;
			}
		}
		return true;
	}
}

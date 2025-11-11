package cl.gob.sna.gtime.validaciones.ingreso.process;

import cl.gob.sna.gtime.api.vo.Cargo;
import cl.gob.sna.gtime.validaciones.utils.AbstractValidation;
import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ProcessValidarCargos implements Processor {
	Logger log = Logger.getLogger(ProcessValidarCargos.class);

	public void process(Exchange exchange) throws Exception {
		Gtime documento = (Gtime) exchange.getIn().getHeader("documento");
		DocumentoResponse docResponse = exchange.getIn().getHeader("docResponse", DocumentoResponse.class);

		if (documento.getCargos() != null && documento.getCargos().getCargo() != null
				&& !documento.getCargos().getCargo().isEmpty()) {

			List<Cargo> listCargos = documento.getCargos().getCargo();

			int ordinality = 1;

			for (Cargo cargo : listCargos) {
				if (!StringValidation.isNull(cargo.getTarifa())
						&& !this.validarTarifaNumerica(cargo.getTarifa())){
					Validacion val = new Validacion(TipoValidacion.CARGOS_TARIFA_MAYOR_ZERO, false, new Date());
					val.setTexto(TipoValidacion.CARGOS_TARIFA_MAYOR_ZERO.getTexto().
							replace("[1?]", String.valueOf(ordinality)));
					docResponse.getListValidaciones().add(val);
				}
				if (StringValidation.isNull(cargo.getTipoCargo())) {
					Validacion val = new Validacion(TipoValidacion.CARGOS_SIN_TIPO_CARGO, false, new Date());
					val.setTexto(TipoValidacion.CARGOS_SIN_TIPO_CARGO.getTexto().
							replace("[1?]", String.valueOf(ordinality)));
					docResponse.getListValidaciones().add(val);
				} else {
					if (!cargo.getTipoCargo().equalsIgnoreCase("FLET")) {
						if (StringValidation.isNull(cargo.getMonto())) {
							Validacion val = new Validacion(TipoValidacion.CARGOS_SIN_VALOR_MONTO, false, new Date());
							val.setTexto(TipoValidacion.CARGOS_SIN_VALOR_MONTO.getTexto().
									replace("[1?]", String.valueOf(ordinality)));
							docResponse.getListValidaciones().add(val);
						}

						if (StringValidation.isNull(cargo.getMoneda())) {
							Validacion val = new Validacion(TipoValidacion.CARGOS_SIN_TIPO_MONEDA, false, new Date());
							val.setTexto(TipoValidacion.CARGOS_SIN_TIPO_MONEDA.getTexto().
									replace("[1?]", String.valueOf(ordinality)));
							docResponse.getListValidaciones().add(val);
						}
					} else {
						if (StringValidation.isNull(cargo.getMonto())) {
							Validacion val = new Validacion(TipoValidacion.CARGOS_FLET_SIN_MONTO, false, new Date());
							docResponse.getListValidaciones().add(val);
						}
						if (StringValidation.isNull(cargo.getMoneda())) {
							Validacion val = new Validacion(TipoValidacion.CARGOS_FLET_SIN_MONEDA, false, new Date());
							docResponse.getListValidaciones().add(val);
						}
					}

					if (StringValidation.isNull(cargo.getDescripcion())) {
						Validacion val = new Validacion(TipoValidacion.CARGOS_SIN_DESC, false, new Date());
						val.setTexto(TipoValidacion.CARGOS_SIN_DESC.getTexto().
								replace("[1?]", String.valueOf(ordinality)));
						docResponse.getListValidaciones().add(val);
					}
				}

				if (!StringValidation.isNumeric(cargo.getMonto())
						&& new BigDecimal(cargo.getMonto()).compareTo(BigDecimal.ZERO) <= 0){
					Validacion val = new Validacion(TipoValidacion.CARGOS_SIN_MONTO, false, new Date());
					val.setTexto(TipoValidacion.CARGOS_SIN_MONTO.getTexto().
							replace("[1?]", String.valueOf(ordinality)));
					docResponse.getListValidaciones().add(val);
				}

				ordinality++;
			}
		}
		exchange.getIn().setHeader("docResponse", docResponse);
	}

	/**
	 *
	 * @param tarifa
	 * @return
	 */
	public boolean validarTarifaNumerica(String tarifa){
		if (StringValidation.isNull(tarifa)) return false;
		if (!StringValidation.isNumeric(tarifa)) return false;
		if (new BigDecimal(tarifa).compareTo(BigDecimal.ZERO) <= 0) return false;
		return true;
	}

}

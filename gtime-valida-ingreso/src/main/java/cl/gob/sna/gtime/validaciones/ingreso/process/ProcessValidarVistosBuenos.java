package cl.gob.sna.gtime.validaciones.ingreso.process;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.api.vo.Vb;
import cl.gob.sna.gtime.api.vo.Vbs;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ProcessValidarVistosBuenos implements Processor {

	Logger log = Logger.getLogger(ProcessValidarVistosBuenos.class);
	public void process(Exchange exchange) throws Exception {
		DocumentoResponse docResponse = (DocumentoResponse) exchange.getIn().getHeader("docResponse");

		try{
			Gtime gtime = docResponse.getGtime();

			if (gtime != null && gtime.getVistosBuenos() != null){
				Vbs vb = gtime.getVistosBuenos();

				if (vb != null && vb.getVb() != null && !vb.getVb().isEmpty()){
					List<Vb> vBuenos = vb.getVb();
					List<String> tipos = Arrays.asList("SAG", "SALUD");

					int ordinality = 1;

					for (Vb vistoBueno : vBuenos){
						if (!tipos.contains(vistoBueno.getTipoVb())){
							Validacion val = new Validacion(TipoValidacion.VISTOSBUENOS_NO_VALIDO_TIPO, false, new Date());
							val.setTexto(TipoValidacion.VISTOSBUENOS_NO_VALIDO_TIPO.getTexto().
									replace("[1?]", String.valueOf(ordinality)));
							docResponse.getListValidaciones().add(val);
						}
						ordinality++;
					}
				}
			}
		}catch (Exception e){
			e.getStackTrace();
		}finally {
			exchange.getIn().setHeader("docResponse", docResponse);
		}
	}

}

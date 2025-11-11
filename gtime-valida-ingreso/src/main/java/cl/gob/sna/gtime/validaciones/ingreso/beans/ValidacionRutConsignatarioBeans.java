package cl.gob.sna.gtime.validaciones.ingreso.beans;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.api.vo.Participacion;
import cl.gob.sna.gtime.validaciones.genericas.service.ValidacionConsignatarioService;
import cl.gob.sna.gtime.validaciones.utils.RutValidation;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import java.util.List;
import java.util.Map;

public class ValidacionRutConsignatarioBeans implements Processor {
    final Logger LOG = Logger.getLogger(ValidacionRutConsignatarioBeans.class);

    @Autowired
    private ValidacionConsignatarioService validacionConsignatarioService;

    @Override
    public void process(Exchange exchange) throws Exception {
        DocumentoResponse docResponse = exchange.getIn().getHeader("docResponse",DocumentoResponse.class);

        Gtime gtime = docResponse.getGtime();

        List<Participacion> participacionList = null;

        if (gtime != null && gtime.getParticipaciones() != null &&
                gtime.getParticipaciones().getParticipacion() != null &&
                !gtime.getParticipaciones().getParticipacion().isEmpty()){

            participacionList = gtime.getParticipaciones().getParticipacion();

            Participacion consignatario = this.getConsignatarioFromPart(participacionList);

            if (consignatario != null) {
                String numeroRef = gtime.getNumeroReferencia();
                String tipoAccion = gtime.getTipoAccion();
                String tipoDoc = gtime.getTipo();

                Date fem = docResponse.getFem();

                Map<String, String> detalleRut = RutValidation.getRutyDV(consignatario.getValorId());
                Map.Entry<String,String> entry = detalleRut.entrySet().iterator().next();

                String rut = entry.getKey();
                String dv = entry.getValue();

                Map<String, String> respuesta = validacionConsignatarioService.
                        getValidaRUTDocumento(numeroRef, tipoDoc, tipoAccion, rut, dv, fem);
                String codSRCEI = (String) respuesta.get("COD_MSJ");

               if (!codSRCEI.equalsIgnoreCase("OK")){
                   String errSRCEI =  (String) respuesta.get("MSJ");
                   Validacion val = new Validacion(TipoValidacion.CONS_NO_VALIDO_SRCEI, false, new Date());
                   val.setTexto(val.getTexto().replace("[1?]", errSRCEI));
                   docResponse.getListValidaciones().add(val);
               }
            }
        }
    }

    /**
     *
     * @param listParticipantes
     * @return
     */
    private Participacion getConsignatarioFromPart(List<Participacion> listParticipantes){
        for (Participacion participacion : listParticipantes) {
            if (participacion.getNombre().equals("CONS")){
                return participacion;
            }
        }
        return null;
    }
}

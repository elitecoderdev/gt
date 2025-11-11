package cl.gob.sna.gtime.validaciones.ingreso.beans;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.api.vo.Transbordo;
import cl.gob.sna.gtime.validaciones.genericas.service.ValidacionTransbordoService;
import cl.gob.sna.gtime.validaciones.repository.dto.LocacionTo;
import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ValidacionTransbordosBeans implements Processor {
    final Logger LOG = Logger.getLogger(ValidacionTransbordosBeans.class);

    @Autowired
    private ValidacionTransbordoService validacionTransbordoService;

    @Override
    public void process(Exchange exchange) throws Exception {
        DocumentoResponse docResponse = (DocumentoResponse) exchange.getIn().getHeader("docResponse");
        Gtime documento = (Gtime) exchange.getIn().getHeader("documento");

        try{
            if (documento.getTransbordos() != null && documento.getTransbordos().getTransbordo() != null
                    && !documento.getTransbordos().getTransbordo().isEmpty()) {

                List<Transbordo> transbordosList = documento.getTransbordos().getTransbordo();

                String viaTransporte = docResponse.getViaTransporte();
                List<String> vias7 = Arrays.asList("07", "7");

                int ordinality = 1;
                for (Transbordo tra : transbordosList){
                    if (StringValidation.isNull(tra.getCodLugar())){
                        Validacion val = new Validacion(TipoValidacion.TRANSBORDOS_SIN_CODIGO, false, new Date());
                        val.setTexto(TipoValidacion.TRANSBORDOS_SIN_CODIGO.getTexto().
                                replace("[1?]", String.valueOf(ordinality)));
                        docResponse.getListValidaciones().add(val);
                    }else{
                        LocacionTo locacion = null;
                        if (vias7.contains(viaTransporte)){
                            locacion = validacionTransbordoService.
                                    buscaLocacionYDescPorCodigo7(tra.getCodLugar());
                        }else{
                            locacion = validacionTransbordoService.
                                    buscaLocacionYDescPorCodigoIATA(tra.getCodLugar());
                        }
                        if (locacion != null && locacion.getLocacion() != null){
                            if (!locacion.getDescripcion().equalsIgnoreCase(tra.getDescripcionLugar())){
                                Validacion val = new Validacion(TipoValidacion.TRANSBORDOS_NO_COINCIDE_DESC, false, new Date());

                                if(tra.getDescripcionLugar() == null) tra.setDescripcionLugar("");
                                val.setTexto(TipoValidacion.TRANSBORDOS_NO_COINCIDE_DESC.getTexto()
                                        .replace("[1?]", String.valueOf(ordinality))
                                        .replace("[2?]", tra.getDescripcionLugar()));
                                docResponse.getListValidaciones().add(val);
                            }
                        }else{
                            Validacion val = new Validacion(TipoValidacion.TRANSBORDOS_COD_LOC_NO_EXISTE, false, new Date());
                            val.setTexto(TipoValidacion.TRANSBORDOS_COD_LOC_NO_EXISTE.getTexto()
                                    .replace("[1?]", String.valueOf(ordinality))
                                    .replace("[2?]", tra.getCodLugar()));
                            docResponse.getListValidaciones().add(val);
                        }
                    }

                    if (StringValidation.isNull(tra.getFechaArribo())){
                        Validacion val = new Validacion(TipoValidacion.TRANSBORDOS_FECARRIBO_NULA, false, new Date());
                        val.setTexto(TipoValidacion.TRANSBORDOS_FECARRIBO_NULA.getTexto().
                                replace("[1?]", String.valueOf(ordinality)));
                        docResponse.getListValidaciones().add(val);
                    }

                    if (StringValidation.isNull(tra.getDescripcionLugar())){
                        Validacion val = new Validacion(TipoValidacion.TRANSBORDOS_SIN_DESC, false, new Date());
                        val.setTexto(TipoValidacion.TRANSBORDOS_SIN_DESC.getTexto().
                                replace("[1?]", String.valueOf(ordinality)));
                        docResponse.getListValidaciones().add(val);
                    }
                    ordinality++;
                }
            }
        }catch (Exception e){
            e.getStackTrace();
        }finally{
            exchange.getIn().setHeader("docResponse", docResponse);
        }
    }
}

package cl.gob.sna.gtime.validaciones.genericas.process;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.validaciones.genericas.service.DetalleLogService;
import cl.gob.sna.gtime.validaciones.genericas.service.LoginSeguridadService;
import cl.gob.sna.gtime.validaciones.genericas.service.QueueService;
import cl.gob.sna.gtime.validaciones.repository.dto.QueueTo;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessValidacionPayload implements Processor {
    Logger log = Logger.getLogger(ProcessValidacionPayload.class);

    @Autowired
    private QueueService queueService;
    @Autowired
    private DetalleLogService detalleLogService;
    @Autowired
    private LoginSeguridadService loginSeguridadService;

    @Override
    public void process(Exchange exchange) throws Exception {
        Gtime documento = exchange.getIn().getHeader("documento",Gtime.class);

        DocumentoResponse docResponse = new DocumentoResponse();
        List<Validacion> validaciones = new ArrayList<>();
        boolean existeLogin = false;

        long idCola = documento.getIdPayload();
        QueueTo queueTo = queueService.obtenerColaPorIdPayload(idCola);
        if (queueTo != null){
            String loginDigitador = documento.getLogin();
            documento.setLogin(queueTo.getLogin());
            documento.setUser(queueTo.getNombreUsuario());
            documento.setTipoDocumento(queueTo.getTipoDocumento());
            documento.setXml(queueTo.getXML());
            documento.setLoginDigitador(loginDigitador);

            /* DELETE INICIAL*/
            detalleLogService.deleteFromDetalleLogByNumRef(queueTo.getTipoDocumento(), queueTo.getNumeroReferencia(), queueTo.getLogin());
            detalleLogService.deleteFromDocLogRecepcion(queueTo.getTipoDocumento(), queueTo.getNumeroReferencia(), queueTo.getLogin());

            /*BUSCAR EL LOGIN*/
             existeLogin = loginSeguridadService.existeLoginUsuarioSeguridad(queueTo.getLogin());
        }

        if (!existeLogin){
            Validacion validacionLogin = new Validacion(TipoValidacion.USUARIO_NO_EXISTE,  false, new Date());
            validaciones.add(validacionLogin);
        }

        docResponse.setNroDocumento(documento.getNumeroReferencia());
        docResponse.setListValidaciones(validaciones);
        docResponse.setGtime(documento);

        exchange.getIn().setHeader("docResponse", docResponse);

        String msgLog = "[IdPayload : (" + documento.getIdPayload() +") | numReferencia : (" + documento.getNumeroReferencia() + ")]";
        exchange.getIn().setHeader("msgLog", msgLog);
    }
}

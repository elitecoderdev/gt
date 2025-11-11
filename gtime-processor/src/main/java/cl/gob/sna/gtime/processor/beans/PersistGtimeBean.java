package cl.gob.sna.gtime.processor.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.gob.sna.gtime.processor.exceptions.BusinessException;
import cl.gob.sna.gtime.processor.repo.DocParticipacionRepository;
import cl.gob.sna.gtime.processor.util.HelperUtil;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.api.vo.Participacion;
import cl.gob.sna.gtime.api.vo.Referencia;
import cl.gob.sna.gtime.processor.repo.DocDocumentosRepository;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.TipoPersistencia;

public class PersistGtimeBean {

	private static final String TIPO_REFERENCIA_REF = "REF";
	private static final String TIPO_FECHA_EMSION = "FEM";
	private static final String TIPO_PARTICIPACION_EMISOR = "EMI";
	private static final Logger log = LoggerFactory.getLogger(PersistGtimeBean.class);

	@Autowired
	private DocDocumentosRepository repoDocBase;

	@Autowired
	private DocParticipacionRepository repoDocParticipacion;

	public void persistGtimeHeader(@Body DocumentoResponse doc, Exchange exchange) {
		if (doc != null && doc.getGtime() != null) {
			boolean existeDocBase =  false;
			Gtime gtime = doc.getGtime();
			Long idDocBase = null;

			//fecha ok
			Date fechaEmision = HelperUtil.obtieneValorFecha(TIPO_FECHA_EMSION, gtime.getFechas().getFecha());

			//emisor ok
			Participacion emisor = HelperUtil.obtieneParticipantePorTipo(TIPO_PARTICIPACION_EMISOR,
					gtime.getParticipaciones().getParticipacion());

			//referencia ok
			Referencia referencia = HelperUtil.obtenerReferencia(TIPO_REFERENCIA_REF, gtime.getReferencias().
					getReferencia());

			if (emisor != null && referencia != null && fechaEmision != null) {
				long idPersonaEmisor = repoDocParticipacion.getIdPersonaEmisor(emisor.getTipoId(),
						emisor.getNacionId(), emisor.getValorId());

				String nombreEmisor = emisor.getNombres();
				String numeroAceptacion = referencia.getNumero();
				String user = gtime.getUser();

				try {
					existeDocBase = repoDocBase.checkConstraintUniqueGtime(gtime.getTipo(),
							gtime.getNumeroReferencia(), fechaEmision, numeroAceptacion, idPersonaEmisor);
					if (!existeDocBase){
						idDocBase = repoDocBase.insertaGtime(gtime, fechaEmision, idPersonaEmisor, nombreEmisor,
								numeroAceptacion,
								user);
						doc.getGtime().setIdPersistencia("" + idDocBase);
					}
				} catch (Exception e) {
					log.error( " [+] Error persistGtimeHeader: " + e.getMessage());
					e.getStackTrace();
				} finally {
					exchange.getIn().setHeader("idDocBase", idDocBase);
					exchange.getIn().setHeader("fechaEmision", fechaEmision);
					exchange.getIn().setHeader("user", user);
					//exchange.getIn().setHeader("viaTransporte", viaTransporte);
					exchange.getIn().setHeader("existeGtime", existeDocBase);

					Persistencia estadoPersistencia = new Persistencia(TipoPersistencia.DOCDOCUMENTOBASE,
							idDocBase != null, "DOCUMENTOS", "ID");

					List<Persistencia> listaEstadoPersistencia = new ArrayList<>();
					listaEstadoPersistencia.add(estadoPersistencia);

					doc.setListEstadoPersistencia(listaEstadoPersistencia);
					exchange.getIn().setHeader("docResponse", doc);
					exchange.getIn().setHeader("persistencias", listaEstadoPersistencia);
				}
			}
		}
	}
}

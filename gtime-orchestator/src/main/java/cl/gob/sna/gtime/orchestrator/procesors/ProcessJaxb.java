package cl.gob.sna.gtime.orchestrator.procesors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.api.vo.Cargo;
import cl.gob.sna.gtime.api.vo.Cargos;
import cl.gob.sna.gtime.api.vo.Fecha;
import cl.gob.sna.gtime.api.vo.Fechas;
import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.api.vo.Item;
import cl.gob.sna.gtime.api.vo.Items;
import cl.gob.sna.gtime.api.vo.Locacion;
import cl.gob.sna.gtime.api.vo.Locaciones;
import cl.gob.sna.gtime.api.vo.Observacion;
import cl.gob.sna.gtime.api.vo.Observaciones;
import cl.gob.sna.gtime.api.vo.Participacion;
import cl.gob.sna.gtime.api.vo.Participaciones;
import cl.gob.sna.gtime.api.vo.ProdItems;
import cl.gob.sna.gtime.api.vo.Proditem;
import cl.gob.sna.gtime.api.vo.Referencia;
import cl.gob.sna.gtime.api.vo.Referencias;
import cl.gob.sna.gtime.api.vo.Transbordo;
import cl.gob.sna.gtime.api.vo.Transbordos;
import cl.gob.sna.gtime.api.vo.Vb;
import cl.gob.sna.gtime.api.vo.Vbs;
import cl.gob.sna.gtime.orchestrator.vo.jaxb.CargoJaxb;
import cl.gob.sna.gtime.orchestrator.vo.jaxb.DocumentoJaxb;
import cl.gob.sna.gtime.orchestrator.vo.jaxb.FechaJaxb;
import cl.gob.sna.gtime.orchestrator.vo.jaxb.ItemJaxb;
import cl.gob.sna.gtime.orchestrator.vo.jaxb.LocacionJaxb;
import cl.gob.sna.gtime.orchestrator.vo.jaxb.ObservacionJaxb;
import cl.gob.sna.gtime.orchestrator.vo.jaxb.ParticipacionJaxb;
import cl.gob.sna.gtime.orchestrator.vo.jaxb.ProdItemJaxb;
import cl.gob.sna.gtime.orchestrator.vo.jaxb.ProdItemsJaxb;
import cl.gob.sna.gtime.orchestrator.vo.jaxb.ReferenciaJaxb;
import cl.gob.sna.gtime.orchestrator.vo.jaxb.TransbordoJaxb;
import cl.gob.sna.gtime.orchestrator.vo.jaxb.VistoBuenoJaxb;

public class ProcessJaxb implements Processor {

	Logger log = Logger.getLogger(ProcessJaxb.class);

	public void process(Exchange exchange) throws Exception {
		DocumentoJaxb documentoJaxb = (DocumentoJaxb) exchange.getIn().getHeader("jaxbVo");
		String idPayload = (String) exchange.getIn().getHeader("idPayload");

		if (documentoJaxb != null) {
			Gtime gtime = mapper(documentoJaxb);
			gtime.setIdPayload(Long.parseLong(idPayload));

			gtime.setNroEncabezado(documentoJaxb.getNroEncabezado());
			gtime.setXml(documentoJaxb.getXml());
			gtime.setFechaEncolamiento(documentoJaxb.getFechaEncolamiento());
			gtime.setFechaDesencolamiento(String.valueOf(new Date().getTime()));
			gtime.setPwd(documentoJaxb.getPwd());
			gtime.setStartTime(System.currentTimeMillis());

			exchange.getIn().setBody(gtime, Gtime.class);
		}
	}

	//dozer
	private Gtime mapper(DocumentoJaxb g) {
		Gtime gtime = new Gtime();
		procesaHeader(g, gtime);
		procesaFechas(g, gtime);
		procesaLocaciones(g, gtime);
		procesaCargo(g, gtime);
		procesaReferencias(g, gtime);
		procesaItem(g, gtime);
		procesaParticipaciones(g, gtime);
		procesaObservaciones(g, gtime);
		procesaTransbordos(g,gtime);
		procesaVistosBuenos(g,gtime);

		return gtime;
	}

	private void procesaHeader(DocumentoJaxb g, Gtime gtime) {
		gtime.setIdSender(g.getIdSender());
		gtime.setLogin(g.getLogin());
		gtime.setTipoAccion(g.getTipoAccion());
		gtime.setNumeroReferencia(g.getNumeroReferencia());
		gtime.setTipoOperacion(g.getTipoOperacion());
		gtime.setUnidadPeso(g.getUnidadPeso());
		gtime.setParcial(g.getParcial());
		gtime.setMonedaValor(g.getMonedaValor());
		gtime.setTipo(g.getTipo());
		gtime.setVersion(g.getVersion());
		gtime.setUserHostOrigen(g.getUserHostOrigen());
		gtime.setTotalBultos(g.getTotalBultos());
		gtime.setTotalPeso(g.getTotalPeso());
		gtime.setTotalItem(g.getTotalItem());
		gtime.setValorDeclarado(g.getValorDeclarado());
		gtime.setTotalVolumen(g.getTotalVolumen());
		gtime.setUnidadVolumen(g.getUnidadVolumen());
		gtime.setUser(g.getUser());
	}

	private void procesaTransbordos(DocumentoJaxb g, Gtime gtime) {
		if (g.getTransbordos() != null) {
			List<Transbordo> transbordos = new ArrayList<Transbordo>();
			List<TransbordoJaxb> listaTransbordoJaxb = g.getTransbordos().getTransbordo();

			if (listaTransbordoJaxb != null && !listaTransbordoJaxb.isEmpty()) {
				for (TransbordoJaxb jaxb : listaTransbordoJaxb) {
					Transbordo e = new Transbordo();
					e.setCodLugar(jaxb.getCodLugar());
					e.setDescripcionLugar(jaxb.getDescripcion());
					e.setFechaArribo(jaxb.getFechaArribo());					
					transbordos.add(e);
				}
			}
			Transbordos transbordosVo = new Transbordos();
			transbordosVo.setTransbordos(transbordos);			
			gtime.setTransbordos(transbordosVo);
		}
	}
	
	private void procesaVistosBuenos(DocumentoJaxb g, Gtime gtime) {
		if (g.getVistosBuenos() != null) {
			List<Vb> vistosBuenos = new ArrayList<Vb>();
			List<VistoBuenoJaxb> listaVistoBuenoJaxb = g.getVistosBuenos().getVb();

			if (listaVistoBuenoJaxb != null && !listaVistoBuenoJaxb.isEmpty()) {
				for (VistoBuenoJaxb jaxb : listaVistoBuenoJaxb) {
					Vb e = new Vb();
					e.setTipoVb(jaxb.getTipoVb());
					vistosBuenos.add(e);
				}
			}
			Vbs vbs = new Vbs();
			vbs.setVbs(vistosBuenos);
			gtime.setVistosBuenos(vbs);
		}
	}

	private void procesaObservaciones(DocumentoJaxb g, Gtime gtime) {
		if (g.getObservaciones() != null) {
			List<Observacion> observaciones = new ArrayList<Observacion>();
			List<ObservacionJaxb> listaObservacionesJaxb = g.getObservaciones().getObservacion();

			if (listaObservacionesJaxb != null && !listaObservacionesJaxb.isEmpty()) {
				for (ObservacionJaxb jaxb : listaObservacionesJaxb) {
					Observacion e = new Observacion();
					e.setNombre(jaxb.getNombre());
					e.setContenido(jaxb.getContenido());
					observaciones.add(e);
				}
			}
			Observaciones obs = new Observaciones();
			 obs.setObservacion(observaciones);
			gtime.setObservaciones(obs);
		}
	}

	private void procesaParticipaciones(DocumentoJaxb g, Gtime gtime) {
		if (g.getParticipaciones() != null) {
			List<Participacion> participaciones = new ArrayList<Participacion>();
			List<ParticipacionJaxb> listaObservacionesJaxb = g.getParticipaciones().getParticipacion();

			if (listaObservacionesJaxb != null && !listaObservacionesJaxb.isEmpty()) {
				for (ParticipacionJaxb jaxb : listaObservacionesJaxb) {
					Participacion e = new Participacion();
					e.setCodigoPais(jaxb.getCodigoPais());
					e.setComuna(jaxb.getComuna());
					e.setDireccion(jaxb.getDireccion());
					e.setNacionId(jaxb.getNacionID());
					e.setNombre(jaxb.getNombre());
					e.setNombres(jaxb.getNombres());
					e.setTipoId(jaxb.getTipoID());
					e.setValorId(jaxb.getValorID());
					participaciones.add(e);
				}
			}
			Participaciones participacionesVo = new Participaciones();
			participacionesVo.setParticipacion(participaciones);
			gtime.setParticipaciones(participacionesVo);
		}
	}

	private void procesaItem(DocumentoJaxb g, Gtime gtime) {
		if (g.getItems() != null) {
			List<Item> items = new ArrayList<Item>();
			List<ItemJaxb> listaItemsJaxb = g.getItems().getItem();

			if (listaItemsJaxb != null && !listaItemsJaxb.isEmpty()) {
				for (ItemJaxb jaxb : listaItemsJaxb) {
					Item e = new Item();
					e.setCantidad(jaxb.getCantidad());
					e.setDescripcion(jaxb.getDescripcion());
					e.setProdItem(procesaProdItem(jaxb.getProdItem()));
					e.setMarcas(jaxb.getMarcas());
					e.setNumeroItem(jaxb.getNumeroItem());
					e.setPesoBruto(jaxb.getPesoBruto());
					e.setTipoBulto(jaxb.getTipoBulto());
					e.setUnidadPeso(jaxb.getUnidadPeso());
					items.add(e);
				}
			}
			Items itemsVo = new Items();
			itemsVo.setItem(items);
			gtime.setItems(itemsVo);
		}
	}

	private ProdItems procesaProdItem(ProdItemsJaxb prodItem) {
		List<Proditem> listaProdItem = null;
		if (prodItem != null) {
			listaProdItem = new ArrayList<Proditem>();

			List<ProdItemJaxb> listaProdItemJaxb = prodItem.getListaProdItem();
			for (ProdItemJaxb prodItemJaxb : listaProdItemJaxb) {

				Proditem e = new Proditem();
				e.setCantidad(prodItemJaxb.getCantidad());
				e.setDescripcion(prodItemJaxb.getDescripcion());
				e.setMoneda(prodItemJaxb.getMoneda());
				e.setUnidadMedida(prodItemJaxb.getUnidadMedida());
				e.setValorDeclarado(prodItemJaxb.getValorDeclarado());

				listaProdItem.add(e);
			}
		}
		ProdItems proditems = new ProdItems();
		proditems.setProditem(listaProdItem);
		
		return proditems;
	}

	private void procesaReferencias(DocumentoJaxb g, Gtime gtime) {
		if (g.getReferencias() != null) {
			List<Referencia> referencias = new ArrayList<Referencia>();
			List<ReferenciaJaxb> listaReferenciasJaxb = g.getReferencias().getReferencia();

			if (listaReferenciasJaxb != null && !listaReferenciasJaxb.isEmpty()) {
				for (ReferenciaJaxb jaxb : listaReferenciasJaxb) {
					Referencia e = new Referencia();
					e.setEmisor(jaxb.getEmisor());
					e.setFecha(jaxb.getFecha());
					e.setNacIdEmisor(jaxb.getNacIDEmisor());
					e.setNumero(jaxb.getNumero());
					e.setObservaciones(jaxb.getObservaciones());
					e.setTipoDocumento(jaxb.getTipoDocumento());
					e.setTipoIdEmisor(jaxb.getTipoIDEmisor());
					e.setTipoReferencia(jaxb.getTipoReferencia());
					e.setValorIdEmisor(jaxb.getValorIDEmisor());
					referencias.add(e);
				}
			}
			Referencias referenciasVo = new Referencias();
			referenciasVo.setReferencia(referencias);
			gtime.setReferencias(referenciasVo);
		}
	}

	private void procesaCargo(DocumentoJaxb g, Gtime gtime) {
		if (g.getCargos() != null) {
			List<Cargo> cargos = new ArrayList<Cargo>();
			List<CargoJaxb> listaCargosJaxb = g.getCargos().getCargo();

			if (listaCargosJaxb != null && !listaCargosJaxb.isEmpty()) {
				for (CargoJaxb cargoJaxb : listaCargosJaxb) {
					Cargo e = new Cargo();
					e.setTipoCargo(cargoJaxb.getTipoCargo());
					e.setMoneda(cargoJaxb.getMoneda());
					e.setDescripcion(cargoJaxb.getDescripcion());
					e.setCondPago(cargoJaxb.getCondPago());
					e.setObservaciones(cargoJaxb.getObservaciones());
					e.setTarifa(cargoJaxb.getTarifa());
					e.setMonto(cargoJaxb.getMonto());

					cargos.add(e);
				}
			}
			Cargos cargosVo = new Cargos();
			cargosVo.setCargo(cargos);
			gtime.setCargos(cargosVo);
		}
	}

	private void procesaLocaciones(DocumentoJaxb g, Gtime gtime) {
		if (g.getLocaciones() != null) {
			List<Locacion> locaciones = new ArrayList<Locacion>();
			List<LocacionJaxb> listaLocacionesJaxb = g.getLocaciones().getLocacion();

			if (listaLocacionesJaxb != null && !listaLocacionesJaxb.isEmpty()) {
				for (LocacionJaxb locacionJaxb : listaLocacionesJaxb) {
					Locacion e = new Locacion();
					e.setNombre(locacionJaxb.getNombre());
					e.setDescripcion(locacionJaxb.getDescripcion());
					e.setCodigo(locacionJaxb.getCodigo());
					locaciones.add(e);
				}
			}
			Locaciones locacionesVo = new Locaciones();
			locacionesVo.setLocacion(locaciones);
			gtime.setLocaciones(locacionesVo);
		}
	}

	private void procesaFechas(DocumentoJaxb g, Gtime gtime) {
		if (g.getFechas() != null) {
			List<Fecha> fechas = new ArrayList<Fecha>();
			List<FechaJaxb> listaFechasJaxb = g.getFechas().getFecha();

			if (listaFechasJaxb != null && !listaFechasJaxb.isEmpty()) {
				for (FechaJaxb fechaJaxb : listaFechasJaxb) {
					Fecha e = new Fecha();
					e.setNombre(fechaJaxb.getNombre());
					e.setValor(fechaJaxb.getValor());
					fechas.add(e);
				}
			}

			Fechas f = new Fechas();
			f.setFecha(fechas);
			
			gtime.setFechas(f);
		}
	}



}

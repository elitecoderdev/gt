package cl.gob.sna.gtime.validaciones.genericas.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gob.sna.gtime.api.vo.Item;
import cl.gob.sna.gtime.validaciones.repository.DocTranTipoBultoRepository;
import cl.gob.sna.gtime.validaciones.repository.UnidadMedidaRepository;
import cl.gob.sna.gtime.validaciones.utils.StringFormat;
import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;


@Service
public class ValidacionItemsServiceImpl implements ValidacionItemsService {
 
	@Autowired
	private DocTranTipoBultoRepository docTranTipoBultoRepository;
	@Autowired
	private UnidadMedidaRepository unidadMedidaRepository;
	
	@Override
	public List<Validacion> validarNumeroItems(Integer secuencia, Item item) {
		List<Validacion> validaciones = new ArrayList<>();
		if(StringValidation.isNull(item.getNumeroItem()) ) {
  			validaciones.add(new Validacion(TipoValidacion.ITEMS_SIN_NRO_ITEM, false, new Date()));
		}else {
			Integer numeroItem = StringFormat.convertToIntegerIfExceptionToZero(item.getNumeroItem());
			if(numeroItem <=0) {
				validaciones.add(new Validacion(TipoValidacion.ITEMS_NRO_ITEM_NO_CORRESPONDE, false, new Date()));
			}else {
				if(numeroItem.compareTo(secuencia) != 0) {
					validaciones.add(new Validacion(TipoValidacion.ITEMS_NRO_ITEM_CON_SECUENCIA_NO_CORRESPONDE, false, new Date()));
				}
			}
		}
		return validaciones;
	}

	@Override
	public List<Validacion> validarMarcas(Item item) {
		List<Validacion> validaciones = new ArrayList<>();
		if(StringValidation.isNull(item.getMarcas()) ) {
			Validacion val = new Validacion(TipoValidacion.ITEMS_SIN_MARCAS, false, new Date());
			val.setTexto(TipoValidacion.ITEMS_SIN_MARCAS.getTexto().replace("[1?]",item.getNumeroItem()));
			validaciones.add(val);
		}
		return validaciones;
	}

	@Override
	public List<Validacion> validarTipoBulto(Item item) {
		List<Validacion> validaciones = new ArrayList<>();
		if(StringValidation.isNull(item.getTipoBulto()) ) {
  			Validacion val = new Validacion(TipoValidacion.ITEMS_SIN_TIPO_BULTO, false, new Date());
			val.setTexto(TipoValidacion.ITEMS_SIN_TIPO_BULTO.getTexto().replace("[1?]",item.getNumeroItem()));
			validaciones.add(val);
		}else if(!docTranTipoBultoRepository.isDocTranTipoBultoExistente(item.getTipoBulto())){
			Validacion val = new Validacion(TipoValidacion.ITEMS_TIPO_BULTO_NO_EXISTE, false, new Date());
			val.setTexto(TipoValidacion.ITEMS_TIPO_BULTO_NO_EXISTE.getTexto()
					.replace("[1?]",item.getNumeroItem())
					.replace("[2?]",item.getTipoBulto())
					);
			validaciones.add(val);
		}
		return validaciones;
	}

	@Override
	public List<Validacion> validarUnidadPeso(Integer secuencia, String unidadPeso, Item item) {
		List<Validacion> validaciones = new ArrayList<>();
		if(StringValidation.isNull(item.getUnidadPeso()) ) {
  			Validacion val = new Validacion(TipoValidacion.ITEMS_SIN_UNIDAD_PESO, false, new Date());
			val.setTexto(TipoValidacion.ITEMS_SIN_UNIDAD_PESO.getTexto().replace("[1?]",item.getNumeroItem()));
			validaciones.add(val);
		}else if(!unidadMedidaRepository.isUnidadMedidaExistente(item.getUnidadPeso())){
			Validacion val = new Validacion(TipoValidacion.ITEM_UNIDAD_PESO_NO_EXISTE, false, new Date());
			val.setTexto(TipoValidacion.ITEM_UNIDAD_PESO_NO_EXISTE.getTexto()
					.replace("[1?]",item.getNumeroItem())
					.replace("[2?]",item.getUnidadPeso()) 
					);
			validaciones.add(val);
		}else if(!item.getUnidadPeso().equals(unidadPeso)){
			Validacion val = new Validacion(TipoValidacion.ITEM_UNIDAD_PESO_DISTINTA_ENCABEZADO, false, new Date());
			val.setTexto(TipoValidacion.ITEM_UNIDAD_PESO_DISTINTA_ENCABEZADO.getTexto().replace("[1?]",secuencia.toString()));
			validaciones.add(val);
		}
		return validaciones;
	}

 
	 
}

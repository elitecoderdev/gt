package cl.gob.sna.gtime.validaciones.genericas.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gob.sna.gtime.api.vo.Proditem;
import cl.gob.sna.gtime.validaciones.repository.UnidadMedidaRepository;
import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

@Service
public class ValidacionProductoServiceImpl implements ValidacionProductoService {
	private final Logger LOG = Logger.getLogger(ValidacionProductoServiceImpl.class);
	@Autowired
	private UnidadMedidaRepository unidadMedidaRepository;
	
	@Override
	public List<Validacion> validarDescripcion(Integer secuenciaItem, Integer secuencia, Proditem producto) {
		List<Validacion> validaciones = new ArrayList<>();
		if(StringValidation.isNull(producto.getDescripcion()) ) {
			Validacion val = new Validacion(TipoValidacion.PRODUCTO_SIN_DESCRIPCION, false, new Date());
			val.setTexto(TipoValidacion.PRODUCTO_SIN_DESCRIPCION.getTexto()
																	.replace("[1?]",secuencia.toString())
																			.replace("[2?]",secuenciaItem.toString()));
			validaciones.add(val);
		}
		return validaciones;
	}

	@Override
	public List<Validacion> validarUnidadMedida(Integer secuenciaItem, Integer secuencia, Proditem producto) {
		List<Validacion> validaciones = new ArrayList<>();
		if(StringValidation.isNull(producto.getUnidadMedida()) ) {
  			Validacion val = new Validacion(TipoValidacion.PRODUCTO_SIN_UNIDAD_MEDIDA, false, new Date());
			val.setTexto(TipoValidacion.PRODUCTO_SIN_UNIDAD_MEDIDA.getTexto()
																		.replace("[1?]",secuencia.toString())
																		.replace("[2?]",secuenciaItem.toString()));
			validaciones.add(val);
			
		}else if(!unidadMedidaRepository.isUnidadMedidaExistente(producto.getUnidadMedida())){
			Validacion val = new Validacion(TipoValidacion.PRODUCTO_UNIDAD_MEDIDA_NO_EXISTE, false, new Date());
			val.setTexto(TipoValidacion.PRODUCTO_UNIDAD_MEDIDA_NO_EXISTE.getTexto()
					.replace("[1?]",secuencia.toString())
					.replace("[2?]",secuenciaItem.toString()) 
					.replace("[3?]",producto.getUnidadMedida()) 
					);
			validaciones.add(val);
		}
		return validaciones;
	}

	@Override
	public List<Validacion> validarCantidad(Integer secuenciaItem, Integer secuencia, Proditem producto) {
		List<Validacion> validaciones = new ArrayList<>();
		if(StringValidation.isNull(producto.getCantidad()) ) {
  			Validacion val = new Validacion(TipoValidacion.PRODUCTO_SIN_CANTIDAD, false, new Date());
			val.setTexto(TipoValidacion.PRODUCTO_SIN_CANTIDAD.getTexto()
																		.replace("[1?]",secuencia.toString())
																		.replace("[2?]",secuenciaItem.toString()));
			validaciones.add(val);
		} else {
 			try {
				Integer cantidad = Integer.parseInt(producto.getCantidad());
				if(cantidad <= 0) {
					throw new NumberFormatException("Debe ser mayor a 0");
				}
			}catch(NumberFormatException e) {
				Validacion val = new Validacion(TipoValidacion.PRODUCTO_NO_CANTIDAD_MAYOR_CERO, false, new Date());
				val.setTexto(TipoValidacion.PRODUCTO_NO_CANTIDAD_MAYOR_CERO.getTexto()
																			.replace("[1?]",secuencia.toString())
																			.replace("[2?]",secuenciaItem.toString()));
				validaciones.add(val);
			}
		}
		return validaciones;
	}

	@Override
	public List<Validacion> validarValorDeclarado(Integer secuenciaItem, Integer secuencia, Proditem producto) {
		List<Validacion> validaciones = new ArrayList<>();
		if(StringValidation.isNull(producto.getValorDeclarado()) ) {
			Validacion val = new Validacion(TipoValidacion.PRODUCTO_SIN_VALOR_DECLARADO, false, new Date());
			val.setTexto(TipoValidacion.PRODUCTO_SIN_VALOR_DECLARADO.getTexto()
																	.replace("[1?]",secuencia.toString())
																			.replace("[2?]",secuenciaItem.toString()));
			validaciones.add(val);
		}
		return validaciones;
	}

	@Override
	public List<Validacion> validarMoneda(Integer secuenciaItem, Integer secuencia, Proditem producto) {
		List<Validacion> validaciones = new ArrayList<>();
		if(StringValidation.isNull(producto.getMoneda()) ) {
			Validacion val = new Validacion(TipoValidacion.PRODUCTO_SIN_MONEDA, false, new Date());
			val.setTexto(TipoValidacion.PRODUCTO_SIN_MONEDA.getTexto()
																	.replace("[1?]",secuencia.toString())
																			.replace("[2?]",secuenciaItem.toString()));
			validaciones.add(val);
		}
		return validaciones;
	}

 
	 
}

package cl.gob.sna.gtime.validaciones.genericas.service;

import java.util.List;

import cl.gob.sna.gtime.api.vo.Proditem;
import cl.gob.sna.gtime.vo.Validacion;

public interface ValidacionProductoService {

	List<Validacion> validarUnidadMedida(Integer secuenciaItem, Integer secuencia, Proditem producto);

	List<Validacion> validarDescripcion(Integer secuenciaItem, Integer secuencia, Proditem producto);

	List<Validacion> validarCantidad(Integer secuenciaItem, Integer secuencia, Proditem producto);

	List<Validacion> validarValorDeclarado(Integer secuenciaItem, Integer secuencia, Proditem producto);

	List<Validacion> validarMoneda(Integer secuenciaItem, Integer secuencia, Proditem producto);
 
}

package cl.gob.sna.gtime.validaciones.genericas.service;

import java.util.List;

import cl.gob.sna.gtime.api.vo.Item;
import cl.gob.sna.gtime.vo.Validacion;

public interface ValidacionItemsService {
	public List<Validacion> validarNumeroItems(Integer secuencia, Item item);
	public List<Validacion> validarMarcas(Item item);
	public List<Validacion> validarTipoBulto(Item item);
	public List<Validacion> validarUnidadPeso(Integer secuencia, String unidadPeso, Item item);
	
	
}

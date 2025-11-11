package cl.gob.sna.gtime.validaciones.genericas.service;


import cl.gob.sna.gtime.validaciones.repository.dto.LocacionTo;


public interface ValidacionTransbordoService {
    LocacionTo buscaLocacionYDescPorCodigo7(String codigo);
    LocacionTo buscaLocacionYDescPorCodigoIATA(String codigo);
}

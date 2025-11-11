package cl.gob.sna.gtime.validaciones.genericas.service;

public interface DetalleLogService {
    void deleteFromDetalleLogByNumRef(String tipoDoc, String numRef, String login);
    void deleteFromDocLogRecepcion(String tipoDoc, String numRef, String login);
}

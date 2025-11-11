package cl.gob.sna.gtime.orchestrator.exceptions;

public class BusinessException extends Exception {

	private static final long serialVersionUID = -2162530200746613738L;

	public BusinessException(String mensaje) {
		super(mensaje);
	}
	
	public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

package pe.com.mapfre.pocia.infrastructure.exception;

public class AuthorAlreadyExistsException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3801072554713205983L;

	public AuthorAlreadyExistsException(String message) {
        super(message);
    }
}

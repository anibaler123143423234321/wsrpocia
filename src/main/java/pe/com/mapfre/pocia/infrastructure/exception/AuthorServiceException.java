package pe.com.mapfre.pocia.infrastructure.exception;

public class AuthorServiceException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthorServiceException(String message) {
        super(message);
    }
}
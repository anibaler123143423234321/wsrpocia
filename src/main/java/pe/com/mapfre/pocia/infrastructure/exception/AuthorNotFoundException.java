package pe.com.mapfre.pocia.infrastructure.exception;

public class AuthorNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthorNotFoundException(Long authorId) {
        super("Author not found with id: " + authorId);
    }
}

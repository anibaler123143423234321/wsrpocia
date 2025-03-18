package pe.com.mapfre.pocia.infrastructure.exception;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {super(); }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
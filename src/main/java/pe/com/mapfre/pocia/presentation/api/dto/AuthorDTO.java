package pe.com.mapfre.pocia.presentation.api.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class AuthorDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long authorId;
    private String firstName;
    private String lastName;
    private String email;
}

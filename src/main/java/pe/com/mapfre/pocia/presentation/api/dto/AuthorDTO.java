package pe.com.mapfre.pocia.presentation.api.dto;

import lombok.Data;

@Data
public class AuthorDTO {
    private Long authorId;
    private String firstName;
    private String lastName;
    private String email;
}

package pe.com.mapfre.pocia.infrastructure.vertical.rest.poc;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Author2Dto {

    private Long id;

    @NotBlank(message = "First name is mandatory")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "An alphanumeric first name is mandatory")
    @Schema(description = "Descripcion del firsName", example = "Pedro")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "An alphanumeric last name is mandatory")
    @Schema(description = "Descripcion del lastName", example = "Arenas")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "A valid mail address is mandatory")
    @Schema(description = "Descripcion del email", example = "pedroarena@gmail.com")
    private String email;

//    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
//    private List<Book2Dto> books;
}
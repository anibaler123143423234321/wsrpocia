package pe.com.mapfre.pocia.infrastructure.vertical.rest.poc;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Book2Dto {

    private Long bookId;

    @NotBlank(message = "Title is mandatory")
    private String title;

    private String description;

    @NotNull(message = "Genre is mandatory")
    private String genre;

    private BigDecimal price;

    @NotNull(message = "Author is mandatory")
    private Author2Dto author;

}
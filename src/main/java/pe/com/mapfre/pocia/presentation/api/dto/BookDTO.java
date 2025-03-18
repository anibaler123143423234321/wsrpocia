package pe.com.mapfre.pocia.presentation.api.dto;

import java.math.BigDecimal;

import lombok.Data;


@Data
public class BookDTO {
    private Long bookId;
    private String title;
    private String description;
    private String genre;
    private BigDecimal price;
    private AuthorDTO author;
}

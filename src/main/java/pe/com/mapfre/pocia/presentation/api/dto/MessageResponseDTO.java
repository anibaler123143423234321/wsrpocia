package pe.com.mapfre.pocia.presentation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {
    private String exchange;
    private String name;
    private String routingKey;
    private String url;
    private String objects;
}
package pe.com.mapfre.pocia.infrastructure.mesaggins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorConsumerEvent {
    private String url;
    private Object object;
}

package pe.com.mapfre.pocia.infrastructure.mesaggins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String exchange;
    private String name;
    private String key;
    private String url;
    private List<Object> objects;
}
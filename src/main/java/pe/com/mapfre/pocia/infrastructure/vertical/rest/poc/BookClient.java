package pe.com.mapfre.pocia.infrastructure.vertical.rest.poc;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;



@Service
public class BookClient {
	
	@Autowired
	private RestClient restClient;

	String url1 = "http://localhost:8090/book/{id}";
    
    public Book2Dto getBookById(Long id) {
        return restClient.get()
                .uri(url1, id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Book2Dto.class);
    }
    

    public void createBook() {
    	BigDecimal bd1 = new BigDecimal("9");
        String url = "http://localhost:8090/book";
        
        Book2Dto book2Dto = new Book2Dto(9L, "Do Androids 9", "descripcion 9", "Sci-Fi9", bd1, new Author2Dto(1L, "Philip K", "Dick", "editor@nexus.corp"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Book2Dto> request = new HttpEntity<>(book2Dto, headers);

        try {
        	ResponseEntity<Void> response = restClient.post() 
        			  .uri(url) 
        			  .contentType(MediaType.APPLICATION_JSON) 
        			  .body(request) 
        			  .retrieve()
        			  .toBodilessEntity();
        	
        	System.out.println("*************************::"+response.getStatusCode());
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }
}

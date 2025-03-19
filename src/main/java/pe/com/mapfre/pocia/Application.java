package pe.com.mapfre.pocia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import pe.com.mapfre.pocia.core.AppConfig;

@SpringBootApplication(
        scanBasePackages = "pe.com.mapfre.pocia",
        exclude = {MongoAutoConfiguration.class}
)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
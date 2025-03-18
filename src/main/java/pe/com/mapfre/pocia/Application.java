package pe.com.mapfre.pocia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.web.client.RestTemplate;
import pe.com.mapfre.pocia.core.AppConfig;

@SpringBootApplication
@EnableJpaRepositories
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

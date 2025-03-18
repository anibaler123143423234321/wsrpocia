package pe.com.mapfre.pocia.core;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "pe.com.mapfre.pocia" })
public class AppConfig {

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    RestClient restClient() {
        return RestClient.builder()
                //.baseUrl("http://localhost:8090")
                .build();
    }
    
}

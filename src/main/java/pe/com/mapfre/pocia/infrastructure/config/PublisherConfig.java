package pe.com.mapfre.pocia.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;


@Slf4j
@Configuration
public class PublisherConfig {

    /**
     * Nombre de la cola a la cual apuntamos en el application.propiertes
     */
    @Value("${rabbitmq.queue.author.name}")
    private String message;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.event.name}")
    private String eventName;

    @Value("${rabbitmq.routing.key.author}")
    private String routingKey;

    @Bean
    public Queue queue() {
        return new Queue(message, true, false, false);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("pe.com.mapfre.pocia.presentation.api.dto");
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
    /**
     * Elimina todos los mensajes de la cola al iniciar la aplicación.
     *
     * @param rabbitTemplate the rabbit template.
     * @return boolean
     */
    @Bean
    public Boolean clearQueueOnStartup(RabbitTemplate rabbitTemplate) {
        while (true) {
            log.info("Se van a eliminar los mensajes de la: " + message);
            Object messages = rabbitTemplate.receiveAndConvert(message);
            if (messages == null) {
                log.info("La cola está vacía");
                break;
            }
        }
        return true;
    }

    // Agregar getters manualmente
    public String getExchange() {
        return exchange;
    }

    public String getEventName() {
        return eventName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

}

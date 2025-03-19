package pe.com.mapfre.pocia.infrastructure.mesaggins;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pe.com.mapfre.pocia.presentation.api.dto.AuthorDTO;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@EnableRabbit
public class Publisher  {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;

    // Corrected constructor name to match the class
    public Publisher(RabbitTemplate rabbitTemplate, Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    public void send(final AuthorDTO event) {
        rabbitTemplate.convertAndSend(queue.getName(), event);
    }

    public CompletableFuture<Void> sendAsync(final AuthorDTO event) {
        return CompletableFuture.runAsync(() -> {
            try {
                rabbitTemplate.convertAndSend(queue.getName(), event);
            } catch (Exception e) {
                log.error("Error al enviar el author: {}", e.getMessage());
                throw new RuntimeException("Error al enviar el author", e);
            }
        });
    }
}
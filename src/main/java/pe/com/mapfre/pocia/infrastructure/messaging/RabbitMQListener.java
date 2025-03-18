package pe.com.mapfre.pocia.infrastructure.messaging;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.com.mapfre.pocia.presentation.api.dto.AuthorDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.MessageProperties;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RabbitMQListener {

    @Autowired
    private MessageStorageService messageStorageService;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queuesToDeclare = @Queue(name = "author_queue", durable = "true"))
    public void receiveMessage(Message message) {
        try {
            // Extract message properties
            MessageProperties props = message.getMessageProperties();
            String exchange = props.getReceivedExchange();
            String routingKey = props.getReceivedRoutingKey();

            // Parse the message body
            String messageBody = new String(message.getBody());
            AuthorDTO authorDTO = objectMapper.readValue(messageBody, AuthorDTO.class);

            // Store message metadata
            String url = "http://localhost:9091/messages/send/author/" + authorDTO.getAuthorId();
            messageStorageService.storeMessageMetadata(
                    authorDTO.getAuthorId(),
                    exchange,
                    "createAuthor",  // You might want to make this dynamic
                    routingKey,
                    url,
                    messageBody
            );

            // Complete the pending request with the author data
            if (messageStorageService.hasPendingRequest(authorDTO.getAuthorId())) {
                messageStorageService.completeRequest(authorDTO.getAuthorId(), authorDTO);
            }

            // Log the received message with metadata
            log.info("Mensaje recibido:");
            log.info("Exchange: {}", exchange);
            log.info("Name: createAuthor");
            log.info("Key: {}", routingKey);
            log.info("URL: {}", url);
            log.info("Objects: {}", messageBody);

        } catch (Exception e) {
            log.error("Error processing RabbitMQ message: " + e.getMessage(), e);
        }
    }
}
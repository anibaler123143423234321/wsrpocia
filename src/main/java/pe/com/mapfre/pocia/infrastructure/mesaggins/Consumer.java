package pe.com.mapfre.pocia.infrastructure.mesaggins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pe.com.mapfre.pocia.infrastructure.persistence.entity.Author;
import pe.com.mapfre.pocia.infrastructure.persistence.repository.AuthorRepository;
import pe.com.mapfre.pocia.presentation.api.dto.AuthorDTO;

@Component
public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private AuthorRepository authorRepository;

    @RabbitListener(queues = {"${rabbitmq.queue.author.name}"})
    public void receive(@Payload AuthorDTO authorDTO) {
        try {
            logger.info("Received Object: " + authorDTO);
            makeSlow();

            // Convert AuthorDTO to Author entity
            Author author = mapDtoToEntity(authorDTO);
            authorRepository.save(author);

        } catch (DataAccessException e) {
            logger.error("Database access error: " + e.getMessage());
            // Handle specific database access errors
        } catch (Exception e) {
            logger.error("Error processing message: " + e.getMessage());
            // Handle general errors
        }
    }

    private Author mapDtoToEntity(AuthorDTO dto) {
        Author author = new Author();
        author.setAuthorId(dto.getAuthorId());
        author.setFirstName(dto.getFirstName());
        author.setLastName(dto.getLastName());
        author.setEmail(dto.getEmail());
        return author;
    }


    private void makeSlow() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error("Slowdown interrupted: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
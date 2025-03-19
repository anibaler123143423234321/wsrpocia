package pe.com.mapfre.pocia.application.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.mapfre.pocia.application.service.AuthorService;
import pe.com.mapfre.pocia.infrastructure.exception.EntityNotFoundException;
import pe.com.mapfre.pocia.infrastructure.mesaggins.AuthorEvent;
import pe.com.mapfre.pocia.infrastructure.mesaggins.Publisher;
import pe.com.mapfre.pocia.infrastructure.persistence.entity.Author;
import pe.com.mapfre.pocia.infrastructure.persistence.repository.AuthorRepository;
import pe.com.mapfre.pocia.presentation.api.dto.AuthorDTO;

@Service
public class AuthorServiceImpl implements AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final Publisher publisher;

    public AuthorServiceImpl(AuthorRepository authorRepository, Publisher publisher) {
        this.authorRepository = authorRepository;
        this.publisher = publisher;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDTO> findAll() {
        logger.info("Entrando al método findAll");
        try {
            return authorRepository.findAll().stream()
                    .map(author -> modelMapper.map(author, AuthorDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error en findAll: ", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthorDTO> findById(Long id) {
        logger.info("Entrando al método findById con id: {}", id);
        try {
            return authorRepository.findById(id)
                    .map(author -> modelMapper.map(author, AuthorDTO.class));
        } catch (Exception e) {
            logger.error("Error en findById: ", e);
            throw e;
        }
    }

    @Transactional
    @Override
    public AuthorDTO create(AuthorDTO authorDTO) {
        try {
            // Publicar el mensaje y esperar a que la publicación se complete
            CompletableFuture<Void> publishFuture = publisher.sendAsync(authorDTO);
            publishFuture.join();

            return authorDTO;

        } catch (Exception e) {
            logger.error("Error al guardar el author: " + e.getMessage());
            return null;
        }
    }

    private Author convertEventToEntity(AuthorEvent event) {
        // Validar campos requeridos
        if (event.getName() == null || event.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El campo 'name' es obligatorio");
        }

        // Buscar AuthorDTO en la lista de objetos
        AuthorDTO authorDTO = event.getObjects().stream()
                .filter(obj -> obj instanceof AuthorDTO)
                .map(obj -> (AuthorDTO) obj)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró AuthorDTO en los objetos"));

        // Validar email desde el DTO
        if (authorDTO.getEmail() == null || authorDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El campo 'email' es obligatorio");
        }

        // Dividir el nombre
        String[] nameParts = event.getName().trim().split("\\s+", 2);

        return Author.builder()
                .firstName(nameParts[0])
                .lastName(nameParts.length > 1 ? nameParts[1] : "N/A")
                .email(authorDTO.getEmail().trim())
                .build();
    }


//    @Transactional
//    public void deleteById(Long id) {
//        logger.info("Entrando al método deleteById con id: {}", id);
//        try {
//            authorRepository.deleteById(id);
//        } catch (Exception e) {
//            logger.error("Error en deleteById: ", e);
//            throw e;
//        }
//    }
    @Transactional
    public void deleteById(Long id) {
        logger.info("Entrando al método deleteById con id: {}", id);
        try {
            Optional<Author> author = authorRepository.findById(id);
            if (author.isPresent()) {
                authorRepository.deleteById(id);
                logger.info("Autor con id: {} eliminado correctamente", id);
            } else {
                logger.warn("Autor con id: {} no encontrado", id);
                throw new EntityNotFoundException("Autor no encontrado");
            }
        } catch (EntityNotFoundException e) {
            logger.error("Error en deleteById: ", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error en deleteById: ", e);
            throw new RuntimeException("Error al eliminar el autor", e);
        }
    }

}
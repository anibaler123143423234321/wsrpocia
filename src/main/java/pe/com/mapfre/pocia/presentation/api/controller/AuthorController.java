package pe.com.mapfre.pocia.presentation.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.com.mapfre.pocia.application.service.AuthorService;
import pe.com.mapfre.pocia.infrastructure.config.PublisherConfig;
import pe.com.mapfre.pocia.infrastructure.exception.AuthorAlreadyExistsException;
import pe.com.mapfre.pocia.infrastructure.exception.EntityNotFoundException;
import pe.com.mapfre.pocia.infrastructure.mesaggins.AuthorEvent;
import pe.com.mapfre.pocia.presentation.api.dto.AuthorDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/1.0")
@Tag(name = "Authors", description = "Operations pertaining to authors")
public class AuthorController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    private AuthorService authorService;

    @Autowired
    private PublisherConfig publisherConfig;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Operation(summary = "View a list of available authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/authors")
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        logger.info("Entrando al método getAllAuthors");
        try {
            List<AuthorDTO> authors = authorService.findAll();
            return new ResponseEntity<>(authors, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error en getAllAuthors: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get an author by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved author"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/author/{authorId}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long authorId) {
        logger.info("Entrando al método getAuthorById con authorId: {}", authorId);
        try {
            Optional<AuthorDTO> author = authorService.findById(authorId);
            return author.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            logger.error("Error en getAuthorById: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Add a new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/author")
    public ResponseEntity<AuthorEvent> createAuthor(@RequestBody AuthorDTO authorDTO) {
        logger.info("Entrando al método createAuthor");
        try {
            // Crear el AuthorDTO utilizando el servicio
            AuthorDTO createdAuthor = authorService.create(authorDTO);

            // Convertir el AuthorDTO a JSON para asignarlo al campo url
            String authorJson = objectMapper.writeValueAsString(createdAuthor);

            // Construir el AuthorEvent usando los valores obtenidos de PublisherConfig
            AuthorEvent event = AuthorEvent.builder()
                    .exchange(publisherConfig.getExchange())
                    .name(publisherConfig.getEventName())
                    .key(publisherConfig.getRoutingKey())
                    .url(authorJson)
                    .objects(Arrays.asList(createdAuthor))
                    .build();

            return new ResponseEntity<>(event, HttpStatus.CREATED);
        } catch (AuthorAlreadyExistsException e) {
            logger.warn("El autor ya existe: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            logger.error("Error en createAuthor: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/author/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
        logger.info("Entrando al método deleteAuthor con authorId: {}", authorId);
        try {
            authorService.deleteById(authorId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            logger.error("Autor no encontrado: ", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error en deleteAuthor: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
package pe.com.mapfre.pocia.application.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.mapfre.pocia.application.service.AuthorService;
import pe.com.mapfre.pocia.infrastructure.exception.AuthorAlreadyExistsException;
import pe.com.mapfre.pocia.infrastructure.exception.EntityNotFoundException;
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
    public AuthorDTO create(AuthorDTO authorDTO) {

    	logger.info("Entrando al método save");
        try {
            // Verificar si el requestId ya existe
            Optional<AuthorDTO> existingAuthor = authorRepository.findById(authorDTO.getAuthorId())
                    .map(author -> modelMapper.map(author, AuthorDTO.class));
            if (existingAuthor.isPresent()) {
            	// Si el requestId ya existe, lanzar excepción personalizada
                throw new AuthorAlreadyExistsException("El autor con ID " + authorDTO.getAuthorId() + " ya existe.");
            }

            // Si el requestId no existe, proceder con la creación
            Author author = modelMapper.map(authorDTO, Author.class);
            Author savedAuthor = authorRepository.save(author);
            return modelMapper.map(savedAuthor, AuthorDTO.class);
        } catch (Exception e) {
            logger.error("Error en save: ", e);
            throw e;
        }
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

    @Transactional
    public AuthorDTO update(Long id, AuthorDTO authorDTO) {
        logger.info("Entrando al método update con id: {}", id);
        try {
            return authorRepository.findById(id)
                    .map(existingAuthor -> {
                        modelMapper.map(authorDTO, existingAuthor);
                        logger.info(" *************** info de entidad: "+existingAuthor.toString());
                        Author updatedAuthor = authorRepository.save(existingAuthor);
                        return modelMapper.map(updatedAuthor, AuthorDTO.class);
                    })
                    .orElseGet(() -> {
                        authorDTO.setAuthorId(id);
                        return create(authorDTO);
                    });
        } catch (Exception e) {
            logger.error("Error en update: ", e);
            throw e;
        }
    }
}
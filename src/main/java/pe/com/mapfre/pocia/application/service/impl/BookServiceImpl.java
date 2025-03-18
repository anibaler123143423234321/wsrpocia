package pe.com.mapfre.pocia.application.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.mapfre.pocia.application.service.BookService;
import pe.com.mapfre.pocia.infrastructure.persistence.entity.Book;
import pe.com.mapfre.pocia.infrastructure.persistence.repository.BookRepository;
import pe.com.mapfre.pocia.presentation.api.dto.BookDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> findAll() {
        logger.info("Entrando al método findAll");
        try {
            return bookRepository.findAll().stream()
                    .map(book -> modelMapper.map(book, BookDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error en findAll: ", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDTO> findById(Long id) {
        logger.info("Entrando al método findById con id: {}", id);
        try {
            return bookRepository.findById(id)
                    .map(book -> modelMapper.map(book, BookDTO.class));
        } catch (Exception e) {
            logger.error("Error en findById: ", e);
            throw e;
        }
    }

    @Transactional
    public BookDTO save(BookDTO bookDTO) {
    	logger.info("Entrando al método save");
        try {
            // Verificar si el requestId ya existe
            Optional<BookDTO> existingBookOptional = bookRepository.findById(bookDTO.getBookId())
                    .map(book -> modelMapper.map(book, BookDTO.class));
            if (existingBookOptional.isPresent()) {
                // Si el requestId ya existe, devolver el libro existente
                return modelMapper.map(existingBookOptional.get(), BookDTO.class);
            }

            // Si el requestId no existe, proceder con la creación
            Book book = modelMapper.map(bookDTO, Book.class);
            Book savedBook = bookRepository.save(book);
            return modelMapper.map(savedBook, BookDTO.class);
        } catch (Exception e) {
            logger.error("Error en save: ", e);
            throw e;
        }
    }

    @Transactional
    public void deleteById(Long id) {
        logger.info("Entrando al método deleteById con id: {}", id);
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error en deleteById: ", e);
            throw e;
        }
    }

    @Transactional
    public BookDTO update(Long id, BookDTO bookDTO) {
        logger.info("Entrando al método update con id: {}", id);
        try {
            return bookRepository.findById(id)
                    .map(existingBook -> {
                        modelMapper.map(bookDTO, existingBook);
                        Book updatedBook = bookRepository.save(existingBook);
                        return modelMapper.map(updatedBook, BookDTO.class);
                    })
                    .orElseGet(() -> {
                        bookDTO.setBookId(id);
                        return save(bookDTO);
                    });
        } catch (Exception e) {
            logger.error("Error en update: ", e);
            throw e;
        }
    }
}
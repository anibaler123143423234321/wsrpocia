package pe.com.mapfre.pocia.application.service;


import java.util.List;
import java.util.Optional;

import pe.com.mapfre.pocia.presentation.api.dto.BookDTO;

public interface BookService {
    List<BookDTO> findAll();
    Optional<BookDTO> findById(Long id);
    BookDTO save(BookDTO bookDTO);
    void deleteById(Long id);
    BookDTO update(Long id, BookDTO bookDTO);
}
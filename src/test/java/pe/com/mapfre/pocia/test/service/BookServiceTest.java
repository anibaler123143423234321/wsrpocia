package pe.com.mapfre.pocia.test.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import pe.com.mapfre.pocia.application.service.BookService;
import pe.com.mapfre.pocia.infrastructure.persistence.entity.Book;
import pe.com.mapfre.pocia.infrastructure.persistence.repository.BookRepository;
import pe.com.mapfre.pocia.presentation.api.dto.BookDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    public void testFindAll() {
        Book book = new Book();
        BookDTO bookDTO = new BookDTO();
        Mockito.when(bookRepository.findAll()).thenReturn(Collections.singletonList(book));
        Mockito.when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);

        List<BookDTO> books = bookService.findAll();
        assertEquals(1, books.size());
        assertEquals(bookDTO, books.get(0));
    }

    @Test
    public void testFindById() {
        Book book = new Book();
        BookDTO bookDTO = new BookDTO();
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);

        Optional<BookDTO> result = bookService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(bookDTO, result.get());
    }

    @Test
    public void testSave() {
        Book book = new Book();
        BookDTO bookDTO = new BookDTO();
        Mockito.when(modelMapper.map(bookDTO, Book.class)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);

        BookDTO result = bookService.save(bookDTO);
        assertEquals(bookDTO, result);
    }

    @Test
    public void testDeleteById() {
        bookService.deleteById(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testUpdate() {
        Book book = new Book();
        BookDTO bookDTO = new BookDTO();
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(modelMapper.map(bookDTO, Book.class)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);

        BookDTO result = bookService.update(1L, bookDTO);
        assertEquals(bookDTO, result);
    }
}

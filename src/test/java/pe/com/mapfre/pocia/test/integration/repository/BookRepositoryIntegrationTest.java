package pe.com.mapfre.pocia.test.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pe.com.mapfre.pocia.infrastructure.persistence.entity.Author;
import pe.com.mapfre.pocia.infrastructure.persistence.entity.Book;
import pe.com.mapfre.pocia.infrastructure.persistence.repository.BookRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldFindABookById() {
        // given
        Long bookId = 1L;

        // when
        Optional<Book> bookOptional = this.bookRepository.findById(bookId);

        // then
        assertTrue(bookOptional.isPresent());
        assertEquals(1L, bookOptional.get().getBookId());
        assertEquals("The Man in the High Castle", bookOptional.get().getTitle());
    }

    @Test
    void shouldSaveABook() {
    	BigDecimal decimal = new BigDecimal("123.44");
        // given
    	Author authorToSave = Author.builder()
    			.authorId(1L)
    			.build();
    	
        Book bookToSave = Book.builder()
        		.bookId(1L)
        		.title("nuevotitulo")
        		.description("nuevaDescription")
        		.genre("Sci-Fi")
        		.price(decimal)
        		.author(authorToSave)
        		.build();
        bookToSave.setTitle("New Book");
        

        // when
        Book savedBook = this.bookRepository.save(bookToSave);
        Optional<Book> bookOptional = this.bookRepository.findById(savedBook.getBookId());

        // then
        assertNotNull(savedBook);
        assertTrue(bookOptional.isPresent());
        assertEquals(savedBook.getBookId(), bookOptional.get().getBookId());
        assertEquals(bookToSave.getTitle(), bookOptional.get().getTitle());
    }

    @Test
    void shouldUpdateABook() {
        // given
        Book bookToUpdate = new Book();
        bookToUpdate.setBookId(1L);
        bookToUpdate.setTitle("Updated Book Title");

        // when
        Book updatedBook = this.bookRepository.save(bookToUpdate);
        Optional<Book> bookOptional = this.bookRepository.findById(1L);

        // then
        assertNotNull(updatedBook);
        assertTrue(bookOptional.isPresent());
        assertEquals(bookToUpdate.getTitle(), bookOptional.get().getTitle());
    }

    @Test
    void shouldDeleteABook() {
        // given
        Long bookId = 1L;

        // when
        this.bookRepository.deleteById(bookId);
        Optional<Book> bookOptional = this.bookRepository.findById(bookId);

        // then
        assertTrue(bookOptional.isEmpty());
    }
}

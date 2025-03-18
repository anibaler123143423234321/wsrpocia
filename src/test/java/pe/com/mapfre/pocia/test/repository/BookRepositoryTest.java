package pe.com.mapfre.pocia.test.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import pe.com.mapfre.pocia.infrastructure.persistence.entity.Author;
import pe.com.mapfre.pocia.infrastructure.persistence.entity.Book;
import pe.com.mapfre.pocia.infrastructure.persistence.repository.AuthorRepository;
import pe.com.mapfre.pocia.infrastructure.persistence.repository.BookRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:test.properties")
@Sql({"/schema.sql","/data.sql"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void testSaveAndFindById() {
        Author author = new Author();
        author.setAuthorId(67L);
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setEmail("john.doe@example.com");
        Author savedAuthor = authorRepository.save(author);

        Book book = new Book();
        book.setBookId(56L);
        book.setTitle("Sample Book");
        book.setDescription("This is a sample book description.");
        book.setGenre("Fiction");
        book.setPrice(new BigDecimal("19.99"));
        book.setAuthor(savedAuthor);

        Book savedBook = bookRepository.save(book);
        Optional<Book> foundBook = bookRepository.findById(savedBook.getBookId());

        assertTrue(foundBook.isPresent());
        assertEquals("Sample Book", foundBook.get().getTitle());
        assertEquals("This is a sample book description.", foundBook.get().getDescription());
        assertEquals("Fiction", foundBook.get().getGenre());
        assertEquals(new BigDecimal("19.99"), foundBook.get().getPrice());
        assertEquals(savedAuthor, foundBook.get().getAuthor());
    }

    @Test
    public void testDeleteById() {
        Author author = new Author();
        author.setAuthorId(34L);
        author.setFirstName("Jane2");
        author.setLastName("Doe2");
        author.setEmail("jane.doe2@example.com");
        Author savedAuthor = authorRepository.save(author);

        Book book = new Book();
        book.setBookId(35L);
        book.setTitle("Another Book");
        book.setDescription("This is another book description.");
        book.setGenre("Non-Fiction");
        book.setPrice(new BigDecimal("29.99"));
        book.setAuthor(savedAuthor);

        Book savedBook = bookRepository.save(book);
        bookRepository.deleteById(savedBook.getBookId());

        Optional<Book> foundBook = bookRepository.findById(savedBook.getBookId());
        assertFalse(foundBook.isPresent());
    }
}

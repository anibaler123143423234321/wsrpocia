package pe.com.mapfre.pocia.test.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import pe.com.mapfre.pocia.infrastructure.persistence.entity.Author;
import pe.com.mapfre.pocia.infrastructure.persistence.repository.AuthorRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:test.properties")
@Sql({"/schema.sql","/data.sql"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void testSaveAndFindById() {
        Author author = new Author();
        author.setFirstName("John1");
        author.setLastName("Doe1");
        author.setEmail("john1.doe@example.com");

        Author savedAuthor = authorRepository.save(author);
        Optional<Author> foundAuthor = authorRepository.findById(savedAuthor.getAuthorId());

        assertTrue(foundAuthor.isPresent());
        assertEquals("John1", foundAuthor.get().getFirstName());
        assertEquals("Doe1", foundAuthor.get().getLastName());
        assertEquals("john1.doe@example.com", foundAuthor.get().getEmail());
    }

    @Test
    public void testDeleteById() {
        Author author = new Author();
        author.setFirstName("Jane2");
        author.setLastName("Doe2");
        author.setEmail("jane.doe2@example.com");

        Author savedAuthor = authorRepository.save(author);
        authorRepository.deleteById(savedAuthor.getAuthorId());

        Optional<Author> foundAuthor = authorRepository.findById(savedAuthor.getAuthorId());
        assertFalse(foundAuthor.isPresent());
    }
}

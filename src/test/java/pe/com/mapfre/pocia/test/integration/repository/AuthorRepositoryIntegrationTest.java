package pe.com.mapfre.pocia.test.integration.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pe.com.mapfre.pocia.infrastructure.persistence.entity.Author;
import pe.com.mapfre.pocia.infrastructure.persistence.repository.AuthorRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorRepositoryIntegrationTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void shouldFindAnAuthorById() {
        // given
        Long authorId = 1L;

        // when
        Optional<Author> authorOptional = this.authorRepository.findById(authorId);

        // then
        assertTrue(authorOptional.isPresent());
        assertEquals(1L, authorOptional.get().getAuthorId());
        assertEquals("Philip K", authorOptional.get().getFirstName());
    }

    @Test
    void shouldSaveAnAuthor() {
        // given
        Author authorToSave = new Author();
        authorToSave.setAuthorId(5L);
        authorToSave.setLastName("Apellido nuevo");
        authorToSave.setFirstName("New Author");
        authorToSave.setEmail("nuevoemail@gmail.com");

        // when
        Author savedAuthor = this.authorRepository.save(authorToSave);
        Optional<Author> authorOptional = this.authorRepository.findById(savedAuthor.getAuthorId());

        // then
        assertNotNull(savedAuthor);
        assertTrue(authorOptional.isPresent());
        assertEquals(savedAuthor.getAuthorId(), authorOptional.get().getAuthorId());
        assertEquals(authorToSave.getFirstName(), authorOptional.get().getFirstName());
    }

    @Test
    void shouldUpdateAnAuthor() {
        // given
        Author authorToUpdate = Author.builder()
                .authorId(1L)
                .firstName("Philip KP")
                .lastName("Dick")
                .email("editor@nexus.corp")
                .build();

        // when
        Author updatedAuthor = this.authorRepository.save(authorToUpdate);
        Optional<Author> authorOptional = this.authorRepository.findById(1L);
        
        // then
        assertNotNull(updatedAuthor);
        assertTrue(authorOptional.isPresent());
        assertEquals(authorToUpdate.getFirstName(), authorOptional.get().getFirstName());
    }

    @Test
    void shouldDeleteAnAuthor() {
        // given
        Long authorId = 1L;

        // when
        this.authorRepository.deleteById(authorId);
        Optional<Author> authorOptional = this.authorRepository.findById(authorId);

        // then
        assertTrue(authorOptional.isEmpty());
    }
}
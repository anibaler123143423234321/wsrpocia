package pe.com.mapfre.pocia.test.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import pe.com.mapfre.pocia.application.service.AuthorService;
import pe.com.mapfre.pocia.infrastructure.persistence.entity.Author;
import pe.com.mapfre.pocia.infrastructure.persistence.repository.AuthorRepository;
import pe.com.mapfre.pocia.presentation.api.dto.AuthorDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    public void testFindAll() {
        Author author = new Author();
        AuthorDTO authorDTO = new AuthorDTO();
        Mockito.when(authorRepository.findAll()).thenReturn(Collections.singletonList(author));
        Mockito.when(modelMapper.map(author, AuthorDTO.class)).thenReturn(authorDTO);

        List<AuthorDTO> authors = authorService.findAll();
        assertEquals(1, authors.size());
        assertEquals(authorDTO, authors.get(0));
    }

    @Test
    public void testFindById() {
        Author author = new Author();
        AuthorDTO authorDTO = new AuthorDTO();
        Mockito.when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        Mockito.when(modelMapper.map(author, AuthorDTO.class)).thenReturn(authorDTO);

        Optional<AuthorDTO> result = authorService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(authorDTO, result.get());
    }

    @Test
    public void testSave() {
        Author author = new Author();
        AuthorDTO authorDTO = new AuthorDTO();
        Mockito.when(modelMapper.map(authorDTO, Author.class)).thenReturn(author);
        Mockito.when(authorRepository.save(author)).thenReturn(author);
        Mockito.when(modelMapper.map(author, AuthorDTO.class)).thenReturn(authorDTO);

        AuthorDTO result = authorService.create(authorDTO);
        assertEquals(authorDTO, result);
    }

    @Test
    public void testDeleteById() {
        authorService.deleteById(1L);
        Mockito.verify(authorRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testUpdate() {
        Author author = new Author();
        AuthorDTO authorDTO = new AuthorDTO();
        Mockito.when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        Mockito.when(modelMapper.map(authorDTO, Author.class)).thenReturn(author);
        Mockito.when(authorRepository.save(author)).thenReturn(author);
        Mockito.when(modelMapper.map(author, AuthorDTO.class)).thenReturn(authorDTO);

        AuthorDTO result = authorService.update(1L, authorDTO);
        assertEquals(authorDTO, result);
    }
}
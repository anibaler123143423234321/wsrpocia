package pe.com.mapfre.pocia.test.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import pe.com.mapfre.pocia.application.service.AuthorService;
import pe.com.mapfre.pocia.presentation.api.dto.AuthorDTO;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@Sql({"/schema.sql","/data.sql"})
@AutoConfigureMockMvc
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Test
    public void testGetAllAuthors() throws Exception {
      Mockito.when(authorService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/1.0/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetAuthorById() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setAuthorId(1L);
        Mockito.when(authorService.findById(1L)).thenReturn(Optional.of(authorDTO));

        mockMvc.perform(get("/1.0/author/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorId").value(1L));
    }

    @Test
    public void testCreateAuthor() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setAuthorId(1L);
        Mockito.when(authorService.create(Mockito.any(AuthorDTO.class))).thenReturn(authorDTO);

        mockMvc.perform(post("/1.0/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"authorId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorId").value(1L));
    }

    @Test
    public void testUpdateAuthor() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setAuthorId(1L);
        authorDTO.setFirstName("Philip K");
        authorDTO.setLastName("Dick");
        authorDTO.setEmail("editor@nexus.corp");
        Mockito.when(authorService.update(Mockito.anyLong(), Mockito.any(AuthorDTO.class))).thenReturn(authorDTO);

        mockMvc.perform(put("/1.0/author/{\"authorId\"}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                //.content("{\"authorId\":1}"))
                .content(asJsonString(authorDTO)))
                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorId").value(1L));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/1.0/author/1"))
                .andExpect(status().isNoContent());
    }
}
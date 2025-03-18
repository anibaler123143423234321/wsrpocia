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

import pe.com.mapfre.pocia.application.service.BookService;
import pe.com.mapfre.pocia.presentation.api.dto.BookDTO;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@Sql({"/schema.sql","/data.sql"})
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    public void testGetAllBooks() throws Exception {
        Mockito.when(bookService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/1.0/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetBookById() throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookId(1L);
        Mockito.when(bookService.findById(1L)).thenReturn(Optional.of(bookDTO));

        mockMvc.perform(get("/1.0/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookId").value(1L));
    }

    @Test
    public void testCreateBook() throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookId(1L);
        Mockito.when(bookService.save(Mockito.any(BookDTO.class))).thenReturn(bookDTO);

        mockMvc.perform(post("/1.0/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bookId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookId").value(1L));
    }

    @Test
    public void testUpdateBook() throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookId(1L);
        Mockito.when(bookService.update(Mockito.anyLong(), Mockito.any(BookDTO.class))).thenReturn(bookDTO);

        mockMvc.perform(put("/1.0/book/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bookId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookId").value(1L));
    }

    @Test
    public void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/1.0/book/1"))
                .andExpect(status().isNoContent());
    }
}

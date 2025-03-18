package pe.com.mapfre.pocia.presentation.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.com.mapfre.pocia.application.service.BookService;
import pe.com.mapfre.pocia.infrastructure.vertical.rest.poc.Book2Dto;
import pe.com.mapfre.pocia.infrastructure.vertical.rest.poc.BookClient;
import pe.com.mapfre.pocia.presentation.api.dto.BookDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/1.0")
@Tag(name = "Books", description = "Operations pertaining to books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private BookClient bookClient;
    
	@Value("${time.sleep}")
	private String timeSleep;
    
    @Operation(summary = "View a list of available books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/books")
    public List<BookDTO> getAllBooks() {
    	System.out.println("**********************::"+Integer.valueOf(timeSleep));
        logger.info("Entrando al método getAllBooks");
//        Book2Dto libro =  bookClient.getBookById(3L);
//        System.out.println("*******::" + libro.toString());
       
        try {
            return bookService.findAll();
            
           
            
        } catch (Exception e) {
            logger.error("Error en getAllBooks: ", e);
            throw e;
        }
    }

    @Operation(summary = "Get a book by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved book"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/book/{bookId}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long bookId) {
        logger.info("Entrando al método getBookById con bookId: {}", bookId);
//        Book2Dto libro =  bookClient.getBookById(3L);
//        System.out.println("*******::" + libro.toString());
        bookClient.createBook();
        try {
            Optional<BookDTO> book = bookService.findById(bookId);
            return book.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error en getBookById: ", e);
            throw e;
        }
    }

    @Operation(summary = "Add a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/book")
    public BookDTO createBook(@RequestBody BookDTO bookDTO) {
        logger.info("Entrando al método createBook");
        try {
            return bookService.save(bookDTO);
        } catch (Exception e) {
            logger.error("Error en createBook: ", e);
            throw e;
        }
    }

    @Operation(summary = "Update an existing book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/book/{bookId}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long bookId, @RequestBody BookDTO bookDTO) {
        logger.info("Entrando al método updateBook con bookId: {}", bookId);
        try {
            Optional<BookDTO> bookOptional = bookService.findById(bookId);

            if (bookOptional.isPresent()) {
                BookDTO updatedBook = bookService.update(bookId, bookDTO);
                return ResponseEntity.ok(updatedBook);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error en updateBook: ", e);
            throw e;
        }
    }

    @Operation(summary = "Delete a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/book/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        logger.info("Entrando al método deleteBook con bookId: {}", bookId);
        try {
            bookService.deleteById(bookId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error en deleteBook: ", e);
            throw e;
        }
    }
}
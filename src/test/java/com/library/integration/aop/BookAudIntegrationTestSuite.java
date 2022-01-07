package com.library.integration.aop;

import com.library.dao.BookRepository;
import com.library.dao.BooksAudRepository;
import com.library.domain.Book;
import com.library.domain.BooksAud;
import com.library.dto.books.BookDto;
import com.library.service.BookService;
import com.library.status.Auditorium;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookAudIntegrationTestSuite {

    @Autowired
    private BooksAudRepository booksAudRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;


    @BeforeEach
    public void setup() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@gmail.com");
    }

    @Test
    void testSaveBookAud() {
        // Given
        Book book = Book.builder().author("Author")
                .title("Title").yearOfPublication(1948)
                .description("Description").price(20.0)
                .currency("PLN").photo("photo").build();
        // When
        Book savedBook = bookService.saveBook(book);
        // Then
        int bookId = savedBook.getId();
        List<BooksAud> savedAuds = (List<BooksAud>) booksAudRepository.findAll();
        BooksAud bookAud = savedAuds.stream().findFirst().get();
        int audId = bookAud.getId();

        assertEquals(1,savedAuds.size());
        assertEquals("test@gmail.com",bookAud.getAudOwner());
        assertEquals(Auditorium.INSERT, bookAud.getEventType());

        bookRepository.deleteById(bookId);
        booksAudRepository.deleteById(audId);
    }

    @Test
    void testDeleteBookAud() {
        // Given
        Book book = Book.builder().author("Author")
                .title("Title").yearOfPublication(1948)
                .description("Description").price(20.0)
                .currency("PLN").photo("photo").build();
        Book savedBook = bookService.saveBook(book);
        int bookId = savedBook.getId();
        // When
        bookService.deleteBook(bookId);
        // Then
        List<BooksAud> auds = (List<BooksAud>) booksAudRepository.findAll();
        BooksAud saveAud = auds.get(0);
        BooksAud deleteAud = auds.get(1);
        int saveAudId = saveAud.getId();
        int deleteAudId = deleteAud.getId();

        assertEquals(2,auds.size());
        assertEquals("test@gmail.com", deleteAud.getAudOwner());
        assertEquals(Auditorium.DELETE, deleteAud.getEventType());

        booksAudRepository.deleteById(saveAudId);
        booksAudRepository.deleteById(deleteAudId);
    }

    @Test
    void testUpdateBookAud() {
        // Given
        Book book = Book.builder().author("Author")
                .title("Title").yearOfPublication(1948)
                .description("Description").price(20.0)
                .currency("PLN").photo("photo").build();
        Book savedBook = bookRepository.save(book);
        int savedBookId = savedBook.getId();
        BookDto bookDto = BookDto.builder().id(savedBookId).author("Author")
                .title("NewTitle").yearOfPublication(200)
                .description("NewDescription").price(30.0)
                .currency("PLN").photo("photo").build();
        // When
        bookService.updateBook(bookDto);
        // Then
        List<BooksAud> auds = (List<BooksAud>) booksAudRepository.findAll();
        BooksAud updateAud = auds.get(0);
        int updateAudId = updateAud.getId();
        assertEquals(1,auds.size());
        assertEquals("test@gmail.com", updateAud.getAudOwner());
        assertEquals(Auditorium.UPDATE, updateAud.getEventType());

        bookRepository.deleteById(savedBookId);
        booksAudRepository.deleteById(updateAudId);
    }

}

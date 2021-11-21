package com.library.unit;

import com.library.dao.BookRepository;
import com.library.domain.Book;
import com.library.dto.BookDto;
import com.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BookUnitTestSuite {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    void testUpdateBook() {
        // Given
        Book book = new Book("BookTitle","BookAuthor",1948);
        BookDto bookDto = new BookDto("Title","Author",1950,"Description");
        // When
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);
        Book updatedBook = bookService.updateBook(bookDto);
        // Then
        assertEquals("Title",updatedBook.getTitle());
        assertEquals("Author",updatedBook.getAuthor());
        assertEquals(1950,updatedBook.getYearOfPublication());
    }
}

package com.library.unit.service;

import com.library.dao.BookRepository;
import com.library.domain.Book;
import com.library.dto.books.BookDto;
import com.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookUnitTestSuite {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    void testSaveBook() {
        // Given
        Book book = Book.builder().title("BookTitle").author("BookAuthor").yearOfPublication(1948).build();
        when(bookRepository.save(book)).thenReturn(book);
        // When
        Book savedBook = bookService.saveBook(book);
        // Then
        assertEquals("BookTitle", savedBook.getTitle());
        assertEquals("BookAuthor", savedBook.getAuthor());
        assertEquals(1948, savedBook.getYearOfPublication());
    }

    @Test
    void testFindBook() {
        // Given
        Book book = Book.builder().title("BookTitle").author("BookAuthor").yearOfPublication(1948).build();
        when(bookRepository.findById(anyInt())).thenReturn(Optional.ofNullable(book));
        // When
        Book savedBook = bookService.findBook(1);
        // Then
        assertEquals("BookTitle", savedBook.getTitle());
        assertEquals("BookAuthor", savedBook.getAuthor());
        assertEquals(1948, savedBook.getYearOfPublication());
    }

    @Test
    void testDeleteBook() {
        // Given
        doNothing().when(bookRepository).deleteById(anyInt());
        // When
        bookService.deleteBook(1);
        // Then
        verify(bookRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void testFindBooksPerPage_withKeyword() {
        // Given
        String keyword = "BookTitle";
        int page = 1;
        Pageable pageable = PageRequest.of(page - 1, 5);
        Book book1 = Book.builder().title("BookTitle1").author("BookAuthor1").yearOfPublication(1948).build();
        Book book2 = Book.builder().title("BookTitle2").author("BookAuthor2").yearOfPublication(1948).build();
        Book book3 = Book.builder().title("BookTitle3").author("BookAuthor3").yearOfPublication(1948).build();
        Book book4 = Book.builder().title("BookTitle4").author("BookAuthor4").yearOfPublication(1948).build();
        Book book5 = Book.builder().title("BookTitle5").author("BookAuthor5").yearOfPublication(1948).build();
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        books.add(book5);
        Page<Book> booksPage = new PageImpl<>(books);
        when(bookRepository.retrieveAllBooksMatchingKeyword(keyword,pageable)).thenReturn(booksPage);
        // When
        Page<Book> allBooks = bookService.findAllBooks(page, keyword);
        // Then
        assertEquals(5,allBooks.getSize());
    }

    @Test
    void testFindBooksPerPage_withoutKeyword() {
        // Given
        String keyword = null;
        int page = 1;
        Pageable pageable = PageRequest.of(page - 1, 5);
        Book book1 = Book.builder().title("BookTitle1").author("BookAuthor1").yearOfPublication(1948).build();
        Book book2 = Book.builder().title("BookTitle2").author("BookAuthor2").yearOfPublication(1948).build();
        Book book3 = Book.builder().title("BookTitle3").author("BookAuthor3").yearOfPublication(1948).build();
        Book book4 = Book.builder().title("BookTitle4").author("BookAuthor4").yearOfPublication(1948).build();
        Book book5 = Book.builder().title("BookTitle5").author("BookAuthor5").yearOfPublication(1948).build();
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        books.add(book5);
        Page<Book> booksPage = new PageImpl<>(books);
        when(bookRepository.findAll(pageable)).thenReturn(booksPage);
        // When
        Page<Book> allBooks = bookService.findAllBooks(page, keyword);
        // Then
        assertEquals(5,allBooks.getSize());
    }

    @Test
    void testUpdateBook() {
        // Given
        Book book = Book.builder().title("BookTitle").author("BookAuthor").yearOfPublication(1948).photo("").build();
        BookDto bookDto = BookDto.builder().title("Title").author("Author").yearOfPublication(1950).photo("").build();
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);
        // When
        Book updatedBook = bookService.updateBook(bookDto);
        // Then
        assertEquals("Title",updatedBook.getTitle());
        assertEquals("Author",updatedBook.getAuthor());
        assertEquals(1950,updatedBook.getYearOfPublication());
    }
}

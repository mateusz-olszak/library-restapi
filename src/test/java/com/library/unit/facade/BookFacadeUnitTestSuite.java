package com.library.unit.facade;

import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.dto.books.BookDto;
import com.library.mappers.BookMapper;
import com.library.service.BookService;
import com.library.service.CopyService;
import com.library.service.facade.BookFacade;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.ui.ConcurrentModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookFacadeUnitTestSuite {

    @InjectMocks
    private BookFacade bookFacade;

    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookService bookService;
    @Mock
    private CopyService copyService;

    @Test
    void testSaveBook() {
        // Given
        int amountOfCopies = 3;
        Book book = Book.builder().author("Author").title("Title").yearOfPublication(1950).build();
        BookDto bookDto = BookDto.builder().author("Author").title("Title").yearOfPublication(1950).copies(amountOfCopies).build();
        Copy copy = new Copy(book, Status.AVAILABLE);
        when(bookMapper.mapToBook(bookDto)).thenReturn(book);
        when(bookService.saveBook(book)).thenReturn(book);
        when(copyService.saveCopy(any())).thenReturn(copy);
        // When
        bookFacade.saveBook(bookDto);
        // Then
        verify(copyService, times(amountOfCopies)).saveCopy(any());
    }

    @Test
    void testUpdateBook() {
        // Given
        int amountOfCopies = 3;
        Book book = Book.builder().author("Author").title("Title").yearOfPublication(1950).build();
        BookDto bookDto = BookDto.builder().author("Author").title("Title").yearOfPublication(1950).copies(amountOfCopies).build();
        when(bookService.updateBook(bookDto)).thenReturn(book);
        doNothing().when(copyService).updateAmountOfCopies(book, amountOfCopies);
        // When
        bookFacade.updateBook(bookDto);
        // Then
        verify(bookService, times(1)).updateBook(bookDto);
        verify(copyService, times(1)).updateAmountOfCopies(book, amountOfCopies);
    }

    @Test
    void testPrintEditPage() {
        // Given
        int bookId = 1;
        Book book = Book.builder().author("Author").title("Title").yearOfPublication(1950).build();
        when(bookService.findBook(bookId)).thenReturn(book);
        when(bookMapper.mapToBookDto(book)).thenReturn(new BookDto());
        // When
        bookFacade.printEditPage(bookId);
        // Then
        verify(bookService, times(1)).findBook(bookId);
        verify(bookMapper, times(1)).mapToBookDto(book);
    }

    @Test
    void testViewBookById() {
        // Given
        int bookId = 1;
        Book book = Book.builder().author("Author").title("Title").yearOfPublication(1950).build();
        BookDto bookDto = BookDto.builder().author("Author").title("Title").yearOfPublication(1950).build();
        when(bookService.findBook(bookId)).thenReturn(book);
        when(bookMapper.mapToBookDto(book)).thenReturn(bookDto);
        when(copyService.retrieveAvailableCopiesForGivenId(bookId)).thenReturn(new ArrayList<>());
        // When
        bookFacade.viewBookById(bookId);
        // Then
        verify(bookService, times(1)).findBook(bookId);
        verify(bookMapper, times(1)).mapToBookDto(book);
        verify(copyService, times(1)).retrieveAvailableCopiesForGivenId(bookId);
    }

    @Test
    void testListBooksByPageAndKeyword() {
        // Given
        String keyword = "Title";
        int page = 1;
        BookDto bookDto1 = BookDto.builder().author("Author1").title("Title1").yearOfPublication(1950).build();
        BookDto bookDto2 = BookDto.builder().author("Author2").title("Title2").yearOfPublication(1950).build();
        BookDto bookDto3 = BookDto.builder().author("Author3").title("Title3").yearOfPublication(1950).build();
        List<BookDto> bookDtoList = Arrays.asList(bookDto1,bookDto2,bookDto3);
        Book book1 = Book.builder().author("Author1").title("Title1").yearOfPublication(1950).build();
        Book book2 = Book.builder().author("Author2").title("Title2").yearOfPublication(1950).build();
        Book book3 = Book.builder().author("Author3").title("Title3").yearOfPublication(1950).build();
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book1,book2,book3));
        when(bookService.findAllBooks(page, keyword)).thenReturn(bookPage);
        when(bookMapper.mapToListBookDto(bookPage.getContent())).thenReturn(bookDtoList);
        // When
        bookFacade.listBooksByPageWithKeyword(keyword, page);
        // Then
        verify(bookService, times(1)).findAllBooks(page, keyword);
        verify(bookMapper, times(1)).mapToListBookDto(anyList());
    }

    @Test
    void testDeleteBook() {
        // Given
        int bookId = 1;
        doNothing().when(bookService).deleteBook(bookId);
        // When
        bookFacade.deleteBook(bookId);
        // Then
        verify(bookService, times(1)).deleteBook(bookId);
    }
}

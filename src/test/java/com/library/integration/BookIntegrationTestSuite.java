package com.library.integration;

import com.library.dao.BookRepository;
import com.library.dao.CopyRepository;
import com.library.dao.ReaderRepository;
import com.library.dao.RentalRepository;
import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.domain.Reader;
import com.library.domain.Rental;
import com.library.exceptions.ElementNotFoundException;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BookIntegrationTestSuite {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CopyRepository copyRepository;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private RentalRepository rentalRepository;

    @Test
    void testCreateBook() {
        // Given
        Book book = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        // When
        Book savedBook = bookRepository.save(book);
        // Then
        assertThat(savedBook.getId()).isGreaterThan(0);
        // Cleanup
        bookRepository.deleteById(savedBook.getId());
    }

    @Test
    void testRetrieveBook() {
        // Given
        Book book = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        Book savedBook = bookRepository.save(book);
        // When
        Book bookDb = bookRepository.findById(savedBook.getId()).get();
        // Then
        assertThat(bookDb.getId()).isGreaterThan(0);
        assertEquals(1948,bookDb.getYearOfPublication());
        // Cleanup
        bookRepository.deleteById(bookDb.getId());
    }

    @Test
    void testRetrieveBooks() {
        // Given
        Book book1 = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        Book book2 = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        Book book3 = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        // When
        List<Book> books = (List<Book>)bookRepository.findAll();
        // Then
        assertEquals(3,books.size());
        // Cleanup
        bookRepository.deleteById(book1.getId());
        bookRepository.deleteById(book2.getId());
        bookRepository.deleteById(book3.getId());
    }

    @Test
    void testDeleteBook_copyShouldBeDeleted() {
        // Given
        Book book = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        Copy copy = new Copy(book, Status.AVAILABLE);
        Book savedBook = bookRepository.save(book);
        // When
        bookRepository.deleteById(savedBook.getId());
        // Then
        boolean bookExists = bookRepository.existsById(savedBook.getId());
        boolean copyExists = copyRepository.existsById(copy.getId());
        assertThat(bookExists).isEqualTo(false);
        assertThat(copyExists).isEqualTo(false);
    }

    @Test
    void testGetBookWithWrongId_throwsElementNotFoundException() {
        // Given
        int wrongId = 25;
        // When Then
        assertThrows(ElementNotFoundException.class, () -> bookRepository.findById(wrongId).orElseThrow(ElementNotFoundException::new));
    }

    @Test
    void testFindBooksByKeyword() {
        // Given
        Book book1 = Book.builder().author("J.K. Rowling").title("Harry Potter").description("BookDesc").yearOfPublication(1948).build();
        Book book2 = Book.builder().author("J.K. Rowling").title("Harry Potter").description("BookDesc").yearOfPublication(1948).build();
        Book book3 = Book.builder().author("J.K. Rowling").title("Harry Potter").description("BookDesc").yearOfPublication(1948).build();
        Book book4 = Book.builder().author("J.K. Rowling").title("Harry Potter").description("BookDesc").yearOfPublication(1948).build();
        Book book5 = Book.builder().author("J.K. Rowling").title("Harry Potter").description("BookDesc").yearOfPublication(1948).build();
        Book book6 = Book.builder().author("J.K. Rowling").title("Harry Potter").description("BookDesc").yearOfPublication(1948).build();
        Book savedBook1 = bookRepository.save(book1);
        Book savedBook2 = bookRepository.save(book2);
        Book savedBook3 = bookRepository.save(book3);
        Book savedBook4 = bookRepository.save(book4);
        Book savedBook5 = bookRepository.save(book5);
        Book savedBook6 = bookRepository.save(book6);
        String keyword = "Potter";
        Pageable pageable = PageRequest.of(0,5);
        // When
        Page<Book> bookPage = bookRepository.retrieveAllBooksMatchingKeyword(keyword,pageable);
        List<Book> books = bookPage.getContent();
        // Then
        assertEquals(5, books.size());
        // Cleanup
        bookRepository.deleteById(savedBook1.getId());
        bookRepository.deleteById(savedBook2.getId());
        bookRepository.deleteById(savedBook3.getId());
        bookRepository.deleteById(savedBook4.getId());
        bookRepository.deleteById(savedBook5.getId());
        bookRepository.deleteById(savedBook6.getId());
    }

    @Test
    void testDeleteBook_copyAndRentalShouldBeDeleted() {
        // Given
        Reader reader = new Reader("John", "Smith", new Date());
        Reader savedReader = readerRepository.save(reader);
        Book book = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Copy savedCopy = copyRepository.save(copy);
        Rental rental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7),Status.IN_USE);
        Rental savedRental = rentalRepository.save(rental);
        // When
        bookRepository.deleteById(savedBook.getId());
        // Then
        boolean bookExists = bookRepository.existsById(savedBook.getId());
        boolean copyExists = copyRepository.existsById(copy.getId());
        boolean readerExists = readerRepository.existsById(savedReader.getId());
        boolean rentalExists = rentalRepository.existsById(savedRental.getId());
        assertThat(bookExists).isEqualTo(false);
        assertThat(copyExists).isEqualTo(false);
        assertThat(rentalExists).isEqualTo(false);
        assertThat(readerExists).isEqualTo(true);
        // Cleanup
        readerRepository.deleteById(savedReader.getId());
    }
}

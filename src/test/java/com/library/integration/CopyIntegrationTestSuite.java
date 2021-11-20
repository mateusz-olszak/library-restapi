package com.library.integration;

import com.library.dao.BookRepository;
import com.library.dao.CopyRepository;
import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CopyIntegrationTestSuite {

    @Autowired
    private CopyRepository copyRepository;
    @Autowired
    private BookRepository bookRepository;

    @Test
    void testCreateNewCopy() {
        // Given
        Book book = new Book("BookTitle","BookDesc",1948);
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.AVAILABLE);
        book.addCopy(copy);
        // When
        Copy savedCopy = copyRepository.save(copy);
        // Then
        assertThat(savedCopy.getId()).isGreaterThan(0);
        assertThat(savedCopy.getBook()).isNotNull();
        assertEquals(1,book.getCopy().size());
        // Cleanup
        bookRepository.deleteById(savedBook.getId());
    }

    @Test
    void testRetrieveCopy() {
        // Given
        Book book = new Book("BookTitle","BookDesc",1948);
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Copy savedCopy = copyRepository.save(copy);
        // When
        Copy copyDb = copyRepository.findById(savedCopy.getId()).get();
        // Then
        assertThat(copyDb).isNotNull();
        assertThat(copyDb.getId()).isGreaterThan(0);
        // Cleanup
        copyRepository.deleteById(copyDb.getId());
        bookRepository.deleteById(savedBook.getId());
    }

    @Test
    void testRetrieveAllCopies() {
        // Given
        Book book = new Book("BookTitle","BookDesc",1948);
        Book savedBook = bookRepository.save(book);
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        Copy copy3 = new Copy(book, Status.AVAILABLE);
        Copy savedCopy1 = copyRepository.save(copy1);
        Copy savedCopy2 = copyRepository.save(copy2);
        Copy savedCopy3 = copyRepository.save(copy3);
        // When
        List<Copy> copies = (List<Copy>) copyRepository.findAll();
        // Then
        assertEquals(3,copies.size());
        // Cleanup
        copyRepository.deleteById(savedCopy1.getId());
        copyRepository.deleteById(savedCopy2.getId());
        copyRepository.deleteById(savedCopy3.getId());
        bookRepository.deleteById(savedBook.getId());
    }

    @Test
    void testRetrieveCopiesMatchingTitle() {
        // Given
        String title = "BookTitle";
        Book book = new Book("BookTitle","BookDesc",1948);
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Copy savedCopy = copyRepository.save(copy);
        // When
        List<Copy> copies = copyRepository.findCopiesByBook_Title(title);
        // Then
        assertEquals(1,copies.size());
        assertEquals(Status.AVAILABLE, copies.get(0).getStatus());
        // Cleanup
        copyRepository.deleteById(savedCopy.getId());
        bookRepository.deleteById(savedBook.getId());
    }

    @Test
    void testRetrieveCopiesMatchingStatusAndBookId() {
        // Given
        Book book = new Book("BookTitle","BookDesc",1948);
        Book savedBook = bookRepository.save(book);
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        Copy savedCopy1 = copyRepository.save(copy1);
        Copy savedCopy2 = copyRepository.save(copy2);
        // When
        List<Copy> copies = copyRepository.findCopiesByStatusAndAndBook_Id(Status.AVAILABLE, savedBook.getId());
        // Then
        assertEquals(2,copies.size());
        // Cleanup
        copyRepository.deleteById(savedCopy1.getId());
        copyRepository.deleteById(savedCopy2.getId());
        bookRepository.deleteById(savedBook.getId());
    }

    @Test
    void testRetrieveCopiesForGivenBookId() {
        // Given
        Book book = new Book("BookTitle","BookDesc",1948);
        Book savedBook = bookRepository.save(book);
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        Copy savedCopy1 = copyRepository.save(copy1);
        Copy savedCopy2 = copyRepository.save(copy2);
        // When
        List<Copy> copies = copyRepository.findCopiesByBook_Id(savedBook.getId());
        // Then
        assertEquals(2,copies.size());
        // Cleanup
        copyRepository.deleteById(savedCopy1.getId());
        copyRepository.deleteById(savedCopy2.getId());
        bookRepository.deleteById(savedBook.getId());
    }

    @Test
    void testRetrieveAvailableCopiesForGivenBook() {
        // Given
        Book book = new Book("BookTitle","BookDesc",1948);
        Book savedBook = bookRepository.save(book);
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        Copy copy3 = new Copy(book, Status.AVAILABLE);
        Copy savedCopy1 = copyRepository.save(copy1);
        Copy savedCopy2 = copyRepository.save(copy2);
        Copy savedCopy3 = copyRepository.save(copy3);
        // When
        List<Copy> copies = copyRepository.findCopiesByStatusAndAndBook_Id(Status.AVAILABLE, savedBook.getId());
        // Then
        assertEquals(3,copies.size());
        // Cleanup
        copyRepository.deleteById(savedCopy1.getId());
        copyRepository.deleteById(savedCopy2.getId());
        copyRepository.deleteById(savedCopy3.getId());
        bookRepository.deleteById(savedBook.getId());
    }

    @Test
    void testDeleteCopy_bookShouldNotBeDeleted() {
        // Given
        Book book = new Book("BookTitle","BookDesc",1948);
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Copy savedCopy = copyRepository.save(copy);
        // When
        copyRepository.deleteById(savedCopy.getId());
        // Then
        boolean copyExists = copyRepository.existsById(savedCopy.getId());
        boolean bookExists = bookRepository.existsById(savedBook.getId());
        assertThat(copyExists).isEqualTo(false);
        assertThat(bookExists).isEqualTo(true);
        // Cleanup
        bookRepository.deleteById(savedBook.getId());
    }
}

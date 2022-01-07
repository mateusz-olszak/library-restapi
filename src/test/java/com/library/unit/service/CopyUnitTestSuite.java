package com.library.unit.service;

import com.library.dao.CopyRepository;
import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.service.CopyService;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CopyUnitTestSuite {

    @InjectMocks
    private CopyService copyService;

    @Mock
    private CopyRepository copyRepository;

    @Test
    void testRetrieveAmountOfCopiesConatiningBook() {
        // Given
        Book book = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        Copy copy3 = new Copy(book, Status.AVAILABLE);
        copy1.setId(1);
        copy2.setId(2);
        copy3.setId(3);
        List<Copy> copies = List.of(copy1,copy2,copy3);
        when(copyRepository.findCopiesByBook_Id(anyInt())).thenReturn(copies);
        // When
        Integer amountOfCopies = copyService.retrieveAmountOfCopiesForGivenBook(1);
        // Then
        assertEquals(3,amountOfCopies);
    }

    @Test
    void testChangeCopyStatus() {
        // Given
        Book book = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        Copy copy = new Copy(book, Status.AVAILABLE);
        // When
        when(copyRepository.findById(anyInt())).thenReturn(Optional.of(copy));
        when(copyRepository.save(any())).thenReturn(copy);
        copyService.changeCopyStatus(1,Status.LOST);
        // Then
        assertEquals(Status.LOST,copy.getStatus());
    }

    @Test
    void testUpdateAmountOfCopies_withAlreadyExistingCopiesIn_addingMoreThanIsInDb() {
        // Given
        int newAmountOfCopies = 3;
        Book book = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        Copy copy = new Copy(book, Status.AVAILABLE);
        doNothing().when(copyRepository).deleteAllByBook_IdAndStatus(anyInt(),any());
        when(copyRepository.save(any())).thenReturn(copy);
        // When
        copyService.updateAmountOfCopies(book, newAmountOfCopies);
        // Then
        verify(copyRepository, times(3)).save(any());
    }

    @Test
    void testUpdateAmountOfCopies_withAlreadyExistingCopiesIn_addingLessThanIsInDb() {
        // Given
        int newAmountOfCopies = 1;
        Book book = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        Copy copy = new Copy(book, Status.AVAILABLE);
        doNothing().when(copyRepository).deleteAllByBook_IdAndStatus(anyInt(),any());
        when(copyRepository.save(any())).thenReturn(copy);
        // When
        copyService.updateAmountOfCopies(book, newAmountOfCopies);
        // Then
        verify(copyRepository, times(1)).save(any());
    }

    @Test
    void testUpdateAmountOfCopies_withoutExistingCopiesInDb() {
        // Given
        int amountOfCopies = 3;
        Book book = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        Copy copy = new Copy(book, Status.AVAILABLE);
        doNothing().when(copyRepository).deleteAllByBook_IdAndStatus(anyInt(),any());
        when(copyRepository.save(any())).thenReturn(copy);
        // When
        copyService.updateAmountOfCopies(book,amountOfCopies);
        // Then
        verify(copyRepository, times(3)).save(any());
    }

    @Test
    void testSaveCopy_withAccurateStatus() {
        // Given
        Book book = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        Copy copy = new Copy(book, Status.AVAILABLE);
        when(copyRepository.save(copy)).thenReturn(copy);
        // When
        Copy savedCopy = copyService.saveCopy(copy);
        // Then
        assertEquals("BookTitle", savedCopy.getBook().getTitle());
        assertEquals(Status.AVAILABLE, copy.getStatus());
    }

    @Test
    void testSaveCopy_withWrongStatus() {
        // Given
        Book book = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        Copy copy = new Copy(book, Status.IN_USE);
        // When Then
        assertThrows(IllegalArgumentException.class, () -> copyService.saveCopy(copy));
    }
}

package com.library;

import com.library.dao.CopyRepository;
import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.exceptions.ElementNotFoundException;
import com.library.service.CopyService;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CopyWithMockTestSuite {

    @Mock
    private CopyRepository copyRepository;

    @InjectMocks
    private CopyService copyService;


    @Test
    void testGetAllCopies(){
        // Given
        Book book = new Book("Harry Potter","J.K Rowling",2003);
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        Copy copy3 = new Copy(book, Status.AVAILABLE);
        book.setCopy(List.of(copy1,copy2,copy3));
        when(copyRepository.findAll()).thenReturn(List.of(copy1,copy2,copy3));

        // When
        List<Copy> copies = copyService.findAllCopies();

        // Then
        assertEquals(3, copies.size());
    }

    @Test
    void testChangeCopyStatus() throws ElementNotFoundException {
        // Given
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        when(copyRepository.findById(copy1.getId())).thenReturn(Optional.of(copy1));
        when(copyRepository.save(copy1)).thenReturn(copy1);

        // When
        Copy actualCopy = copyService.changeCopyStatus(copy1.getId(), Status.RENTED);

        // Then
        assertEquals(Status.RENTED, actualCopy.getStatus());
    }

    @Test
    void testRetrieveCopiesForGivenBook(){
        // Given
        Book book = new Book("Harry Potter","J.K Rowling",2003);
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        Copy copy3 = new Copy(book, Status.AVAILABLE);
        when(copyRepository.retrieveCopiesForGivenBook(book.getId())).thenReturn(List.of(copy1,copy2,copy3));

        // When
        Integer copiesAmount = copyService.retrieveCopiesForGivenBook(book.getId());

        // Then
        assertEquals(3, copiesAmount);
    }

    @Test
    void testRetrieveAvailableCopiesForGivenId(){
        // Given
        Book book = new Book("Harry Potter","J.K Rowling",2003);
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        Copy copy3 = new Copy(book, Status.RENTED);
        when(copyRepository.retrieveAvailableCopiesForGivenId(book.getId())).thenReturn(List.of(copy1,copy2));

        // When
        List<Copy> copiesAmount = copyService.retrieveAvailableCopiesForGivenId(book.getId());

        // Then
        assertEquals(2, copiesAmount.size());
    }

    @Test
    void testRetrieveCopiesWithGivenTitle(){
        // Given
        Book book = new Book("Harry Potter","J.K Rowling",2003);
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        Copy copy3 = new Copy(book, Status.RENTED);
        when(copyRepository.retrieveCopiesWithGivenTitle(book.getTitle())).thenReturn(List.of(copy1,copy2,copy3));

        // When
        List<Copy> copiesAmount = copyService.retrieveCopiesWithGivenTitle(book.getTitle());

        // Then
        assertEquals(3, copiesAmount.size());
    }
}

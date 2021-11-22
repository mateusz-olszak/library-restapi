package com.library.unit;

import com.library.dao.BookRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CopyUnitTestSuite {

    @InjectMocks
    private CopyService copyService;

    @Mock
    private CopyRepository copyRepository;

    @Test
    void testRetrieveAmountOfCopiesConatiningBook() {
        // Given
        Book book = new Book("BookTitle","BookDesc",1948);
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        Copy copy3 = new Copy(book, Status.AVAILABLE);
        copy1.setId(1);
        copy2.setId(2);
        copy3.setId(3);
        List<Copy> copies = List.of(copy1,copy2,copy3);
        // When
        when(copyRepository.findCopiesByBook_Id(anyInt())).thenReturn(copies);
        Integer amountOfCopies = copyService.retrieveAmountOfCopiesForGivenBook(1);
        // Then
        assertEquals(3,amountOfCopies);
    }

    @Test
    void testChangeCopyStatus() {
        // Given
        Book book = new Book("BookTitle","BookDesc",1948);
        Copy copy = new Copy(book, Status.AVAILABLE);
        // When
        when(copyRepository.findById(anyInt())).thenReturn(Optional.of(copy));
        when(copyRepository.save(any())).thenReturn(copy);
        copyService.changeCopyStatus(1,Status.LOST);
        // Then
        assertEquals(Status.LOST,copy.getStatus());
    }
}

package com.library.unit.facade;

import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.dto.books.CopyDto;
import com.library.mappers.CopyMapper;
import com.library.service.CopyService;
import com.library.service.facade.CopyFacade;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CopyFacadeUnitTestSuite {

    @InjectMocks
    private CopyFacade copyFacade;

    @Mock
    private CopyService copyService;
    @Mock
    private CopyMapper copyMapper;

    @Test
    void testGetAllCopies() {
        // Given
        Book book = Book.builder().title("BookTitle").author("BookAuthor").yearOfPublication(1948).build();
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        Copy copy3 = new Copy(book, Status.AVAILABLE);
        List<Copy> copies = Arrays.asList(copy1,copy2,copy3);
        when(copyService.findAllCopies()).thenReturn(copies);
        doCallRealMethod().when(copyMapper).mapToListCopyDto(anyList());
        // When
        List<CopyDto> allCopies = copyFacade.getAllCopies();
        // Then
        assertEquals(3, allCopies.size());
    }

    @Test
    void testGetAllCopiesForGivenBookId() {
        // Given
        int bookId = 1;
        Book book = Book.builder().title("BookTitle").author("BookAuthor").yearOfPublication(1948).build();
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        Copy copy3 = new Copy(book, Status.AVAILABLE);
        List<Copy> copies = Arrays.asList(copy1,copy2,copy3);
        when(copyService.retrieveAvailableCopiesForGivenId(1)).thenReturn(copies);
        doCallRealMethod().when(copyMapper).mapToListCopyDto(copies);
        // When
        List<CopyDto> allCopiesForGivenBookId = copyFacade.getAllCopiesForGivenBookId(bookId);
        // Then
        assertEquals(3, allCopiesForGivenBookId.size());
        verify(copyService, times(1)).retrieveAvailableCopiesForGivenId(bookId);
        verify(copyMapper, times(1)).mapToListCopyDto(copies);
    }

    @Test
    void testGetCopy() {
        // Given
        int copyId = 1;
        Book book = Book.builder().title("BookTitle").author("BookAuthor").yearOfPublication(1948).build();
        Copy copy = new Copy(book, Status.AVAILABLE);
        when(copyService.findCopy(copyId)).thenReturn(copy);
        doCallRealMethod().when(copyMapper).mapToCopyDto(any());
        // When
        CopyDto retrievedCopy = copyFacade.getCopy(copyId);
        // Then
        assertEquals(Status.AVAILABLE, retrievedCopy.getStatus());
        assertEquals(CopyDto.class, retrievedCopy.getClass());
        verify(copyService, times(1)).findCopy(copyId);
        verify(copyMapper, times(1)).mapToCopyDto(any());
    }

    @Test
    void testSaveCopy() {
        // Given
        Book book = Book.builder().title("BookTitle").author("BookAuthor").yearOfPublication(1948).build();
        Copy copy = new Copy(book, Status.AVAILABLE);
        CopyDto copyDto = new CopyDto(1, 1, Status.AVAILABLE);
        when(copyMapper.mapToCopy(copyDto)).thenReturn(copy);
        when(copyService.saveCopy(copy)).thenReturn(copy);
        // When
        copyFacade.saveCopy(copyDto);
        // Then
        verify(copyMapper, times(1)).mapToCopy(copyDto);
        verify(copyService, times(1)).saveCopy(any());
    }

    @Test
    void testChangeCopyStatus() {
        // Given
        int copyId = 1;
        Status status = Status.AVAILABLE;
        Book book = Book.builder().title("BookTitle").author("BookAuthor").yearOfPublication(1948).build();
        Copy copy = new Copy(book, Status.AVAILABLE);
        CopyDto copyDto = new CopyDto(1, 1, Status.AVAILABLE);
        when(copyService.changeCopyStatus(copyId,status)).thenReturn(copy);
        when(copyMapper.mapToCopyDto(any())).thenReturn(copyDto);
        // When
        CopyDto retrievedCopy = copyFacade.changeCopyStatus(copyId, status);
        // Then
        assertEquals(Status.AVAILABLE, retrievedCopy.getStatus());
        assertEquals(CopyDto.class, retrievedCopy.getClass());
        verify(copyService, times(1)).changeCopyStatus(copyId,status);
        verify(copyMapper, times(1)).mapToCopyDto(copy);
    }

    @Test
    void testDeleteCopy() {
        // Given
        int copyId = 1;
        doNothing().when(copyService).deleteCopy(copyId);
        // When
        copyFacade.deleteCopy(copyId);
        // Then
        verify(copyService, times(1)).deleteCopy(copyId);
    }
}

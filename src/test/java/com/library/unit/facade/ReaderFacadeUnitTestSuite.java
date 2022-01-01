package com.library.unit.facade;

import com.library.domain.Reader;
import com.library.mappers.ReaderMapper;
import com.library.service.ReaderService;
import com.library.service.facade.ReaderFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ReaderFacadeUnitTestSuite {

    @InjectMocks
    private ReaderFacade readerFacade;

    @Mock
    private ReaderService readerService;
    @Mock
    private ReaderMapper readerMapper;

    @Test
    void testRegisterReader() {
        // Given
        String email = "test@gmail.com";
        String password = "password";
        Reader reader = new Reader();
        when(readerMapper.mapToReader(any())).thenReturn(reader);
        when(readerService.saveReader(reader)).thenReturn(reader);
        // When
        readerFacade.registerReader(email,password);
        // Then
        verify(readerMapper, times(1)).mapToReader(any());
        verify(readerService, times(1)).saveReader(reader);
    }
}

package com.library.unit.service;

import com.library.dao.ReaderRepository;
import com.library.dao.RoleRepository;
import com.library.domain.Reader;
import com.library.domain.Role;
import com.library.service.ReaderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReaderServiceUnitTestSuite {

    @InjectMocks
    private ReaderService readerService;

    @Mock
    private ReaderRepository readerRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testSaveReader() {
        // Given
        Role role = new Role();
        Reader reader = new Reader();
        when(roleRepository.findByRoleName(any())).thenReturn(role);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(readerRepository.save(any())).thenReturn(reader);
        // When
        readerService.saveReader(reader);
        // Then
        verify(roleRepository, times(1)).findByRoleName(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(readerRepository, times(1)).save(any());
    }

    @Test
    void testEmailUnique() {
        // Given
        String email = "test@gmail.com";
        Reader reader = new Reader();
        when(readerRepository.findByEmail(email)).thenReturn(reader);
        // When
        boolean emailUnique = readerService.isEmailUnique(email);
        // Then
        assertFalse(emailUnique);
        verify(readerRepository, times(1)).findByEmail(email);
    }

    @Test
    void testDeleteReader() {
        // Given
        int readerId = 1;
        doNothing().when(readerRepository).deleteById(readerId);
        // When
        readerService.deleteReader(readerId);
        // Then
        verify(readerRepository, times(1)).deleteById(readerId);
    }
}

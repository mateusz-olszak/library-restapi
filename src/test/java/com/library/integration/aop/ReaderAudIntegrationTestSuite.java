package com.library.integration.aop;

import com.library.dao.ReaderRepository;
import com.library.dao.ReadersAudRepository;
import com.library.dao.RoleRepository;
import com.library.domain.Reader;
import com.library.domain.ReadersAud;
import com.library.domain.Role;
import com.library.service.ReaderService;
import com.library.status.Auditorium;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReaderAudIntegrationTestSuite {

    @Autowired
    private ReaderService readerService;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private ReadersAudRepository readersAudRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testSaveReaderAud() {
        // Given
        Role role = new Role("Reader","View&Rent books");
        Reader reader = new Reader("test@gmail.com","user",new Date());
        Role savedRole = roleRepository.save(role);
        int roleId = savedRole.getId();
        // When
        Reader savedReader = readerService.saveReader(reader);
        // Then
        int readerId = savedReader.getId();
        List<ReadersAud> savedAuds = (List<ReadersAud>) readersAudRepository.findAll();
        ReadersAud saveReaderAud = savedAuds.stream().findFirst().get();
        int savedReaderAudId = saveReaderAud.getId();
        assertEquals(1, savedAuds.size());
        assertEquals(Auditorium.INSERT, saveReaderAud.getEventType());
        assertEquals("test@gmail.com",saveReaderAud.getNewEmail());

        readerRepository.deleteById(readerId);
        roleRepository.deleteById(roleId);
        readersAudRepository.deleteById(savedReaderAudId);
    }

    @Test
    void testDeleteReaderAud() {
        // Given
        Reader reader = new Reader("test@gmail.com","user",new Date());
        Reader savedReader = readerRepository.save(reader);
        int readerId = savedReader.getId();
        // When
        readerService.deleteReader(readerId);
        // Then
        List<ReadersAud> savedAuds = (List<ReadersAud>) readersAudRepository.findAll();
        ReadersAud deleteReaderAud = savedAuds.stream().findFirst().get();
        int deletedReaderAudId = deleteReaderAud.getId();
        assertEquals(1, savedAuds.size());
        assertEquals(Auditorium.DELETE, deleteReaderAud.getEventType());
        assertEquals("test@gmail.com", deleteReaderAud.getOldEmail());

        readersAudRepository.deleteById(deletedReaderAudId);
    }
}

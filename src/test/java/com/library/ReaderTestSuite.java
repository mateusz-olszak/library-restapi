package com.library;

import com.library.domain.Reader;
import com.library.exceptions.ElementNotFoundException;
import com.library.service.ReaderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReaderTestSuite {

    @Autowired
    private ReaderService service;

    @Test
    void testCreateReader(){
        Reader reader = new Reader("Anthony","Joshua",new Date());
        service.saveReader(reader);

        int expectedId = reader.getId();
        int actualId = 0;
        Reader actualReader;
        try {
            actualReader = service.findReaderById(expectedId);
            actualId = actualReader.getId();
        }catch (ElementNotFoundException e){
            e.getMessage();
        }

        assertEquals(expectedId, actualId);
    }

}

package com.library;

import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.exceptions.ElementNotFoundException;
import com.library.service.CopyService;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CopyTestSuite {

    @Autowired
    private CopyService copyService;

    @Test
    void testAddCopy(){
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy = new Copy(book, Status.AVAILABLE.name());
        copyService.saveCopy(copy);

        int actualId = copy.getId();
        int expectedId = 0;
        Copy actualCopy = null;
        try {
            actualCopy = copyService.findCopy(actualId);
            expectedId = actualCopy.getId();
        }catch (ElementNotFoundException e){
            e.getMessage();
        }

        assertEquals(expectedId, actualId);
        copyService.deleteCopy(actualId);
    }

    @Test
    void testFindCopyWithWrongId_throwElementNotFoundException(){
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy = new Copy(book, Status.AVAILABLE.name());
        copyService.saveCopy(copy);


        int id = copy.getId();
        assertThrows(ElementNotFoundException.class, () -> {
            copyService.findCopy(2);
        });
        copyService.deleteCopy(id);
    }

    @Test
    void testChangeCopyStatus(){
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy expectedCopy = new Copy(book, Status.AVAILABLE.name());
        copyService.saveCopy(expectedCopy);

        expectedCopy.setStatus(Status.RENTED.name());
        copyService.saveCopy(expectedCopy);
        int expectedId = expectedCopy.getId();
        int actualId = 0;
        Copy actualCopy = null;
        try {
            actualCopy = copyService.findCopy(expectedId);
            actualId = actualCopy.getId();
        }catch (ElementNotFoundException e){
            e.getMessage();
        }

        assertEquals(Status.RENTED.name(), actualCopy.getStatus());
        assertEquals(expectedId, actualId);

        copyService.deleteCopy(actualId);
    }

    @Test
    void testChangeCopyStatusWithWrongStatus_throwIllegalArgumentException(){
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy expectedCopy = new Copy(book, Status.AVAILABLE.name());
        copyService.saveCopy(expectedCopy);

        int id = expectedCopy.getId();
        expectedCopy.setStatus("Wrong status");

        assertThrows(IllegalArgumentException.class, () -> {
            copyService.saveCopy(expectedCopy);
        });

        copyService.deleteCopy(id);
    }

    @Test
    void testCheckAvailableAmountOfCopiesWithAllStatusAvailable(){
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy1 = new Copy(book, Status.AVAILABLE.name());
        Copy copy2 = new Copy(book, Status.AVAILABLE.name());
        Copy copy3 = new Copy(book, Status.AVAILABLE.name());
        Copy copy4 = new Copy(book, Status.AVAILABLE.name());
        List<Copy> copies = new ArrayList<>();
        copies.add(copy1);
        copies.add(copy2);
        copies.add(copy3);
        copies.add(copy4);
        book.setCopy(copies);
        copyService.saveAllCopies(List.of(copy1,copy2,copy3,copy4));

        List<Copy> availableCopies = copyService.retrieveAvailableCopies(Status.AVAILABLE.name());

        assertEquals(4, availableCopies.size());

        copyService.deleteCopy(copy1.getId());
    }

    @Test
    void testCheckAvailableAmountOfCopiesWithNotAllStatusAvailable(){
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy1 = new Copy(book, Status.AVAILABLE.name());
        Copy copy2 = new Copy(book, Status.AVAILABLE.name());
        Copy copy3 = new Copy(book, Status.AVAILABLE.name());
        Copy copy4 = new Copy(book, Status.RENTED.name());
        List<Copy> copies = new ArrayList<>();
        copies.add(copy1);
        copies.add(copy2);
        copies.add(copy3);
        copies.add(copy4);
        book.setCopy(copies);
        copyService.saveAllCopies(List.of(copy1,copy2,copy3,copy4));

        List<Copy> availableCopies = copyService.retrieveAvailableCopies(Status.AVAILABLE.name());

        assertNotEquals(4, availableCopies.size());
        assertEquals(3,availableCopies.size());

        copyService.deleteCopy(copy1.getId());
    }

    @Test
    void testCheckAmountOfBooksWithGivenTitle_GivenProperTitle(){
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy1 = new Copy(book, Status.AVAILABLE.name());
        Copy copy2 = new Copy(book, Status.AVAILABLE.name());
        Copy copy3 = new Copy(book, Status.AVAILABLE.name());
        Copy copy4 = new Copy(book, Status.RENTED.name());
        List<Copy> copies = new ArrayList<>();
        copies.add(copy1);
        copies.add(copy2);
        copies.add(copy3);
        copies.add(copy4);
        book.setCopy(copies);
        copyService.saveAllCopies(List.of(copy1,copy2,copy3,copy4));

        String title = "The Big Fisherman";
        List<Copy> getTitleCopies = copyService.retrieveCopiesWithGivenTitle(title);

        assertEquals(4,getTitleCopies.size());

        copyService.deleteCopy(copy1.getId());
    }

    @Test
    void testCheckAmountOfBooksWithGivenTitle_GivenWrongTitle(){
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy1 = new Copy(book, Status.AVAILABLE.name());
        Copy copy2 = new Copy(book, Status.AVAILABLE.name());
        Copy copy3 = new Copy(book, Status.AVAILABLE.name());
        Copy copy4 = new Copy(book, Status.RENTED.name());
        List<Copy> copies = new ArrayList<>();
        copies.add(copy1);
        copies.add(copy2);
        copies.add(copy3);
        copies.add(copy4);
        book.setCopy(copies);
        copyService.saveAllCopies(List.of(copy1,copy2,copy3,copy4));

        String title = "Wrong title";
        List<Copy> getTitleCopies = copyService.retrieveCopiesWithGivenTitle(title);

        assertNotEquals(4,getTitleCopies.size());
        assertEquals(0, getTitleCopies.size());

        copyService.deleteCopy(copy1.getId());
    }
}

package com.library;

import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.domain.Reader;
import com.library.domain.Rental;
import com.library.exceptions.ElementNotFoundException;
import com.library.exceptions.NoBooksAvailableException;
import com.library.service.CopyService;
import com.library.service.RentalService;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RentalTestSuite {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private CopyService copyService;

    @Test
    void testCreateRental(){
        Reader reader = new Reader("Anthony","Joshua",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Rental expectedRental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7), Status.IN_USE);
        rentalService.createRental(expectedRental);

        Rental actualRental = null;
        int expectedId = expectedRental.getId();
        int actualId = 0;
        try {
            actualRental = rentalService.findRental(expectedId);
            actualId = actualRental.getId();
        }catch (ElementNotFoundException e){
            e.getMessage();
        }

        assertEquals(expectedId, actualId);

        rentalService.deleteRental(actualId);
    }

    @Test
    void testCreateRentalWithoutAvailableCopy(){
        Reader reader = new Reader("Anthony","Joshua",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy = new Copy(book, Status.RENTED);
        Rental rental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7), Status.IN_USE);

        assertThrows(NoBooksAvailableException.class, () -> {
           rentalService.createRental(rental);
        });
    }

    @Test
    void testCreateOneAvailableBookAndTryToRentForTwoReader(){
        Reader reader1 = new Reader("Anthony","Joshua",new Date());
        Reader reader2 = new Reader("John","Smith",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Rental rental1 = new Rental(copy,reader1, LocalDate.now(),LocalDate.now().plusDays(7), Status.IN_USE);
        Rental rental2 = new Rental(copy,reader2, LocalDate.now(),LocalDate.now().plusDays(7), Status.IN_USE);
        rentalService.createRental(rental1);

        assertThrows(NoBooksAvailableException.class, () -> {
            rentalService.createRental(rental2);
        });
    }

    @Test
    void testCreateTwoAvailableBookAndTryToRentForTwoReaders(){
        Reader reader1 = new Reader("Anthony","Joshua",new Date());
        Reader reader2 = new Reader("John","Smith",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        book.setCopy(List.of(copy1,copy2));
        Rental rental1 = new Rental(copy1,reader1, LocalDate.now(),LocalDate.now().plusDays(7), Status.IN_USE);
        Rental rental2 = new Rental(copy2,reader2, LocalDate.now(),LocalDate.now().plusDays(7), Status.IN_USE);
        rentalService.createMultipleRental(List.of(rental1,rental2));

        Rental actualRental1 = null;
        Rental actualRental2 = null;
        int actualId1 = rental1.getId();
        int actualId2 = rental2.getId();
        try {
            actualRental1 = rentalService.findRental(actualId1);
            actualRental2 = rentalService.findRental(actualId2);
        }catch (ElementNotFoundException e){
            e.getMessage();
        }

        assertEquals(Status.RENTED, actualRental1.getCopy().getStatus());
        assertEquals(Status.RENTED, actualRental2.getCopy().getStatus());
    }

    @Test
    void testCreateTwoAvailableBookAndTryToRentForThreeReaders(){
        Reader reader1 = new Reader("Anthony","Joshua",new Date());
        Reader reader2 = new Reader("John","Smith",new Date());
        Reader reader3 = new Reader("Mark","Winston",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        book.setCopy(List.of(copy1,copy2));
        Rental rental1 = new Rental(copy1,reader1, LocalDate.now(),LocalDate.now().plusDays(7), Status.IN_USE);
        Rental rental2 = new Rental(copy2,reader2, LocalDate.now(),LocalDate.now().plusDays(7), Status.IN_USE);
        Rental rental3 = new Rental(copy2,reader3, LocalDate.now(),LocalDate.now().plusDays(7), Status.IN_USE);


        assertThrows(NoBooksAvailableException.class, () -> {
            rentalService.createMultipleRental(List.of(rental1,rental2,rental3));
        });
    }

    @Test
    void testCreateRentalCheckIfStatusChanged(){
        Reader reader = new Reader("Anthony","Joshua",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Rental expectedRental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7), Status.IN_USE);
        rentalService.createRental(expectedRental);

        Rental actualRental = null;
        int expectedId = expectedRental.getId();
        int actualId = 0;
        try {
            actualRental = rentalService.findRental(expectedId);
            actualId = actualRental.getId();
        }catch (ElementNotFoundException e){
            e.getMessage();
        }

        assertEquals(Status.RENTED, actualRental.getCopy().getStatus());
        assertEquals(Status.IN_USE, actualRental.getCompleted());

        rentalService.deleteRental(actualId);
    }

    @Test
    void testReturnBookCheckIfBookStatusChanged(){
        Reader reader = new Reader("Anthony","Joshua",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Rental rental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7), Status.IN_USE);
        rentalService.createRental(rental);
        int copyId = copy.getId();
        int rentalId = rental.getId();

        rentalService.completeRental(rental);
        Copy expectedCopy = null;
        Rental expectedRental = null;
        try {
            expectedCopy = copyService.findCopy(copyId);
            expectedRental = rentalService.findRental(rentalId);
        }catch (ElementNotFoundException e){
            e.getMessage();
        }

        assertEquals(Status.AVAILABLE, expectedCopy.getStatus());
        assertEquals(Status.COMPLETED, expectedRental.getCompleted());

        rentalService.deleteRental(rentalId);
    }

}

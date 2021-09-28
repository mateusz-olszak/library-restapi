package com.library;

import com.library.dao.BookRepository;
import com.library.dao.RentalRepository;
import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.domain.Reader;
import com.library.domain.Rental;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class KodillaLibraryApplicationTests {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void test_createFirstRental(){
        Reader reader = new Reader("John","Smith",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy1 = new Copy(book,Status.RENTED.name());
        Copy copy2 = new Copy(book,Status.AVAILABLE.name());
        Copy copy3 = new Copy(book,Status.AVAILABLE.name());
        Copy copy4 = new Copy(book,Status.AVAILABLE.name());
        List<Copy> copies = new ArrayList<>();
        copies.add(copy1);
        copies.add(copy2);
        copies.add(copy3);
        copies.add(copy4);
        book.setCopy(copies);
        Rental rental = new Rental(copy1,reader,LocalDate.now(),LocalDate.now().plusDays(7));
        List<Rental> rentalList = new ArrayList<>();
        rentalList.add(rental);
        reader.setRental(rentalList);

        rentalRepository.save(rental);

    }

}

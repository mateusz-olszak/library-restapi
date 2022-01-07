package com.library.service.facade;

import com.library.domain.*;
import com.library.service.BookService;
import com.library.service.CopyService;
import com.library.service.ReaderService;
import com.library.service.RentalService;
import com.library.status.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class RentalFacade {

    private final RentalService rentalService;
    private final ReaderService readerService;
    private final CopyService copyService;
    private final BookService bookService;

    public Map<String, Object> getRentalsForReader(ReaderDetails readerDetails) {
        Map<String, Object> modelMap = new HashMap<>();
        Reader reader = readerService.findReaderByEmail(readerDetails.getUsername());
        List<Rental> allRentals = rentalService.findRentalsForReader(reader);
        allRentals.removeIf(rental -> rental.getCompleted().equals(Status.COMPLETED) && (LocalDate.now().getMonth().getValue() - rental.getRentedTo().getMonth().getValue()) > 0);
        modelMap.put("rentals", allRentals);
        return modelMap;
    }

    public Map<String,Object> getRentalsForAdmin() {
        Map<String,Object> modelMap = new HashMap<>();
        List<Rental> allRentals = rentalService.findAllRentals();
        modelMap.put("rentals",allRentals);
        return modelMap;
    }

    public Map<String,Object> createRental(ReaderDetails readerDetails, int bookId) {
        log.info("Rental is about to be created");
        Map<String, Object> modelMap = new HashMap<>();
        Reader reader = readerService.findReaderByEmail(readerDetails.getUsername());
        Copy copy = copyService.retrieveAvailableCopiesForGivenId(bookId).stream().findFirst().get();
        Book book = bookService.findBook(bookId);
        Rental rental = new Rental(copy,reader);
        rentalService.createRental(rental);
        modelMap.put("reader", reader);
        modelMap.put("book", book);
        modelMap.put("rental", rental);
        return modelMap;
    }

    public void completeRental(int rentalId) {
        Rental rental = rentalService.findRental(rentalId);
        rentalService.completeRental(rental);
    }

    public Map<String, Object> printEditRentalPage(int rentalId) {
        Map<String, Object> modelMap = new HashMap<>();
        Rental rental = rentalService.findRental(rentalId);
        modelMap.put("rental",rental);
        modelMap.put("available",Status.AVAILABLE);
        modelMap.put("rented",Status.RENTED);
        modelMap.put("destroyed",Status.DESTROYED);
        modelMap.put("lost",Status.LOST);
        modelMap.put("completed",Status.COMPLETED);
        modelMap.put("in_use",Status.IN_USE);
        return modelMap;
    }

    public void saveEdittedRental(Rental rental) {
        Rental rentalDb = rentalService.findRental(rental.getId());
        rentalDb.setCompleted(rental.getCompleted());
        rentalDb.getCopy().setStatus(rental.getCopy().getStatus());
        rentalService.saveRental(rentalDb);
    }

    public void deleteRental(int rentalId) {
        Rental rental = rentalService.findRental(rentalId);
        rental.getCopy().setStatus(Status.LOST);
        rentalService.deleteRental(rentalId);
    }
}

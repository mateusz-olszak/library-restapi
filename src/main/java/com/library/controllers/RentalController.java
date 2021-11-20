package com.library.controllers;

import com.library.domain.*;
import com.library.exceptions.ElementNotFoundException;
import com.library.service.*;
import com.library.status.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Controller
@AllArgsConstructor
@Slf4j
public class RentalController {

    private RentalService rentalService;
    private ReaderService readerService;
    private CopyService copyService;
    private BookService bookService;

    @GetMapping("/reader/rentals")
    String getRentals(@AuthenticationPrincipal ReaderDetails readerDetails, Model model) {
        Reader reader = readerService.findReaderByEmail(readerDetails.getUsername());
        List<Rental> allRentals = rentalService.findRentalsForReader(reader);
        allRentals.removeIf(rental -> rental.getCompleted().equals(Status.COMPLETED) && (LocalDate.now().getMonth().getValue() - rental.getRentedTo().getMonth().getValue()) > 0);
        model.addAttribute("rentals", allRentals);
        return "my_rentals";
    }

    @GetMapping("/admin/rentals")
    String getRentalsForAdmin(Model model) throws ElementNotFoundException {
        List<Rental> allRentals = rentalService.findAllRentals();
        model.addAttribute("rentals", allRentals);
        return "admin_rentals";
    }

    @PostMapping("/rentals")
    String createRental(
            @AuthenticationPrincipal ReaderDetails readerDetails,
            @RequestParam("id") int bookId,
            Model model
    ) throws ElementNotFoundException
    {
        Reader reader = readerService.findReaderByEmail(readerDetails.getUsername());
        Copy copy = copyService.retrieveAvailableCopiesForGivenId(bookId).stream().findFirst().get();
        Book book = bookService.findBook(bookId);
        Rental rental = new Rental(copy,reader);
        rentalService.createRental(rental);
        model.addAttribute("reader", reader);
        model.addAttribute("book", book);
        model.addAttribute("rental",rental);
        return "redirect:/reader/rentals";
    }

    @RequestMapping("/rentals/complete/{id}")
    String completeRental(@PathVariable int id) throws ElementNotFoundException {
        Rental rental = rentalService.findRental(id);
        rentalService.completeRental(rental);
        return "redirect:/reader/rentals";
    }

    @GetMapping("/admin/rentals/{id}")
    String editRental(@PathVariable("id") int rentalId, Model model) throws ElementNotFoundException {
        Rental rental = rentalService.findRental(rentalId);
        model.addAttribute("rental",rental);
        model.addAttribute("available",Status.AVAILABLE);
        model.addAttribute("rented",Status.RENTED);
        model.addAttribute("destroyed",Status.DESTROYED);
        model.addAttribute("lost",Status.LOST);
        model.addAttribute("completed",Status.COMPLETED);
        model.addAttribute("in_use",Status.IN_USE);
        return "edit_rental";
    }

    @PostMapping("/admin/rentals/save")
    String saveEdittedRental(@ModelAttribute Rental rental) throws ElementNotFoundException {
        log.info("Preparing to edit rental with id: " + rental.getId());
        Rental rentalDb = rentalService.findRental(rental.getId());
        rentalDb.setCompleted(rental.getCompleted());
        rentalDb.getCopy().setStatus(rental.getCopy().getStatus());
        rentalService.saveRental(rentalDb);
        log.info("Rental has been updated");
        return "redirect:/admin/rentals";
    }

    @RequestMapping("/admin/rentals/delete/{id}")
    String deleteRental(@PathVariable("id") int rentalId) throws ElementNotFoundException {
        Rental rental = rentalService.findRental(rentalId);
        rental.getCopy().setStatus(Status.LOST);
        rentalService.deleteRental(rentalId);
        return "redirect:/admin/rentals";
    }
}

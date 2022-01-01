package com.library.controllers;

import com.library.domain.*;
import com.library.service.ModelFillerService;
import com.library.service.facade.RentalFacade;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Data
@Controller
@RequiredArgsConstructor
public class RentalController {

    private final RentalFacade rentalFacade;
    private final ModelFillerService modelFillerService;

    @GetMapping("/reader/rentals")
    String getRentals(@AuthenticationPrincipal ReaderDetails readerDetails, Model model) {
        Map<String, Object> modelMap = rentalFacade.getRentalsForReader(readerDetails);
        modelFillerService.fullFillModel(model,modelMap);
        return "my_rentals";
    }

    @GetMapping("/admin/rentals")
    String getRentalsForAdmin(Model model) {
        Map<String, Object> modelMap = rentalFacade.getRentalsForAdmin();
        modelFillerService.fullFillModel(model,modelMap);
        return "admin_rentals";
    }

    @PostMapping("/rentals")
    String createRental(
            @AuthenticationPrincipal ReaderDetails readerDetails,
            @RequestParam("id") int bookId,
            Model model
    )
    {
        Map<String, Object> modelMap = rentalFacade.createRental(readerDetails, bookId);
        modelFillerService.fullFillModel(model,modelMap);
        return "redirect:/reader/rentals";
    }

    @RequestMapping("/rentals/complete/{id}")
    String completeRental(@PathVariable int id) {
        rentalFacade.completeRental(id);
        return "redirect:/reader/rentals";
    }

    @GetMapping("/admin/rentals/{id}")
    String printEditRentalPage(@PathVariable("id") int rentalId, Model model) {
        Map<String, Object> modelMap = rentalFacade.printEditRentalPage(rentalId);
        modelFillerService.fullFillModel(model,modelMap);
        return "edit_rental";
    }

    @PostMapping("/admin/rentals/save")
    String saveEdittedRental(@ModelAttribute Rental rental) {
        rentalFacade.saveEdittedRental(rental);
        return "redirect:/admin/rentals";
    }

    @RequestMapping("/admin/rentals/delete/{id}")
    String deleteRental(@PathVariable("id") int rentalId) {
        rentalFacade.deleteRental(rentalId);
        return "redirect:/admin/rentals";
    }
}

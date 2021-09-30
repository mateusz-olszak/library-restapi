package com.library.controllers;

import com.library.domain.Rental;
import com.library.dto.RentalDto;
import com.library.exceptions.ElementNotFoundException;
import com.library.mappers.RentalMapper;
import com.library.service.RentalService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class RentalController {

    private RentalService rentalService;
    private RentalMapper rentalMapper;

    @GetMapping("/rentals")
    List<RentalDto> getRentals() {
       List<Rental> rentals = rentalService.findAllRentals();
       return rentalMapper.mapToListRentalDto(rentals);
    }

    @GetMapping("/rental")
    RentalDto getRental(@RequestParam("id") int id) throws ElementNotFoundException {
        Rental rental = rentalService.findRental(id);
        return rentalMapper.mapToRentalDto(rental);
    }

    @PostMapping("/rentals")
    void createRental(@RequestBody RentalDto rentalDto) throws ElementNotFoundException{
        Rental rental = rentalMapper.mapToRental(rentalDto);
        rentalService.createRental(rental);
    }

    @PutMapping("/rentals/complete/{id}")
    void completeRental(@PathVariable int id) throws ElementNotFoundException {
        Rental rental = rentalService.findRental(id);
        rentalService.completeRental(rental);
    }

}

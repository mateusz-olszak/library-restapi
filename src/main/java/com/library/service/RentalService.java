package com.library.service;

import com.library.dao.RentalRepository;
import com.library.domain.Rental;
import com.library.exceptions.ElementNotFoundException;
import com.library.exceptions.NoBooksAvailableException;
import com.library.status.Status;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RentalService {

    private RentalRepository rentalRepository;

    public Rental saveRental(final Rental rental) {
        return rentalRepository.save(rental);
    }

    public void deleteRental(final int id) {
        rentalRepository.deleteById(id);
    }

    public Rental findRental(final int id) throws ElementNotFoundException {
        return rentalRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("Could not find rental."));
    }

    public List<Rental> findAllRentals(){
        return (List<Rental>) rentalRepository.findAll();
    }

    public void createRental(final Rental rental) {
        if (rental.getCopy().getStatus().equals(Status.AVAILABLE)){
            rental.getCopy().setStatus(Status.RENTED);
            rental.setCompleted(Status.IN_USE);
            rental.setRentedFrom(LocalDate.now());
            rental.setRentedTo(LocalDate.now().plusDays(7));
            rentalRepository.save(rental);
        }else {
            throw new NoBooksAvailableException("There are no available books to rent");
        }
    }

    public void createMultipleRental(final List<Rental> rentals) {
        List<Rental> rentalList = new ArrayList<>();
        for(Rental rental : rentals){
            if (rental.getCopy().getStatus().equals(Status.AVAILABLE)){
                rental.getCopy().setStatus(Status.RENTED);
                rental.setCompleted(Status.IN_USE);
                rentalList.add(rental);
            }else {
                throw new NoBooksAvailableException("There are no available books to rent");
            }
        }

        rentalRepository.saveAll(rentalList);
    }

    public void completeRental(final Rental rental) {
        rental.getCopy().setStatus(Status.AVAILABLE);
        rental.setCompleted(Status.COMPLETED);
        saveRental(rental);
    }
}

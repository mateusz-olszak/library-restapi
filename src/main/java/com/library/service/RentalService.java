package com.library.service;

import com.library.dao.RentalRepository;
import com.library.domain.Reader;
import com.library.domain.Rental;
import com.library.exceptions.ElementNotFoundException;
import com.library.exceptions.NoBooksAvailableException;
import com.library.status.Status;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@EnableAspectJAutoProxy
@Slf4j
public class RentalService {

    private RentalRepository rentalRepository;

    public Rental saveRental(final Rental rental) {
        return rentalRepository.save(rental);
    }

    public void deleteRental(final int id) {
        rentalRepository.deleteById(id);
    }

    public Rental findRental(final int id) throws ElementNotFoundException {
        return rentalRepository.findById(id).orElseThrow(ElementNotFoundException::new);
    }

    public List<Rental> findAllRentals(){
        return (List<Rental>) rentalRepository.findAll();
    }

    public Rental createRental(final Rental rental) {
        if (rental.getCopy().getStatus().equals(Status.AVAILABLE)){
            rental.getCopy().setStatus(Status.RENTED);
            rental.setCompleted(Status.IN_USE);
            rental.setRentedFrom(LocalDate.now());
            rental.setRentedTo(LocalDate.now().plusDays(7));
            log.info("Rental is created");
            return rentalRepository.save(rental);
        }else {
            throw new NoBooksAvailableException("There are no available books to rent");
        }
    }

    public Rental completeRental(final Rental rental) {
        rental.getCopy().setStatus(Status.AVAILABLE);
        rental.setCompleted(Status.COMPLETED);
        return rentalRepository.save(rental);
    }

    public List<Rental> findRentalsForReader(Reader reader) {
        return rentalRepository.findAllByReader(reader);
    }
}

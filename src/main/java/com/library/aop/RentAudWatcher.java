package com.library.aop;

import com.library.dao.RentAudRepository;
import com.library.dao.RentalRepository;
import com.library.domain.*;
import com.library.dto.books.BookDto;
import com.library.exceptions.ElementNotFoundException;
import com.library.status.Auditorium;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RentAudWatcher {

    private final RentAudRepository rentAudRepository;
    private final RentalRepository rentalRepository;

    @After("execution(* com.library.service.RentalService.createRental(..))" +
            "&& args(rental)")
    public void saveRentalLogDb(Rental rental) {
        log.info("INSERT operation is being caught");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ReaderDetails readerDetails = (ReaderDetails) authentication.getPrincipal();
        RentsAud rentsAud = buildInsertRentAud(rental, readerDetails.getUsername());
        rentAudRepository.save(rentsAud);
        log.info("INSERT operation is being recorded");
    }

    @Before("execution(* com.library.service.RentalService.deleteRental(..))" +
            "&& args(rentalId)")
    public void deleteRentalLogDb(int rentalId) {
        log.info("DELETE operation is being caught");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ReaderDetails readerDetails = (ReaderDetails) authentication.getPrincipal();
        Rental rental = rentalRepository.findById(rentalId).orElseThrow(ElementNotFoundException::new);
        RentsAud rentsAud = buildDeleteRentAud(rental, readerDetails.getUsername());
        rentAudRepository.save(rentsAud);
        log.info("DELETE operation is being recorded");
    }

    @Before("execution(* com.library.service.RentalService.completeRental(..))" +
            "&& args(rental)")
    public void updateRentalLogDb(Rental rental) {
        log.info("UPDATE operation is being caught");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ReaderDetails readerDetails = (ReaderDetails) authentication.getPrincipal();
        if (rentalRepository.existsById(rental.getId())) {
            Rental oldRental = rentalRepository.findById(rental.getId()).orElseThrow(ElementNotFoundException::new);
            RentsAud rentsAud = buildUpdateRentAud(oldRental, rental, readerDetails.getUsername());
            rentAudRepository.save(rentsAud);
            log.info("UPDATE operation is being recorded");
        }
    }

    private RentsAud buildInsertRentAud(Rental rental, String owner) {
        return RentsAud.builder()
                .rentalId(rental.getId())
                .newCopyId(rental.getCopy().getId())
                .newReaderId(rental.getReader().getId())
                .date(new Date())
                .eventType(Auditorium.INSERT)
                .audOwner(owner)
                .newRentFrom(rental.getRentedFrom())
                .newReturn(rental.getRentedTo())
                .build();
    }

    private RentsAud buildUpdateRentAud(Rental oldRental, Rental newRental, String owner) {
        return RentsAud.builder()
                .rentalId(newRental.getId())
                .oldCopyId(oldRental.getCopy().getId())
                .newCopyId(newRental.getCopy().getId())
                .oldReaderId(oldRental.getReader().getId())
                .newReaderId(newRental.getReader().getId())
                .oldRentFrom(oldRental.getRentedFrom())
                .newRentFrom(newRental.getRentedFrom())
                .oldReturn(oldRental.getRentedTo())
                .newReturn(newRental.getRentedTo())
                .audOwner(owner)
                .date(new Date())
                .eventType(Auditorium.UPDATE)
                .build();
    }

    private RentsAud buildDeleteRentAud(Rental rental, String owner) {
        return RentsAud.builder()
                .rentalId(rental.getId())
                .oldCopyId(rental.getCopy().getId())
                .oldReaderId(rental.getReader().getId())
                .oldRentFrom(rental.getRentedFrom())
                .oldReturn(rental.getRentedTo())
                .audOwner(owner)
                .date(new Date())
                .eventType(Auditorium.DELETE)
                .build();
    }
}

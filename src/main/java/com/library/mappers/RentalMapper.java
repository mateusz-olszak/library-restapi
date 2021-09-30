package com.library.mappers;

import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.domain.Reader;
import com.library.domain.Rental;
import com.library.dto.RentalDto;
import com.library.exceptions.ElementNotFoundException;
import com.library.service.CopyService;
import com.library.service.ReaderService;
import com.library.status.Status;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RentalMapper {

    private CopyService copyService;
    private ReaderService readerService;

    public Rental mapToRental(final RentalDto rentalDto) throws ElementNotFoundException {
        Copy copy = copyService.findCopy(rentalDto.getCopyId());
        Reader reader = readerService.findReaderById(rentalDto.getReaderId());
        return new Rental(
                copy,
                reader,
                rentalDto.getRentedFrom(),
                rentalDto.getRentedTo(),
                rentalDto.getCompleted()
        );
    }

    public RentalDto mapToRentalDto(final Rental rental){
        return new RentalDto(
                rental.getId(),
                rental.getCopy().getId(),
                rental.getReader().getId(),
                rental.getRentedFrom(),
                rental.getRentedTo(),
                rental.getCompleted()
        );
    }

    public List<RentalDto> mapToListRentalDto(final List<Rental> rentals){
        return rentals.stream()
                .map(this::mapToRentalDto)
                .collect(Collectors.toList());
    }

}

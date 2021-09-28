package com.library.mappers;

import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.domain.Reader;
import com.library.domain.Rental;
import com.library.dto.RentalDto;
import com.library.status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalMapper {

    private ReaderMapper readerMapper;
    private BookMapper bookMapper;

    @Autowired
    public RentalMapper(ReaderMapper readerMapper, BookMapper bookMapper) {
        this.readerMapper = readerMapper;
        this.bookMapper = bookMapper;
    }

    private Copy createCopy(final RentalDto rentalDto){
        return new Copy(
                bookMapper.mapToBook(rentalDto.getBook()),
                Status.RENTED.name()
        );
    }

    public Rental mapToRental(final RentalDto rentalDto){
        Copy copy = createCopy(rentalDto);
        return new Rental(
                copy,
                readerMapper.mapToReader(rentalDto.getReader()),
                rentalDto.getRentedFrom(),
                rentalDto.getRentedTo()
        );
    }

    public RentalDto mapToRentalDto(final Rental rental){
        return new RentalDto(
                rental.getId(),
                bookMapper.mapToBookDto(rental.getCopy().getBook()),
                readerMapper.mapToReaderDto(rental.getReader()),
                rental.getRentedFrom(),
                rental.getRentedTo()
        );
    }

    public List<RentalDto> mapToListRentalDto(final List<Rental> rentals){
        return rentals.stream()
                .map(this::mapToRentalDto)
                .collect(Collectors.toList());
    }

}

package com.library.mappers;

import com.library.client.BookRatingsClient;
import com.library.domain.Book;
import com.library.dto.books.BookDto;
import com.library.dto.googleapi.GoogleItemDto;
import com.library.dto.googleapi.GoogleVolumeInfoDto;
import com.library.service.CopyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookMapper {

    private final CopyService copyService;
    private final BookRatingsClient bookRatingsClient;

    public Book mapToBook(final BookDto bookDto){
        return new Book(
                bookDto.getPhoto(),
                bookDto.getTitle(),
                bookDto.getAuthor(),
                bookDto.getYearOfPublication(),
                bookDto.getDescription(),
                bookDto.getPrice(),
                bookDto.getCurrency()
        );
    }

    public BookDto mapToBookDto(final Book book){
        int copies = copyService.retrieveAmountOfCopiesForGivenBook(book.getId());
        GoogleVolumeInfoDto googleVolumeInfoDto = bookRatingsClient.getRatings().stream()
                .map(GoogleItemDto::getGoogleVolumeInfoDtos)
                .filter(p -> p.getTitle().equalsIgnoreCase(book.getTitle()))
                .findFirst().orElse(new GoogleVolumeInfoDto());
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getDescription(),
                book.getPrice(),
                book.getCurrency(),
                book.getYearOfPublication(),
                googleVolumeInfoDto,
                copies,
                book.getPhoto()
        );
    }

    public List<BookDto> mapToListBookDto(final List<Book> books){
        return books.stream()
                .map(this::mapToBookDto)
                .collect(Collectors.toList());
    }
}

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
        return Book.builder()
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .yearOfPublication(bookDto.getYearOfPublication())
                .description(bookDto.getDescription())
                .price(bookDto.getPrice())
                .currency(bookDto.getCurrency())
                .photo(bookDto.getPhoto().isEmpty() ? null : bookDto.getPhoto()).build();
    }

    public BookDto mapToBookDto(final Book book){
        int copies = copyService.retrieveAmountOfCopiesForGivenBook(book.getId());
        GoogleVolumeInfoDto googleVolumeInfoDto = bookRatingsClient.getRatings().stream()
                .map(GoogleItemDto::getGoogleVolumeInfoDtos)
                .filter(p -> p.getTitle().equalsIgnoreCase(book.getTitle()))
                .findFirst().orElse(new GoogleVolumeInfoDto());
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .description(book.getDescription())
                .price(book.getPrice())
                .currency(book.getCurrency())
                .yearOfPublication(book.getYearOfPublication())
                .googleVolumeInfoDto(googleVolumeInfoDto)
                .copies(copies)
                .photo(book.getPhoto()).build();
    }

    public List<BookDto> mapToListBookDto(final List<Book> books){
        return books.stream()
                .map(this::mapToBookDto)
                .collect(Collectors.toList());
    }
}

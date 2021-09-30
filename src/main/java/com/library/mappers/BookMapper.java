package com.library.mappers;

import com.library.domain.Book;
import com.library.dto.BookDto;
import com.library.service.CopyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookMapper {

    private CopyService copyService;

    public Book mapToBook(final BookDto bookDto){
        return new Book(
                bookDto.getTitle(),
                bookDto.getAuthor(),
                bookDto.getYearOfPublication()
        );
    }

    public BookDto mapToBookDto(final Book book){
        int copies = copyService.retrieveCopiesForGivenBook(book.getId());
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getYearOfPublication(),
                copies
        );
    }

    public List<BookDto> mapToListReaderDto(final List<Book> books){
        return books.stream()
                .map(this::mapToBookDto)
                .collect(Collectors.toList());
    }

}

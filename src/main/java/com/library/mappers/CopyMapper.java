package com.library.mappers;

import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.dto.books.CopyDto;
import com.library.exceptions.ElementNotFoundException;
import com.library.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CopyMapper {

    private BookService bookService;

    public Copy mapToCopy(final CopyDto copyDto) throws ElementNotFoundException {
        Book book = bookService.findBook(copyDto.getBookId());
        return new Copy(
                book,
                copyDto.getStatus()
        );
    }

    public CopyDto mapToCopyDto(final Copy copy){
        return new CopyDto(
                copy.getId(),
                copy.getBook().getId(),
                copy.getStatus()
        );
    }

    public List<CopyDto> mapToListCopyDto(final List<Copy> copies){
        return copies.stream()
                .map(this::mapToCopyDto)
                .collect(Collectors.toList());
    }
}

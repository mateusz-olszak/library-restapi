package com.library.mappers;

import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.dto.CopyDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CopyMapper {

    public Copy mapToCopy(final CopyDto copyDto){
        return new Copy(
                new Book(copyDto.getBookTitle(), copyDto.getBookAuthor(), copyDto.getBookYearOfPublication()),
                copyDto.getStatus()
        );
    }

    public CopyDto maptoCopyDto(final Copy copy){
        return new CopyDto(
                copy.getId(),
                copy.getBook().getTitle(),
                copy.getBook().getAuthor(),
                copy.getBook().getYearOfPublication(),
                copy.getStatus()
        );
    }

    public List<CopyDto> mapToListCopyDto(final List<Copy> copies){
        return copies.stream()
                .map(this::maptoCopyDto)
                .collect(Collectors.toList());
    }

}

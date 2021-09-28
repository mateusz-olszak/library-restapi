package com.library.mappers;

import com.library.domain.Reader;
import com.library.dto.ReaderDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReaderMapper {

    public Reader mapToReader(final ReaderDto readerDto){
        return new Reader(
                readerDto.getFirstName(),
                readerDto.getLastName(),
                readerDto.getCreated()
        );
    }

    public ReaderDto mapToReaderDto(final Reader reader){
        return new ReaderDto(
                reader.getId(),
                reader.getFirstName(),
                reader.getLastName(),
                reader.getCreated()
        );
    }

    public List<ReaderDto> mapToListReaderDto(final List<Reader> readers){
        return readers.stream()
                .map(this::mapToReaderDto)
                .collect(Collectors.toList());
    }
}

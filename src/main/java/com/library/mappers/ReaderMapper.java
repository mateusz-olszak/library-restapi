package com.library.mappers;

import com.library.domain.Reader;
import com.library.dto.ReaderDto;
import org.springframework.stereotype.Service;

@Service
public class ReaderMapper {

    public Reader mapToReader(final ReaderDto readerDto){
        return new Reader(
                readerDto.getEmail(),
                readerDto.getPassword(),
                readerDto.getCreated()
        );
    }
}

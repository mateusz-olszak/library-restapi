package com.library.controllers;

import com.library.domain.Reader;
import com.library.dto.ReaderDto;
import com.library.exceptions.ElementNotFoundException;
import com.library.mappers.ReaderMapper;
import com.library.service.ReaderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ReaderController {

    private ReaderService readerService;
    private ReaderMapper readerMapper;

    @GetMapping("/readers")
    List<ReaderDto> getAllReaders(){
        List<Reader> readerDtoList = readerService.findAllReaders();
        return readerMapper.mapToListReaderDto(readerDtoList);
    }

    @GetMapping("/reader")
    ReaderDto getReader(@RequestParam("id") int id) throws ElementNotFoundException {
        Reader reader = readerService.findReaderById(id);
        return readerMapper.mapToReaderDto(reader);
    }

    @PostMapping("/readers")
    void createReader(@RequestBody ReaderDto readerDto){
        Reader reader = readerMapper.mapToReader(readerDto);
        readerService.saveReader(reader);
    }

    @DeleteMapping("/readers/delete/{id}")
    void deleteReader(@PathVariable int id){
        readerService.deleteReader(id);
    }

}

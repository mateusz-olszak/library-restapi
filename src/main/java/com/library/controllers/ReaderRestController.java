package com.library.controllers;

import com.library.service.facade.ReaderFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReaderRestController {

    private final ReaderFacade readerFacade;

    @PostMapping("/readers/register/check_email")
    public String checkDuplicateEmail(@Param("email") String email) {
        return readerFacade.checkDuplicateEmail(email);
    }

    @DeleteMapping("/readers/delete/{id}")
    void deleteReader(@PathVariable int id){
        readerFacade.deleteReader(id);
    }
}

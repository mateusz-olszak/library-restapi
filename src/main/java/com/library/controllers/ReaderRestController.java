package com.library.controllers;

import com.library.service.ReaderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class ReaderRestController {

    private ReaderService readerService;

    @PostMapping("/readers/register/check_email")
    public String checkDuplicateEmail(@Param("email") String email) {
        log.info("Check if email already exist method activated");
        return readerService.isEmailUnique(email) ? "OK" : "Duplicated";
    }

}

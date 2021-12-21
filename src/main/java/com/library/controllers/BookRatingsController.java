package com.library.controllers;

import com.library.client.BookRatingsClient;
import com.library.dto.googleapi.GoogleItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookRatingsController {

    private final BookRatingsClient bookRatingsClient;

    @GetMapping("/v1/book/ratings")
    public List<GoogleItemDto> getBooks() {
        return bookRatingsClient.getRatings();
    }

}

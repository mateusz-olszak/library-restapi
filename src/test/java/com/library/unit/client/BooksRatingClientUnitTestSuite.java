package com.library.unit.client;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.library.client.BookRatingsClient;
import com.library.dto.googleapi.GoogleBooksRatingDto;
import com.library.dto.googleapi.GoogleImageLinksDto;
import com.library.dto.googleapi.GoogleItemDto;
import com.library.dto.googleapi.GoogleVolumeInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BooksRatingClientUnitTestSuite {

    @InjectMocks
    @Spy
    private BookRatingsClient bookRatingsClient;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void testGetRatings_returnsRatings() throws GeneralSecurityException, IOException {
        // Given
        URI url = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/books/v1/mylibrary/bookshelves/2/volumes")
                .build().encode().toUri();

        GoogleImageLinksDto googleImageLinksDto = new GoogleImageLinksDto();
        googleImageLinksDto.setSmallThumbnail("smallThumbnail");

        GoogleVolumeInfoDto volumeInfoDto = new GoogleVolumeInfoDto();
        volumeInfoDto.setTitle("Testing title");
        volumeInfoDto.setGoogleImageLinksDto(googleImageLinksDto);

        GoogleItemDto item = new GoogleItemDto();
        item.setGoogleVolumeInfoDtos(volumeInfoDto);

        GoogleItemDto[] itemDto = new GoogleItemDto[1];
        itemDto[0] = item;

        GoogleBooksRatingDto ratingDto = new GoogleBooksRatingDto();
        ratingDto.setItems(itemDto);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        headers.setBearerAuth(null);
        ResponseEntity<GoogleBooksRatingDto> response = new ResponseEntity<>(ratingDto, headers, HttpStatus.OK);

        doReturn(new GoogleCredential()).when(bookRatingsClient).getCredentials();

        when(restTemplate.exchange(url, HttpMethod.GET, entity, GoogleBooksRatingDto.class)).thenReturn(response);
        // When
        List<GoogleItemDto> ratings = bookRatingsClient.getRatings();
        // Then
        assertTrue(Objects.nonNull(ratings));
        assertEquals(1, ratings.size());
        assertEquals("Testing title", ratings.get(0).getGoogleVolumeInfoDtos().getTitle());
        assertEquals("smallThumbnail", ratings.get(0).getGoogleVolumeInfoDtos().getGoogleImageLinksDto().getSmallThumbnail());
    }

    @Test
    void testGetRatings_returnsEmptyList() throws GeneralSecurityException, IOException {
        // Given
        URI url = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/books/v1/mylibrary/bookshelves/2/volumes")
                .build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(null);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        doReturn(new GoogleCredential()).when(bookRatingsClient).getCredentials();

        when(restTemplate.exchange(url, HttpMethod.GET, entity, GoogleBooksRatingDto.class)).thenThrow(RestClientException.class);
        // When
        List<GoogleItemDto> ratings = bookRatingsClient.getRatings();
        // Then
        assertTrue(Objects.nonNull(ratings));
        assertEquals(0, ratings.size());
    }
}

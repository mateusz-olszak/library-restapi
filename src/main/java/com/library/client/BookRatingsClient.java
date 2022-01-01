package com.library.client;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.library.config.GoogleClientConfig;
import com.library.dto.googleapi.GoogleBooksRatingDto;
import com.library.dto.googleapi.GoogleItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookRatingsClient {

    private final RestTemplate restTemplate;
    private final GoogleClientConfig clientConfig;

    public List<GoogleItemDto> getRatings() {
        URI url = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/books/v1/mylibrary/bookshelves/2/volumes")
                .build().encode().toUri();
        try {
            Credential credentials = getCredentials();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(credentials.getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>("body", headers);

            ResponseEntity<GoogleBooksRatingDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, GoogleBooksRatingDto.class);

            return Optional.ofNullable(Objects.requireNonNull(response.getBody()).getItems())
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(p -> Objects.nonNull(p.getGoogleVolumeInfoDtos()))
                    .collect(Collectors.toList());
        } catch (RestClientException | GeneralSecurityException | IOException e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public Credential getCredentials() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(clientConfig.getClientId(), clientConfig.getClientSecret())
                .build();
        credential.setAccessToken(getNewToken());
        credential.setRefreshToken(clientConfig.getRefreshToken());
        return credential;
    }

    public String getNewToken() throws IOException {
        ArrayList<String> scopes = new ArrayList<>();
        scopes.add(clientConfig.getScope());
        TokenResponse tokenResponse = new GoogleRefreshTokenRequest(new NetHttpTransport(), new JacksonFactory(),
                clientConfig.getRefreshToken(), clientConfig.getClientId(), clientConfig.getClientSecret()).setScopes(scopes).setGrantType("refresh_token").execute();
        return tokenResponse.getAccessToken();
    }
}

package com.library.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GoogleClientConfig {

    @Value("${google.api.refresh-token}")
    private String refreshToken;

    @Value("${google.api.client-id}")
    private String clientId;

    @Value("${google.api.client-secret}")
    private String clientSecret;

    @Value("${google.api.scope}")
    private String scope;
}

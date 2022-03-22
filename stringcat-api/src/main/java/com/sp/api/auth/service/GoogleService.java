package com.sp.api.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleService {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private static final String CLIENT_ID = "604487308809-7dj190vf7ab9po4k7qp79pmokshaopbf";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://35.232.10.143/auth/callback";
    private static final String GRANT_TYPE = "authorization_code";

    public ResponseEntity<String> createPostReq(String code) {
        String url = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("grant_type", GRANT_TYPE);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    }

}

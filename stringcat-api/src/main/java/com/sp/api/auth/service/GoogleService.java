package com.sp.api.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.api.auth.dto.GoogleUserDto;
import com.sp.api.auth.dto.OauthTokenDto;
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

    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";
    private static final String REDIRECT_URI = "";
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

    public OauthTokenDto getAccessToken(ResponseEntity<String> response) {
        OauthTokenDto oauthTokenDto = null;

        try {
            oauthTokenDto = objectMapper.readValue(response.getBody(), OauthTokenDto.class);
        } catch (JsonProcessingException e) {
            log.error("JSON 변환과정 중 에러 :: {}", e.getMessage());
        }

        return oauthTokenDto;
    }

    public ResponseEntity<String> createGetReq(OauthTokenDto oauthTokenDto) {
        String url = "https://www.googleapis.com/oauth2/v1/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oauthTokenDto.getAccessToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);

        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }

    public GoogleUserDto getUserInfo(ResponseEntity<String> googleUserInfoRes) {
        GoogleUserDto googleUserDto = null;

        try {
            googleUserDto = objectMapper.readValue(googleUserInfoRes.getBody(), GoogleUserDto.class);
        } catch (JsonProcessingException e) {
            log.error("사용자 등록중 에러 !! :: {}", e.getMessage());
        }

        return googleUserDto;
    }
}

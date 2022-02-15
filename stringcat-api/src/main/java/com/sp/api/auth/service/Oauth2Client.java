package com.sp.api.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.api.auth.dto.GithubResDto;
import com.sp.api.auth.dto.GoogleResDto;
import com.sp.api.auth.dto.KakaoResDto;
import com.sp.common.type.HttpStatusCode;
import com.sp.exception.type.ErrorCode;
import com.sp.exception.type.StringcatCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2Client {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    String kakaoUrl = "https://kapi.kakao.com/v2/user/me";
    String googleUrl = "https://oauth2.googleapis.com/tokeninfo";
    String githubUrl = "https://github.com/login/oauth/access_token";

    public KakaoResDto getKakaoUserInfo(String accessToken) {
        try {
            return getUserInfoByKakaoToken(accessToken);
        } catch (HttpClientErrorException e) {
            throw new StringcatCustomException("Unauthorized", ErrorCode.INVALID_AUTH_TOKEN);
        }
    }

    public GoogleResDto getGoogleUserInfo(String accessToken) {
        try {
            return getUserInfoByGoogleToken(accessToken);
        } catch (HttpClientErrorException e) {
            throw new StringcatCustomException("Bad Request", ErrorCode.INVALID_AUTH_TOKEN);
        }
    }

    public GithubResDto getGithubUserInfo(String accessToken) {
        try {
            return getUserInfoByGithubToken(accessToken);
        } catch (HttpClientErrorException e) {
            throw new StringcatCustomException("NOT FOUND", ErrorCode.NOT_FOUND);
        }
    }

    public KakaoResDto getUserInfoByKakaoToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        log.info("------------------요청 이전------------------");

        HttpEntity<MultiValueMap<String, String>> kakaoRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(kakaoUrl, HttpMethod.POST, kakaoRequest, String.class);
        log.info(valueOf(response.getStatusCode()));
        log.info(response.getBody());

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        if(response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            log.info("Encountered Error while Calling API");
            throw new StringcatCustomException("this access token does not exist", ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        JSONObject body = new JSONObject(response.getBody());
        Long socialId = body.getLong("id");
        String email = body.getJSONObject("kakao_account").getString("email");

        log.info("------------------요청 이후------------------" + socialId + " " + email);

        return new KakaoResDto(socialId, email);
    }

    public GoogleResDto getUserInfoByGoogleToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> googleRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(googleUrl, HttpMethod.POST, googleRequest, String.class);

        GoogleResDto googleUser = null;

        try {
            googleUser = objectMapper.readValue(response.getBody(), GoogleResDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return googleUser;
    }

    public GithubResDto getUserInfoByGithubToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> githubRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(githubUrl, HttpMethod.POST, githubRequest, String.class);

        JSONObject body = new JSONObject(response.getBody());
        String socialId = body.getString("id");
        String email = body.getJSONObject("username").getString("email");

        return new GithubResDto(socialId, email);
    }
}

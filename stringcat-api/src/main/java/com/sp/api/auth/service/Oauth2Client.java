package com.sp.api.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.api.auth.dto.*;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2Client {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String KAKAO_URL = "https://kapi.kakao.com/v2/user/me";
    private final String GOOGLE_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";
    private final String GITHUB_URL = "https://github.com/login/oauth/access_token";
    private final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded;charset=utf-8";

    public UserInfoDto.KakaoUserInfo getKakaoUserInfo(String accessToken) {
        try {
            return getUserInfoByKakaoToken(accessToken);
        } catch (HttpClientErrorException e) {
            throw new StringcatCustomException("Unauthorized", ErrorCode.INVALID_AUTH_TOKEN);
        }
    }

    public UserInfoDto.GoogleUserInfo getGoogleUserInfo(String accessToken) {
        try {
            return getUserInfoByGoogleToken(accessToken);
        } catch (HttpClientErrorException e) {
            throw new StringcatCustomException("Bad Request", ErrorCode.INVALID_AUTH_TOKEN);
        }
    }

    public UserInfoDto.GithubUserInfo getGithubUserInfo(String accessToken) {
        try {
            return getUserInfoByGithubToken(accessToken);
        } catch (HttpClientErrorException e) {
            throw new StringcatCustomException("NOT FOUND", ErrorCode.NOT_FOUND);
        }
    }

    public UserInfoDto.KakaoUserInfo getUserInfoByKakaoToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", CONTENT_TYPE_VALUE);

        log.info("------------------요청------------------");

        HttpEntity<HttpHeaders> kakaoRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_URL, HttpMethod.POST, kakaoRequest, String.class);

        log.info("kakao로부터 가져온 응답 :: {}", response);

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        UserInfoDto.KakaoUserInfo kakaoUser = null;

        try {
            kakaoUser = objectMapper.readValue(response.getBody(), UserInfoDto.KakaoUserInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("kakao 유저 정보 :: {}", kakaoUser);

        return kakaoUser;
    }

    public UserInfoDto.GoogleUserInfo getUserInfoByGoogleToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> googleRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_URL, HttpMethod.POST, googleRequest, String.class);

        UserInfoDto.GoogleUserInfo googleUser = null;

        try {
            googleUser = objectMapper.readValue(response.getBody(), UserInfoDto.GoogleUserInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return googleUser;
    }

    public UserInfoDto.GithubUserInfo getUserInfoByGithubToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", CONTENT_TYPE_VALUE);

        HttpEntity<MultiValueMap<String, String>> githubRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(GITHUB_URL, HttpMethod.POST, githubRequest, String.class);

        JSONObject body = new JSONObject(response.getBody());
        String socialId = body.getString("id");
        String email = body.getJSONObject("username").getString("email");

        return new UserInfoDto.GithubUserInfo(socialId, email);
    }

}

package com.sp.api.common.security.kakao;

import com.sp.api.common.exception.ApiException;
import com.sp.exception.type.ErrorCode;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuth2 {

    public KakaoUserInfo getUserInfo(String token) {
        try {
            return getUserInfoByToken(token);
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new ApiException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
    }

    private KakaoUserInfo getUserInfoByToken(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        JSONObject jsonObject = new JSONObject(response.getBody());
        System.out.println(jsonObject);

        Long id = jsonObject.getLong("id");
        String email = jsonObject.getJSONObject("kakao_account").getString("email");
        String nickname = jsonObject.getJSONObject("properties").getString("nickname");
        String image = jsonObject.getJSONObject("properties").getString("profile_image");

        return new KakaoUserInfo(id, email, nickname, image);

    }
}

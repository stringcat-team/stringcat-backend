package com.sp.api.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.google.gson.Gson;
import com.sp.api.common.exception.ApiException;
import com.sp.api.common.utils.Aes256;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.token.issuer}")
    private String issuer;

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    private final Gson gson = new Gson();

    //토큰에 들어가야하는 정보
    @Data
    @Accessors(chain = true)
    public static class TokenFormatter {
        Long id;
        String email;
        String nickname;
    }

    //토큰 생성하기
    public String generateToken(TokenFormatter token, int expiredMinutes) {
        long issued = Instant.now().toEpochMilli();
        long expired = Instant.now().plus(expiredMinutes, ChronoUnit.MINUTES).toEpochMilli();

        Date iat = new Date(issued);
        Date exp = new Date(expired);
        String claim;

        try {
            claim = gson.toJson(token);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("UserDetail 정보 변환중 에러 발생");
        }

        String encClaim = Aes256.encrypt(claim, jwtSecretKey);

        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(iat)
                .withExpiresAt(exp)
                .withClaim("u", encClaim)
                .sign(Algorithm.HMAC256(jwtSecretKey));
    }

    //jwt 정보 뽑아오기
    public String extractInfoInJwt(String[] decClaim, int index, String key) {
        return Optional.ofNullable(decClaim)
                .map(it -> it[index])
                .orElseThrow(() -> new ApiException("토큰 정보 분석중 에러: "+key));
    }

    //토큰 정보 얻기
    public TokenFormatter getTokenInfo(String encToken) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtSecretKey)).build();

        Claim jwt = jwtVerifier.verify(encToken).getClaim("u");
        String decClaim = Aes256.decrypt(jwt.asString(), jwtSecretKey);

        return gson.fromJson(decClaim, TokenFormatter.class);
    }

    //토큰 유효성 검사
    public boolean validateToken(String authToken) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtSecretKey)).build();

            jwtVerifier.verify(authToken);

            return true;
        } catch (SignatureVerificationException exception) {
            log.error("JWT 검사가 실패하였습니다.");
            log.error("실패된 토큰: " + authToken);

            throw new ApiException("토큰 검사 실패");
        }
    }

    //요청으로부터 jwt 가져오기
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}

package com.sp.api.common.security;

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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${biz.jwt.issuer}")
    private String issuer;

    @Value("${biz.secretkey}")
    private String jwtSecret;

    private Gson gson = new Gson();

    @Data
    @Accessors(chain = true)
    public static class TokenFormat {
        long id;
        String email;
    }

    public String generateToken(TokenFormat token, int expiredMinutes) {
        long begin = Instant.now().toEpochMilli();
        long end = Instant.now().plus(expiredMinutes, ChronoUnit.MINUTES).toEpochMilli();

        Date iat = new Date(begin);
        Date exp = new Date(end);

        String claim;
        try {
            claim = gson.toJson(token);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("UserDetail 정보 변환중 에러!!");
        }

        String encClaim = Aes256.encrypt(claim, jwtSecret);

        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(iat)
                .withExpiresAt(exp)
                .withClaim("u", encClaim)
                .sign(Algorithm.HMAC512(jwtSecret));
    }

    public String extractInfoInJwt(String[] decClaim, int index, String key) {
        return Optional.ofNullable(decClaim)
                .map(it -> it[index])
                .orElseThrow(() -> new ApiException("토큰정보 분석중 에러 (" + key + ")"));
    }


    public TokenFormat getTokenInfo(String encToken) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(jwtSecret))
                .build();

        Claim jwt = verifier.verify(encToken).getClaim("u");
        String decClaim = Aes256.decrypt(jwt.asString(), jwtSecret);

        TokenFormat token = gson.fromJson(decClaim, TokenFormat.class);

        return token;
    }

    public boolean validateToken(String authToken) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(jwtSecret))
                    .build();
            verifier.verify(authToken);

            return true;
        } catch(SignatureVerificationException e) {
            log.error("JWT token verification failed!!");
            log.error("input token = [" + authToken + "]");
            throw new ApiException("토근 검증 실패!!");
        }
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}

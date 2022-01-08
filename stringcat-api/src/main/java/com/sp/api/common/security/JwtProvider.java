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
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtProvider {

    @Value("${biz.jwt.issuer}")
    private String issuer;

    @Value("${biz.secretkey}")
    private String jwtSecret;

    private Gson gson = new Gson();

    @Data
    @Accessors(chain = true)
    public static class TokenFormat {
        Long id;
        String email;
    }

    public String generateToken(TokenFormat tokenFormat, int expiredMinutes) {
        long start = Instant.now().toEpochMilli();
        long end = Instant.now().plus(expiredMinutes, ChronoUnit.MINUTES).toEpochMilli();

        Date iat = new Date(start);
        Date exp = new Date(end);

        String claim;

        try {
            claim = gson.toJson(tokenFormat);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("Error in Converting UserDetail");
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
                .orElseThrow(() -> new ApiException("Error in Analyzing of Token information" + key));
    }

    public TokenFormat getTokenInfo(String encToken) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(jwtSecret)).build();

        Claim jwt = jwtVerifier.verify(encToken).getClaim("u");
        String decClaim = Aes256.decrypt(jwt.asString(), jwtSecret);

        TokenFormat tokenFormat = gson.fromJson(decClaim, TokenFormat.class);

        return tokenFormat;
    }

    public boolean validateToken(String authToken) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(jwtSecret)).build();
            jwtVerifier.verify(authToken);

            return true;
        } catch (SignatureVerificationException e) {
            log.error("JWT token verification failed!!");
            log.error("input token = [" + authToken + "]");
            throw new ApiException("Fail to verify token");
        }
    }

    public String getJwtFromRequest(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer : ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}

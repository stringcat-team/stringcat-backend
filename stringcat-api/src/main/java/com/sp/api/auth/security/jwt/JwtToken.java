package com.sp.api.auth.security.jwt;

import com.sp.domain.code.UserRole;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Slf4j
public class JwtToken {

    @Getter
    private final String token;
    private final Key key;

    public static final String AUTH_KEY = "role";

    public JwtToken(String token, Key key) {
        this.token = token;
        this.key = key;
    }

    public JwtToken(String socialId, UserRole userRole, Date expired, Key key) {
        this.key = key;
        this.token = generateToken(socialId, userRole.toString(), expired);
    }

    private String generateToken(String socialId, String role, Date expired) {
        return Jwts.builder()
                .setSubject(socialId)
                .claim(AUTH_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expired)
                .compact();
    }

    public boolean validate() {
        return getTokenClaims() != null;
    }

    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        }

        return null;
    }
}

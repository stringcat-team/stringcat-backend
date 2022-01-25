package com.sp.api.auth.security.jwt;

import com.sp.domain.code.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final String JWT_SECRET_KEY = "stringcat-authorization-jwt-token-secret-key";
    private final int JWT_EXPIRATIONS_MS = 604800000;
    private final Key key;

    public JwtTokenProvider() {
        this.key = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes());
    }

    public JwtToken generateToken(String socialId, UserRole userRole) {
        Date expiredDate = new Date(System.currentTimeMillis() + JWT_EXPIRATIONS_MS);
        return new JwtToken(socialId, userRole, expiredDate, key);
    }

    public JwtToken generateNewToken(String socialId) {
        return generateToken(socialId, UserRole.USER);
    }

    public JwtToken convertStringToJwtToken(String token) {
        return new JwtToken(token, key);
    }

    public Authentication getAuthentication(JwtToken jwtToken) {
        if(jwtToken.validate()) {
            Claims claims = jwtToken.getTokenClaims();
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{claims.get(JwtToken.AUTH_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            User user = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(user, jwtToken, authorities);
        } else {
            throw new RuntimeException();
        }
    }
}

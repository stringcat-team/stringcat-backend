package com.sp.api.common.utils;

import com.sp.api.common.exception.ApiException;
import com.sp.exception.type.ErrorCode;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil implements Serializable {

    @Value("${jwt.secret}")
    private String secret;
    public static final long JWT_VALIDITY = 24*60*60; //24hours
    public static final long JWT_REFRESH_EXPIRE = 7*24*60*60; //7days
    private static final long serialVersionUID = 9876543210L;

    //accessToken
    public String generateToken(UserDetails userDetail) {
        Map<String, Object> claims = new HashMap<>();

        return doGenerateToken(claims, userDetail.getUsername());
    }

    //refreshToken
    public String generateRefreshToken() {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_EXPIRE * 1000))
                .signWith(SignatureAlgorithm.RS256, secret)
                .compact();
    }

    //jwt로부터 username 가져오기
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //토큰 만료일자 가져오기
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //토큰 만료되었는지 확인하기
    public Boolean isExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }

    //validate Token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);

        return (username.equals(userDetails.getUsername()) && !isExpired(token));
    }

    //Success
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    //필요한 정보 가져오기 위한 메서드
    private Claims getAllClaimsFromToken(String claimsJws) {
        JwtParser jwtParser = Jwts.parser().setSigningKey(secret);

        try {
            return jwtParser.parseClaimsJwt(claimsJws).getBody();
        } catch (ExpiredJwtException e) {
            throw new ApiException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        } catch (MalformedJwtException | UnsupportedJwtException | SignatureException e) {
            throw new ApiException(ErrorCode.MALFORMED_TOKEN_EXCEPTION);
        }
    }

    //토큰 생성 메서드
    public String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.RS256, secret)
                .compact();
    }
}

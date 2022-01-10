package com.sp.api.common.security;

import com.sp.api.common.exception.ApiException;
import com.sp.api.common.utils.JwtUtil;
import com.sp.api.user.UserDetailImpl;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil tokenProvider;

    @Autowired
    private UserDetailsService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String username = null;
        String authToken = null;

        if(StringUtils.equals(request.getMethod(), "OPTIONS")) {
            log.error("REQ OPTIONS METHOD");
        }

        if(header != null && header.startsWith("Bearer ")) {
            authToken = header.replace("Bearer ", "");

            try {
                username = tokenProvider.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                log.error("username을 얻어오면서 에러 발생 : ", e);
            } catch (ExpiredJwtException e) {
                log.error("토큰이 더이상 유효하지 않습니다 : ", e);
            }

        } else {
            log.error("bearer 문자열을 찾지 못했습니다. header는 무시");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailService.loadUserByUsername(username);

            if(tokenProvider.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                log.info("인증된 사용자 " + username + ", security context");

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
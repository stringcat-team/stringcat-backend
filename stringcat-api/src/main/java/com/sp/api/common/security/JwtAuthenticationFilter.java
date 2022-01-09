package com.sp.api.common.security;

import com.sp.api.common.exception.ApiException;
import com.sp.api.user.UserDetailImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = tokenProvider.getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                JwtTokenProvider.TokenFormat token = tokenProvider.getTokenInfo(jwt);

                UserDetailImpl userDetails = (UserDetailImpl)userDetailService.loadUserByUsername(token.getEmail());

                if (Objects.isNull(userDetails) ||
                        !userDetails.getEmail().equals(token.getEmail())) {
                    log.error("고객정보와 토큰정보가 일치 하지 않습니다.");
                    log.error("db [{}], token [{}]", userDetails.toString(), token.getEmail());
                    throw new ApiException("고객정보와 요청된 토큰의 고객정보가 일치 하지 않습니다.");
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute("ID", token.getId());
                request.setAttribute("USER_ID", token.getEmail());
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }
}
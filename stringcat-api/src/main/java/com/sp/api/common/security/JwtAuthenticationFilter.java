package com.sp.api.common.security;

import com.sp.api.common.exception.ApiException;
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
    private JwtProvider jwtProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = jwtProvider.getJwtFromRequest(request);

            if(StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
                JwtProvider.TokenFormat tokenFormat = jwtProvider.getTokenInfo(jwt);

                UserDetailImpl userDetail = (UserDetailImpl) userDetailsService.loadUserByUsername(tokenFormat.getEmail());

                if(Objects.isNull(userDetail) || !userDetail.getEmail().equals(tokenFormat.getEmail())) {
                    log.error("the info of user doesn't match with the info of token");
                    log.error("db [{}], token [{}]", userDetail.toString(), tokenFormat.getEmail());

                    throw new ApiException("the info of user doesn't match with requested token info.");
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                request.setAttribute("ID", tokenFormat.getId());
                request.setAttribute("USER_EMAIL", tokenFormat.getEmail());
            }
        } catch (Exception e) {
            logger.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }
}

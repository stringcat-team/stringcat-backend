package com.sp.api.auth;

import com.sp.api.common.exception.ApiException;
import com.sp.api.user.CustomUserDetail;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = tokenProvider.getJwtFromRequest(request);

            if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                JwtTokenProvider.TokenFormatter token = tokenProvider.getTokenInfo(jwt);
                CustomUserDetail userDetail = (CustomUserDetail) userDetailsService.loadUserByUsername(token.getEmail());

                if(Objects.isNull(userDetail) || !userDetail.getEmail().equals(token.getEmail())) {

                    log.error("사용자 정보와 토큰 정보 불일치");
                    log.error("db info :: {}, token info :: {}", userDetail.toString(), token.getEmail());

                    throw new ApiException("사용자 정보와 해당 사용자의 토큰의 정보와 일치하지 않습니다.");
                }

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                request.setAttribute("ID", token.getId());
                request.setAttribute("USER_EMAIL", token.getEmail());
                request.setAttribute("USER_NICKNAME", token.getNickname());

            }

        } catch (Exception exception) {
            logger.error("보안 텍스트에서 사용자의 인가 정보를 입력할 수 없습니다: ", exception);
        }

        filterChain.doFilter(request, response);
    }
}

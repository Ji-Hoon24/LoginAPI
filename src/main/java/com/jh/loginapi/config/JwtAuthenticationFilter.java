package com.jh.loginapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.jh.loginapi.config.ApiResultUtil.error;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtConfig jwtConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if(isPublic((HttpServletRequest) request)) {
            chain.doFilter(request, response);
        } else {
            String token = jwtConfig.extractAccessToken((HttpServletRequest) request).orElse(null);
            if(token != null && jwtConfig.isTokenValid(token)) {
                String memberNo = jwtConfig.extractMemberNo(token).get();
                String memberRole = jwtConfig.extractMemberRole(token).get();

                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(memberRole));

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        memberNo, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(request, response);
            } else {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setContentType("application/json");
                httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                new ObjectMapper().writeValue(response.getOutputStream(), error("토큰이 필요합니다.", HttpStatus.UNAUTHORIZED));
            }
        }
    }

    private boolean isPublic(HttpServletRequest request) {
        boolean isPublic = false;
        if (
            request.getServletPath().equals("") ||
            request.getServletPath().equals("/api/member/login") ||
            request.getServletPath().equals("/api/member/join") ||
            request.getServletPath().equals("/api/member/passwdReset") ||
            request.getServletPath().equals("/h2-console") ||
            request.getServletPath().equals("/swagger") ||
            request.getServletPath().indexOf("/swagger") > -1 ||
            request.getServletPath().indexOf("/v2/api-docs") > -1 ||
            request.getServletPath().equals("/api/auth/refresh") ||
            request.getServletPath().equals("/api/auth/sendAuth") ||
            request.getServletPath().equals("/api/auth/validAuth")
        ) {
            isPublic = true;
        }
        return isPublic;
    }
}
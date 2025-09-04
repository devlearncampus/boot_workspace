package com.sinse.jwttest.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.jwttest.util.MyJwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final MyJwtUtil myJwtUtil;

    public JwtAuthenticationFilter(MyJwtUtil myJwtUtil, AuthenticationManager authManager) {
        super("/member/login");
        this.myJwtUtil = myJwtUtil;
        setAuthenticationManager(authManager);
        setAuthenticationSuccessHandler(this::onSuccess);
        setAuthenticationFailureHandler(this::onFailure);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

        Map<String, String> creds = new ObjectMapper().readValue(request.getInputStream(), Map.class);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(creds.get("username"), creds.get("password"));

        return getAuthenticationManager().authenticate(authToken);
    }

    private void onSuccess(HttpServletRequest req, HttpServletResponse res,
                           Authentication auth) throws IOException {

        String username = auth.getName();
        String accessToken = myJwtUtil.generateAccessToken(username);
        String refreshToken = myJwtUtil.generateRefreshToken(username);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        res.setContentType("application/json");
        new ObjectMapper().writeValue(res.getWriter(), tokens);
    }

    private void onFailure(HttpServletRequest req, HttpServletResponse res,
                           AuthenticationException ex) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.getWriter().write("Authentication Failed: " + ex.getMessage());
    }
}

package com.sinse.jwttest.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProtectedController {

    @GetMapping("/protected")
    public Map<String, String> secureEndpoint(@AuthenticationPrincipal String username) {
        return Map.of("message", "Hello, " + username + " 👋 You are authenticated.");
    }
}

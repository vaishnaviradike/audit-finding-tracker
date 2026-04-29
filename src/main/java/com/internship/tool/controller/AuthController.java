package com.internship.tool.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    // TEMP dummy login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");

        if ("admin".equals(username) && "password".equals(password)) {
            return ResponseEntity.ok(Map.of(
                    "token", "dummy-jwt-token",
                    "role", "ADMIN"
            ));
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

    // TEMP register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "role", "VIEWER"
        ));
    }

    // TEMP refresh
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh() {

        return ResponseEntity.ok(Map.of(
                "token", "new-dummy-token"
        ));
    }
}
package com.green_home_project.controller;

import com.green_home_project.dto.LoginRequest;
import com.green_home_project.dto.RegisterRequest;
import com.green_home_project.dto.UserDTO;

import com.green_home_project.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }

    // ================= GET CURRENT USER =================
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }

    // ================= ACTIVATE SELLING =================
    @PutMapping("/activate-selling")
    public ResponseEntity<Map<String, String>> activateSelling() {
        return ResponseEntity.ok(authService.activateSelling());
    }

    // ================= ACTIVATE CARE =================
    @PutMapping("/activate-care/{id}")
    public ResponseEntity<Map<String, String>> activateCare(
            @PathVariable Long id) {

        return ResponseEntity.ok(authService.activateCare(id));
    }

    // ================= TEST AUTH =================
    @GetMapping("/test-auth")
    public String testAuth() {
        return "OK - You are authenticated";
    }
}
package com.green_home_project.controller;

import com.green_home_project.dto.LoginRequest;
import com.green_home_project.dto.RegisterRequest;
import com.green_home_project.model.User;
import com.green_home_project.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // تسجيل مستخدم جديد
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(authService.register(request));
    }

    // تسجيل الدخول
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    // تحديث flags (canSell, canCare) للمستخدم
    @PutMapping("/update-flags/{id}")
    public ResponseEntity<User> updateFlags(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> flags) {

        Boolean canSell = flags.get("canSell");
        Boolean canCare = flags.get("canCare");

        User updatedUser = authService.updateFlags(id, canSell, canCare);

        return ResponseEntity.ok(updatedUser);
    }

    // ✅ Endpoint جديد لتفعيل زر البيع مباشرة من الفرونت أند
    @PutMapping("/activate-selling/{id}")
    public ResponseEntity<Map<String, String>> activateSelling(@PathVariable Long id) {
        User user = authService.updateFlags(id, true, null); // فقط canSell = true
        Map<String, String> response = new HashMap<>();
        response.put("message", "Selling activated for user: " + user.getName());
        return ResponseEntity.ok(response);
    }

    // ✅ Endpoint جديد لتفعيل زر العناية بالنباتات
    @PutMapping("/activate-care/{id}")
    public ResponseEntity<Map<String, String>> activateCare(@PathVariable Long id) {
        User user = authService.updateFlags(id, null, true); // فقط canCare = true
        Map<String, String> response = new HashMap<>();
        response.put("message", "Care activated for user: " + user.getName());
        return ResponseEntity.ok(response);
    }
}
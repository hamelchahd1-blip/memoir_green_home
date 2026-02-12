package com.green_home_project.controller;

import com.green_home_project.dto.LoginRequest;
import com.green_home_project.dto.RegisterRequest;
import com.green_home_project.model.User;
import com.green_home_project.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request){
        return authService.login(request);
    }
    @PutMapping("/update-flags/{id}")
    public User updateFlags(@PathVariable Long id, @RequestBody Map<String, Boolean> flags){
        Boolean canSell = flags.get("canSell");
        Boolean canCare = flags.get("canCare");
        return authService.updateFlags(id, canSell, canCare);
    }
}

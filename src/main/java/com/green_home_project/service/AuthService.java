package com.green_home_project.service;

import com.green_home_project.config.JwtService;
import com.green_home_project.dto.LoginRequest;
import com.green_home_project.dto.RegisterRequest;
import com.green_home_project.model.User;
import com.green_home_project.repository.UserRepository;
import com.green_home_project.model.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // ======================= REGISTER =======================
    public Map<String, String> register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // ✅ ROLE PAR DEFAULT
        user.setRole(Role.USER);

        userRepository.save(user);

        // توليد توكن من الـ USER كامل
        String token = jwtService.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return response;
    }

    // ======================= LOGIN =======================
    public Map<String, String> login(LoginRequest request) {

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {

            if (!user.get().isActive()) {
                throw new RuntimeException("Account not active");
            }

            // ✅ هنا نخدم generateToken على USER كامل مش Email فقط
            String token = jwtService.generateToken(user.get());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return response;
        }

        throw new RuntimeException("Invalid email or password");
    }

    // ======================= UPDATE FLAGS =======================
    public User updateFlags(Long userId, Boolean canSell, Boolean canCare) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (canSell != null) user.setCanSell(canSell);
        if (canCare != null) user.setCanCare(canCare);

        return userRepository.save(user);
    }
}
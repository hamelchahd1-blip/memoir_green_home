package com.green_home_project.service;

import com.green_home_project.dto.LoginRequest;
import com.green_home_project.dto.RegisterRequest;
import com.green_home_project.dto.UserDTO;
import com.green_home_project.model.User;
import com.green_home_project.model.User.Role;
import com.green_home_project.repository.UserRepository;
import com.green_home_project.config.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // ================= REGISTER =================
    public Map<String, String> register(RegisterRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return response;
    }

    // ================= LOGIN =================
    public Map<String, String> login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return response;
    }

    // ================= GET CURRENT USER =================
    public User getCurrentUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ================= DTO =================
    public UserDTO getCurrentUser() {
        User user = getCurrentUserEntity();

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());

        return dto;
    }

    // ================= UPDATE FLAGS =================
    public User updateFlags(Long id, Boolean canSell, Boolean canCare) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (canSell != null) user.setCanSell(canSell);
        if (canCare != null) user.setCanCare(canCare);

        return userRepository.save(user);
    }

    // ================= ACTIVATE SELLING (NEW) =================
    public Map<String, String> activateSelling() {

        User user = getCurrentUserEntity();

        // 🔥 أهم سطر: تحويل role
        user.setRole(Role.SELLER);

        user.setCanSell(true);

        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User is now SELLER");

        return response;
    }

    // ================= ACTIVATE CARE =================
    public Map<String, String> activateCare(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setCanCare(true);
        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Care activated");

        return response;
    }
}
package com.green_home_project.service;

import com.green_home_project.dto.LoginRequest;
import com.green_home_project.dto.RegisterRequest;
import com.green_home_project.model.User;
import com.green_home_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getName(),
                request.getEmail(),
                encodedPassword,
                "USER"
        );

        return userRepository.save(user);
    }

    public User login(LoginRequest request) {

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword())){
            if(!user.get().isActive()){
                throw new RuntimeException("Account not active");
            }
            return user.get();
        }

        throw new RuntimeException("Invalid email or password");}
        public User updateFlags(Long userId, Boolean canSell, Boolean canCare){
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if(canSell != null) user.setCanSell(canSell);
            if(canCare != null) user.setCanCare(canCare);

            return userRepository.save(user);
        }
    }



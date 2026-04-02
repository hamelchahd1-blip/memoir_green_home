package com.green_home_project.config;

import com.green_home_project.model.User;
import com.green_home_project.model.User.Role;
import com.green_home_project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository,
                                BCryptPasswordEncoder passwordEncoder) {

        return args -> {

            // ✅ check if admin already exists
            if (userRepository.findByEmail("admin@green.com").isEmpty()) {

                User admin = new User();
                admin.setName("ADMIN");
                admin.setEmail("admin@green.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                admin.setActive(true);
                admin.setCanSell(false);
                admin.setCanCare(false);

                userRepository.save(admin);

                System.out.println("🔥 ADMIN CREATED: admin@green.com / admin123");
            }
        };
    }
}
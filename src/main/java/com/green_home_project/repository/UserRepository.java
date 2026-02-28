package com.green_home_project.repository;

import com.green_home_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.green_home_project.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
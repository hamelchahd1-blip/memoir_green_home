package com.green_home_project.service;

import org.springframework.stereotype.Service;
import com.green_home_project.model.CarePost;
import com.green_home_project.model.User;
import com.green_home_project.repository.UserRepository;
import com.green_home_project.repository.CarePostRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;

@Service
public class CareService {

    private final CarePostRepository carePostRepository;
    private final UserRepository userRepository;

    public CareService(CarePostRepository carePostRepository,
                       UserRepository userRepository) {
        this.carePostRepository = carePostRepository;
        this.userRepository = userRepository;
    }

    public CarePost addPost(CarePost post) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User caregiver = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        post.setCaregiver(caregiver);

        return carePostRepository.save(post);
    }

    public List<CarePost> getAllPosts() {
        return carePostRepository.findAll();
    }
}
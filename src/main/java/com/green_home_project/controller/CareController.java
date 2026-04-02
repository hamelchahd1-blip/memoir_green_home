package com.green_home_project.controller;
import com.green_home_project.service.CareService;
import jakarta.persistence.*;
import com.green_home_project.model.CarePost;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/care")
public class CareController {

    private final CareService careService;

    public CareController(CareService careService) {
        this.careService = careService;
    }

    // ✅ caregiver يحط post
    @PostMapping("/post")
    @PreAuthorize("hasAnyRole('SELLER', 'USER')")
    public CarePost addPost(@RequestBody CarePost post) {
        return careService.addPost(post);
    }

    // ✅ user يشوف posts
    @GetMapping
    public List<CarePost> getAllPosts() {
        return careService.getAllPosts();
    }
}


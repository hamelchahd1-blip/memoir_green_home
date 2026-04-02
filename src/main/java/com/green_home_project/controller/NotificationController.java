package com.green_home_project.controller;

import com.green_home_project.model.Notification;
import com.green_home_project.model.User;
import com.green_home_project.repository.UserRepository;
import com.green_home_project.service.NotificationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public NotificationController(NotificationService notificationService,
                                  UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    // ✅ Get my notifications
    @GetMapping
    public List<Notification> getNotifications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return notificationService.getUserNotifications(user);
    }

    // ✅ Mark notification as read
    @PutMapping("/read/{id}")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
}

package com.green_home_project.controller;

import com.green_home_project.model.User;
import com.green_home_project.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')") // 🔥 كلش هنا غير للأدمن
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ================= GET ALL USERS =================
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    // ================= DISABLE USER =================
    @PutMapping("/disable/{id}")
    public String disableUser(@PathVariable Long id) {
        adminService.disableUser(id);
        return "User disabled successfully";
    }

    // ================= ENABLE USER =================
    @PutMapping("/enable/{id}")
    public String enableUser(@PathVariable Long id) {
        adminService.enableUser(id);
        return "User enabled successfully";
    }
}

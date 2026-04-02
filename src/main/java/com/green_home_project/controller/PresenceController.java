package com.green_home_project.controller;

import com.green_home_project.service.OnlineUserService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/presence")
public class PresenceController {

    private final OnlineUserService onlineUserService;

    public PresenceController(OnlineUserService onlineUserService) {
        this.onlineUserService = onlineUserService;
    }

    @PostMapping("/online")
    public void setOnline(@RequestParam Long userId) {
        onlineUserService.userOnline(userId);
    }

    @PostMapping("/offline")
    public void setOffline(@RequestParam Long userId) {
        onlineUserService.userOffline(userId);
    }

    @GetMapping("/all")
    public Set<Long> getOnlineUsers() {
        return onlineUserService.getOnlineUsers();
    }
}
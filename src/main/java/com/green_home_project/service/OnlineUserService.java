package com.green_home_project.service;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import com.green_home_project.repository.UserRepository;
import java.time.LocalDateTime;
@Service
public class OnlineUserService {

    private final Set<Long> onlineUsers = ConcurrentHashMap.newKeySet();
    private final UserRepository userRepository;

    public OnlineUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void userOnline(Long userId) {
        onlineUsers.add(userId);
    }

    public void userOffline(Long userId) {
        onlineUsers.remove(userId);

        userRepository.findById(userId).ifPresent(user -> {
            user.setLastSeen(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    public boolean isOnline(Long userId) {
        return onlineUsers.contains(userId);
    }

    public Set<Long> getOnlineUsers() {
        return onlineUsers;
    }
}

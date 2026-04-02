package com.green_home_project.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashSet;
import java.util.Set;

@Component
public class OnlineUsersTracker {

    private final Set<String> onlineUsers = new HashSet<>();
    private final SimpMessagingTemplate messagingTemplate;

    public OnlineUsersTracker(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        if (event.getUser() != null) {
            onlineUsers.add(event.getUser().getName());
            messagingTemplate.convertAndSend("/topic/online", onlineUsers);
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        if (event.getUser() != null) {
            onlineUsers.remove(event.getUser().getName());
            messagingTemplate.convertAndSend("/topic/online", onlineUsers);
        }
    }
}

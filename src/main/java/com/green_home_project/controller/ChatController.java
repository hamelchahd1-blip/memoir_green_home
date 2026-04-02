package com.green_home_project.controller;

import com.green_home_project.dto.ChatMessage;
import com.green_home_project.model.Message;
import com.green_home_project.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.green_home_project.dto.NotificationMessage;
import com.green_home_project.dto.TypingMessage;
@Controller
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(MessageService messageService,
                          SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage chatMessage) {
        Message savedMessage = messageService.sendMessage(
                chatMessage.getSenderId(),
                chatMessage.getReceiverId(),
                chatMessage.getContent(),
                null
        );

        // 📩 message عادي
        messagingTemplate.convertAndSend(
                "/topic/messages/" + chatMessage.getReceiverId(),
                savedMessage
        );

        // 🔔 notification
        NotificationMessage notification = new NotificationMessage(
                "عندك ميساج جديد 💬",
                chatMessage.getSenderId()
        );

        messagingTemplate.convertAndSend(
                "/topic/notifications/" + chatMessage.getReceiverId(),
                notification
        );
    }
    @MessageMapping("/chat.typing")
    public void typing(TypingMessage typingMessage) {

        messagingTemplate.convertAndSend(
                "/topic/typing/" + typingMessage.getReceiverId(),
                typingMessage.getSenderId()
        );
    }
}
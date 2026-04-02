package com.green_home_project.controller;

import com.green_home_project.model.Message;
import com.green_home_project.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin("*")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }




    // 📩 Send message
    @PostMapping("/send")
    public Message sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam String content,
            @RequestParam(required = false) String audioUrl
    ) {
        return messageService.sendMessage(senderId, receiverId, content, audioUrl);
    }

    // 💬 Conversation
    @GetMapping("/conversation")
    public List<Message> getConversation(
            @RequestParam Long user1,
            @RequestParam Long user2
    ) {
        return messageService.getConversation(user1, user2);
    }

    // 👁 Mark as seen
    @PutMapping("/seen")
    public String markAsSeen(
            @RequestParam Long senderId,
            @RequestParam Long receiverId
    ) {
        messageService.markMessagesAsSeen(senderId, receiverId);
        return "Messages marked as seen";
    }

    // 🔔 Unread messages
    @GetMapping("/unread")
    public List<Message> getUnread(@RequestParam Long userId) {
        return messageService.getUnreadMessages(userId);
    }

    // 🔢 Unread count
    @GetMapping("/unread/count")
    public long countUnread(@RequestParam Long userId) {
        return messageService.countUnread(userId);
    }
}
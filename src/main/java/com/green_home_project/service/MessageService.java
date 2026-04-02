package com.green_home_project.service;

import com.green_home_project.model.Message;
import com.green_home_project.model.User;
import com.green_home_project.model.Conversation;
import com.green_home_project.repository.MessageRepository;
import com.green_home_project.repository.UserRepository;
import com.green_home_project.repository.ConversationRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository; // ✅ كان ناقص

    // ✅ Constructor فيه كل repositories
    public MessageService(MessageRepository messageRepository,
                          UserRepository userRepository,
                          ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
    }

    // ✅ unread messages
    public List<Message> getUnreadMessages(Long userId) {
        return messageRepository.findByReceiverIdAndSeenFalse(userId);
    }

    // ✅ send message (with conversation)
    public Message sendMessage(Long senderId, Long receiverId, String content, String audioUrl) {

        User sender = userRepository.findById(senderId).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();

        Conversation conversation = conversationRepository
                .findByUser1IdAndUser2Id(senderId, receiverId)
                .orElse(
                        conversationRepository.findByUser2IdAndUser1Id(senderId, receiverId)
                                .orElseGet(() -> {
                                    Conversation c = new Conversation();
                                    c.setUser1Id(senderId);
                                    c.setUser2Id(receiverId);
                                    return conversationRepository.save(c);
                                })
                );

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setConversation(conversation);
        message.setSeen(false);

        // ✅ مهم: audio optional
        message.setAudioUrl(audioUrl != null ? audioUrl : null);

        return messageRepository.save(message);
    }

    // ✅ conversation messages
    public List<Message> getConversation(Long user1, Long user2) {
        return messageRepository.getConversation(user1, user2);
    }
    public void markMessagesAsSeen(Long senderId, Long receiverId) {

        List<Message> messages = messageRepository
                .getConversation(senderId, receiverId);

        for (Message msg : messages) {
            if (!msg.isSeen() && msg.getReceiver().getId().equals(receiverId)) {
                msg.setSeen(true);
            }
        }

        messageRepository.saveAll(messages);
    }
    public long countUnread(Long userId) {
        return messageRepository.findByReceiverIdAndSeenFalse(userId).size();
    }
}
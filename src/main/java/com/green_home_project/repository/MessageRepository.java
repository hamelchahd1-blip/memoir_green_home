package com.green_home_project.repository;

import com.green_home_project.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
        SELECT m FROM Message m
        WHERE (m.sender.id = :user1 AND m.receiver.id = :user2)
           OR (m.sender.id = :user2 AND m.receiver.id = :user1)
        ORDER BY m.timestamp ASC
    """)
    List<Message> getConversation(Long user1, Long user2);
    List<Message> findByReceiverIdAndSeenFalse(Long receiverId);

}

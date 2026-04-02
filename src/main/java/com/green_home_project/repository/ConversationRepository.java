package com.green_home_project.repository;

import com.green_home_project.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    Optional<Conversation> findByUser2IdAndUser1Id(Long user1Id, Long user2Id);
}

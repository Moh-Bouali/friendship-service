package com.individual_s7.friendship_service.repository;

import com.individual_s7.friendship_service.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Boolean existsByUser1IdAndUser2Id(Long user1Id, Long user2Id);
}

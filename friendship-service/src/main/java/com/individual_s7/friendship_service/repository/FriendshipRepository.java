package com.individual_s7.friendship_service.repository;

import com.individual_s7.friendship_service.model.Friendship;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Boolean existsByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    void deleteAllByUser1IdOrUser2Id(Long user1Id, Long user2Id);

    @Modifying
    @Transactional
    @Query("UPDATE Friendship f " +
            "SET f.user1Username = CASE WHEN f.user1Id = :userId THEN :newUsername ELSE f.user1Username END, " +
            "f.user2Username = CASE WHEN f.user2Id = :userId THEN :newUsername ELSE f.user2Username END " +
            "WHERE f.user1Id = :userId OR f.user2Id = :userId")
    void updateUsernameByUserId(Long userId, String newUsername);
}

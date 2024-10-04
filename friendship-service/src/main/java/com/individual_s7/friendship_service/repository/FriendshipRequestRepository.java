package com.individual_s7.friendship_service.repository;

import com.individual_s7.friendship_service.model.FriendshipRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequest, Long> {
}

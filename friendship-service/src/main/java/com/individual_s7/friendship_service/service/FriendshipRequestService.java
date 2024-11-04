package com.individual_s7.friendship_service.service;

import com.individual_s7.friendship_service.configuration.RabbitMQConfig;
import com.individual_s7.friendship_service.dto.FriendshipRequestRequest;
import com.individual_s7.friendship_service.dto.FriendshipRequestResponse;
import com.individual_s7.friendship_service.event.FriendshipEvent;
import com.individual_s7.friendship_service.event.UserUpdatedEvent;
import com.individual_s7.friendship_service.model.Friendship;
import com.individual_s7.friendship_service.model.FriendshipRequest;
import com.individual_s7.friendship_service.repository.FriendshipRepository;
import com.individual_s7.friendship_service.repository.FriendshipRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendshipRequestService {

    private final FriendshipRequestRepository friendshipRequestRepository;
    private final FriendshipRepository friendshipRepository;
    private final RabbitTemplate rabbitTemplate;

    public void createFriendRequest(FriendshipRequestRequest friendshipRequestRequest){
        FriendshipRequest friendshipRequest = FriendshipRequest.builder()
                .requested_id(friendshipRequestRequest.requested_id())
                .requester_id(friendshipRequestRequest.requester_id())
                .created_at(friendshipRequestRequest.created_at())
                .requester_username(friendshipRequestRequest.requester_username())
                .requested_username(friendshipRequestRequest.requested_username())
                .status(friendshipRequestRequest.status())
                .build();
        friendshipRequestRepository.save(friendshipRequest);

        rabbitTemplate.convertAndSend(RabbitMQConfig.FRIEND_REQUEST_EXCHANGE,
                RabbitMQConfig.FRIEND_REQUEST_ROUTING_KEY, friendshipRequest);
    }

    @Transactional
    public void handleFriendshipResponse(FriendshipRequestResponse response) {
        // Retrieve the friendship request
        FriendshipRequest request = friendshipRequestRepository.findById(response.id())
                .orElseThrow(() -> new RuntimeException("Friendship request not found"));

        if ("ACCEPTED".equalsIgnoreCase(response.status())) {
            // Create new friendship
            Friendship newFriendship = Friendship.builder()
                    .user1Id(response.requester_id())
                    .user2Id(response.requested_id())
                    .user1Username(response.requester_username())
                    .user2Username(response.requested_username())
                    .since(response.created_at())
                    .build();
            friendshipRepository.save(newFriendship);

            // Emit event to RabbitMQ
            FriendshipEvent event = FriendshipEvent.builder()
                            .requester_id(response.requester_id())
                                .requested_id(response.requested_id())
                                    .requested_username(response.requested_username())
                                            .requester_username(response.requester_username())
                                                    .build();

            rabbitTemplate.convertAndSend(RabbitMQConfig.FRIENDSHIP_RESPONSE_EXCHANGE,
                    RabbitMQConfig.FRIENDSHIP_RESPONSE_ROUTING_KEY, event);
        }

        // Delete friendship request after processing
        friendshipRequestRepository.delete(request);
    }

    // delete user friendship by id that is listened to from rabbitmq
    @Transactional
    @RabbitListener(queues = RabbitMQConfig.USER_DELETE_QUEUE)
    public void deleteUserFriendship(Long id){
        friendshipRepository.deleteAllByUser1IdOrUser2Id(id, id);
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.USER_UPDATE_QUEUE)
    public void updateUserFriendship(UserUpdatedEvent userUpdatedEvent){
        friendshipRepository.updateUsernameByUserId(userUpdatedEvent.getId(), userUpdatedEvent.getUsername());
    }

    public Boolean checkFriendship(Long user1_id, Long user2_id){
        return friendshipRepository.existsByUser1IdAndUser2Id(user1_id, user2_id) ||
                friendshipRepository.existsByUser1IdAndUser2Id(user2_id, user1_id);
    }
}

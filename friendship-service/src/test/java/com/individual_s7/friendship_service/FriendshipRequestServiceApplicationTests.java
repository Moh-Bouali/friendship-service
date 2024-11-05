package com.individual_s7.friendship_service;

import com.individual_s7.friendship_service.configuration.RabbitMQConfig;
import com.individual_s7.friendship_service.dto.FriendshipRequestRequest;
import com.individual_s7.friendship_service.dto.FriendshipRequestResponse;
import com.individual_s7.friendship_service.event.FriendshipEvent;
import com.individual_s7.friendship_service.event.UserUpdatedEvent;
import com.individual_s7.friendship_service.model.Friendship;
import com.individual_s7.friendship_service.model.FriendshipRequest;
import com.individual_s7.friendship_service.repository.FriendshipRepository;
import com.individual_s7.friendship_service.repository.FriendshipRequestRepository;
import com.individual_s7.friendship_service.service.FriendshipRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FriendshipRequestServiceApplicationTests {

	@Mock
	private FriendshipRequestRepository friendshipRequestRepository;

	@Mock
	private FriendshipRepository friendshipRepository;

	@Mock
	private RabbitTemplate rabbitTemplate;

	@InjectMocks
	private FriendshipRequestService friendshipRequestService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		doNothing().when(rabbitTemplate).convertAndSend(any(String.class), any(String.class), any(Object.class));
	}

	// Test for createFriendRequest method
	@Test
	void testCreateFriendRequest() {
		FriendshipRequestRequest request = new FriendshipRequestRequest(
				1L,
				2L,
				"requester",
				3L,
				"requested",
				"PENDING",
				Timestamp.from(Instant.parse("2021-08-01T00:00:00.000Z"))
		);

		friendshipRequestService.createFriendRequest(request);

		verify(friendshipRequestRepository, times(1)).save(any(FriendshipRequest.class));
		verify(rabbitTemplate, times(1)).convertAndSend(eq(RabbitMQConfig.FRIEND_REQUEST_EXCHANGE),
				eq(RabbitMQConfig.FRIEND_REQUEST_ROUTING_KEY), any(FriendshipRequest.class));
	}

	// Test for handleFriendshipResponse method with ACCEPTED response
	@Test
	void testHandleFriendshipResponseAccepted() {
		FriendshipRequest request = new FriendshipRequest();
		when(friendshipRequestRepository.findById(anyLong())).thenReturn(Optional.of(request));
		FriendshipRequestResponse response = new FriendshipRequestResponse(1L, 3L, "requested", 2L, "requester" ,"ACCEPTED", Timestamp.from(Instant.parse("2021-08-01T00:00:00.000Z")));

		friendshipRequestService.handleFriendshipResponse(response);

		verify(friendshipRepository, times(1)).save(any(Friendship.class));
		verify(friendshipRequestRepository, times(1)).delete(request);
		verify(rabbitTemplate, times(1)).convertAndSend(eq(RabbitMQConfig.FRIENDSHIP_RESPONSE_EXCHANGE),
				eq(RabbitMQConfig.FRIENDSHIP_RESPONSE_ROUTING_KEY), any(FriendshipEvent.class));
	}

	// Test for deleteUserFriendship method
	@Test
	void testDeleteUserFriendship() {
		friendshipRequestService.deleteUserFriendship(1L);

		verify(friendshipRepository, times(1)).deleteAllByUser1IdOrUser2Id(1L, 1L);
	}

	// Test for updateUserFriendship method
	@Test
	void testUpdateUserFriendship() {
		UserUpdatedEvent event = new UserUpdatedEvent(1L, "newUsername");
		friendshipRequestService.updateUserFriendship(event);

		verify(friendshipRepository, times(1)).updateUsernameByUserId(1L, "newUsername");
	}

	// Test for checkFriendship method with an existing friendship
	@Test
	void testCheckFriendship_Exists() {
		when(friendshipRepository.existsByUser1IdAndUser2Id(1L, 2L)).thenReturn(true);

		Boolean exists = friendshipRequestService.checkFriendship(1L, 2L);

		assertTrue(exists);
		verify(friendshipRepository, times(1)).existsByUser1IdAndUser2Id(1L, 2L);
		verify(friendshipRepository, never()).existsByUser1IdAndUser2Id(2L, 1L);
	}

	// Test for checkFriendship method with a non-existing friendship
	@Test
	void testCheckFriendship_NotExists() {
		when(friendshipRepository.existsByUser1IdAndUser2Id(1L, 2L)).thenReturn(false);
		when(friendshipRepository.existsByUser1IdAndUser2Id(2L, 1L)).thenReturn(false);

		Boolean exists = friendshipRequestService.checkFriendship(1L, 2L);

		assertFalse(exists);
		verify(friendshipRepository, times(1)).existsByUser1IdAndUser2Id(1L, 2L);
		verify(friendshipRepository, times(1)).existsByUser1IdAndUser2Id(2L, 1L);
	}
}


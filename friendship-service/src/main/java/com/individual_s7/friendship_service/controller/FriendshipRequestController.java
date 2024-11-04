package com.individual_s7.friendship_service.controller;

import com.individual_s7.friendship_service.dto.FriendshipRequestRequest;
import com.individual_s7.friendship_service.dto.FriendshipRequestResponse;
import com.individual_s7.friendship_service.service.FriendshipRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/friendship/")
public class FriendshipRequestController {

    private final FriendshipRequestService friendshipRequestService;

    @PostMapping("/request")
    public ResponseEntity<String> addAFriend(@RequestBody FriendshipRequestRequest friendshipRequestRequest){
        friendshipRequestService.createFriendRequest(friendshipRequestRequest);
        return ResponseEntity.ok("Added");
    }

    @PostMapping("/response")
    public ResponseEntity<String> handleFriendshipResponse(@RequestBody FriendshipRequestResponse friendshipRequestResponse){
        friendshipRequestService.handleFriendshipResponse(friendshipRequestResponse);
        return ResponseEntity.ok("Handled");
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFriendship(
            @RequestHeader("X-User-Id") Long user1Id,
            @RequestHeader("X-Friend-Id") Long user2Id) {
        boolean isFriend = friendshipRequestService.checkFriendship(user1Id, user2Id);
        return ResponseEntity.ok(isFriend);
    }
}

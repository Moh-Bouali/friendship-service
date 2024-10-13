package com.individual_s7.friendship_service.event;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendshipEvent implements Serializable {
    private Long requester_id;
    private String requester_username;
    private String requested_username;
}

package com.individual_s7.friendship_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "t_friendship_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendshipRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long requester_id;
    private String requester_username;
    private Long requested_id;
    private String requested_username;
    private String status;
    private Timestamp created_at;
}

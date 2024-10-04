package com.individual_s7.friendship_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "t_friendships")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long user1_id;
    private String user1_username;
    private Long user2_id;
    private String user2_username;
    private Timestamp since;
}

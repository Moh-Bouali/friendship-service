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

    @Column(name = "user1_id")
    private Long user1Id;

    @Column(name = "user1_username")
    private String user1Username;

    @Column(name = "user2_id")
    private Long user2Id;

    @Column(name = "user2_username")
    private String user2Username;

    private Timestamp since;
}

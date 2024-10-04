CREATE TABLE Friendships (
        id bigint(20) PRIMARY KEY,
        user1_id bigint(20) NOT NULL,
        user1_username VARCHAR(255),
        user2_id bigint(20) NOT NULL,
        user2_username VARCHAR(255),
        since TIMESTAMP DEFAULT NOW()
);

CREATE TABLE FriendshipRequests (
        id bigint(20) PRIMARY KEY,
        requester_id bigint(20) NOT NULL,
        requester_username VARCHAR(255),
        requested_id bigint(20) NOT NULL,
        requested_username VARCHAR(255),
        request_status VARCHAR(255),
        created_at TIMESTAMP DEFAULT NOW()
);
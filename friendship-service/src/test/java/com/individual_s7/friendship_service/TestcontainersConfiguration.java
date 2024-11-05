package com.individual_s7.friendship_service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    MySQLContainer<?> mySqlContainer() {
        return new MySQLContainer<>(DockerImageName.parse("mysql:latest"))
                .withUsername("testuser")       // Set your desired username
                .withPassword("testpassword")   // Set your desired password
                .withDatabaseName("testdb");    // Set your desired database name
    }

}

package com.laszlogulyas.kitchensink_migrated.acceptance;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
@Tag("AcceptanceTest")
public abstract class AbstractAcceptanceTest {

    protected static final int MONGODB_PORT = 27017;
    protected static final String MONGODB_DATABASE = "test";

    @Container
    protected static final MongoDBContainer mongoDBContainer;
    protected static final MongoClient mongoClient;
    protected static final MongoTemplate mongoTemplate;
    protected static final RestTemplate restTemplate;

    static {
        mongoDBContainer = new MongoDBContainer("mongo:latest").withExposedPorts(MONGODB_PORT);
        mongoDBContainer.start();
        mongoClient = MongoClients.create(mongoDBContainer.getConnectionString());
        mongoTemplate = new MongoTemplate(mongoClient, MONGODB_DATABASE);
        restTemplate = new RestTemplate();
    }

    @DynamicPropertySource
    protected static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.port", () -> mongoDBContainer.getMappedPort(MONGODB_PORT));
    }

    @AfterAll
    public static void cleanUp() {
        mongoClient.close();
        mongoDBContainer.close();
    }
}

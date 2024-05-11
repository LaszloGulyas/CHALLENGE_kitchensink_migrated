package com.laszlogulyas.kitchensink_migrated.acceptance;

import com.laszlogulyas.kitchensink_migrated.model.Member;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
public class AcceptanceApiMemberTest extends AbstractAcceptanceTest {

    private static final String BASE_URL_MEMBERS = "http://localhost:8080/kitchensink/rest/members";
    private static final Member TEST_MEMBER = new Member(
            "663f504e9388508823166a61",
            "John Smith",
            "john.smith@mailinator.com",
            "2125551212"
    );

    @BeforeAll
    public static void setup() {
        insertTestMemberDocument();
    }

    @Test
    public void testGetSampleUser() {
        String url = BASE_URL_MEMBERS + "/" + TEST_MEMBER.getId();
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<Map<String, Object>> actualResponseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

        Map<String, Object> actualResponse = actualResponseEntity.getBody();
        assertAll(
                () -> assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode()),
                () -> assertEquals(TEST_MEMBER.getId(), actualResponse.get("id")),
                () -> assertEquals(TEST_MEMBER.getName(), actualResponse.get("name")),
                () -> assertEquals(TEST_MEMBER.getEmail(), actualResponse.get("email")),
                () -> assertEquals(TEST_MEMBER.getPhoneNumber(), actualResponse.get("phoneNumber"))
        );
    }

    @Test
    public void testGetAllUsers() {
        ParameterizedTypeReference<List<Map<String, Object>>> responseType = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<Map<String, Object>>> actualResponse = restTemplate.exchange(BASE_URL_MEMBERS, HttpMethod.GET, null, responseType);

        Map<String, Object> firstRecord = Objects.requireNonNull(actualResponse.getBody()).getFirst();
        assertAll(
                () -> assertEquals(HttpStatus.OK, actualResponse.getStatusCode()),
                () -> assertNotNull(firstRecord.get("id")),
                () -> assertNotNull(firstRecord.get("name")),
                () -> assertNotNull(firstRecord.get("email")),
                () -> assertNotNull(firstRecord.get("phoneNumber"))
        );
    }

    @Test
    public void testCreateNewUserOK() {
        String randomEmailAddress = UUID.randomUUID().toString().replace("-", "") + "@test.test";
        Map<String, Object> request = Map.of("name", "Test Test", "email", randomEmailAddress, "phoneNumber", "1231231230");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(BASE_URL_MEMBERS, request, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    private static void insertTestMemberDocument() {
        Document testDocument = new Document("_id", new ObjectId(TEST_MEMBER.getId()));
        testDocument.put("name", TEST_MEMBER.getName());
        testDocument.put("email", TEST_MEMBER.getEmail());
        testDocument.put("phone_number", TEST_MEMBER.getPhoneNumber());
        mongoTemplate.getCollection("member").insertOne(testDocument);
    }
}

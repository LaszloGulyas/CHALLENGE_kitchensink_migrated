package acceptance;

import com.laszlogulyas.kitchensink_migrated.KitchensinkMigratedApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = KitchensinkMigratedApplication.class)
public class JbossAcceptanceTest {

    private static final String BASE_URL_MEMBERS = "http://localhost:8080/kitchensink/rest/members";

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testGetSampleUser() {
        String url = BASE_URL_MEMBERS + "/0";
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<Map<String, Object>> actualResponseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

        Map<String, Object> actualResponse = actualResponseEntity.getBody();
        assertAll(
                () -> assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode()),
                () -> assertEquals(0, actualResponse.get("id")),
                () -> assertEquals("John Smith", actualResponse.get("name")),
                () -> assertEquals("john.smith@mailinator.com", actualResponse.get("email")),
                () -> assertEquals("2125551212", actualResponse.get("phoneNumber"))
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
}

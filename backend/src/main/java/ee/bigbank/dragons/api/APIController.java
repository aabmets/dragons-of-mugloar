package ee.bigbank.dragons.api;

import ee.bigbank.dragons.client.DragonsApiClient;
import ee.bigbank.dragons.client.GameStartResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Tag(name = "Game", description = "Dragons of Mugloar game controller API")
public class APIController {

    @Autowired
    private DragonsApiClient dragonsApiClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/new-game")
    @Operation(
            summary = "Start a new game",
            description = "Start a new game"
    )
    public ResponseEntity<GameStartResponse> newGame(
            @RequestParam(name = "username", defaultValue = "Anonymous") String username
    ) {
        GameStartResponse response = dragonsApiClient.startGame();
        String uuid = UUID.randomUUID().toString();

        response.setUuid(uuid);
        response.setTimestamp(Instant.now().toString());

        try {
            String value = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(uuid, value);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Failed to serialize GameStartResponse",
                ex
            );
        }

        return ResponseEntity.ok(response);
    }

}

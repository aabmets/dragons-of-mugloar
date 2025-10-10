package ee.bigbank.dragons.api;

import ee.bigbank.dragons.client.GameState;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class NewGameController extends APIController {

    @PostMapping("/new-game")
    @Operation(summary = "Start a new game")
    public ResponseEntity<GameState> newGame(
            @RequestParam(name = "username", defaultValue = "Anonymous") String username
    ) {
        GameState response = dragonsApiClient.startGame();
        String uuid = UUID.randomUUID().toString();

        response.setUuid(uuid);
        response.setUsername(username);
        response.setTimestamp(Instant.now().toString());

        try {
            String value = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(uuid, value);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to serialize GameState",
                    ex
            );
        }

        return ResponseEntity.ok(response);
    }

}

package ee.bigbank.dragons.api;

import ee.bigbank.dragons.client.GameState;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api")
public class SolveMessageController extends APIController {

    @PostMapping("/solve-message")
    @Operation(summary = "Try to solve a message from the message board.")
    public ResponseEntity<GameState> solveMessage(
            @RequestParam(name = "uuid", required = true) String uuid,
            @RequestParam(name = "adId", required = true) String adId
    ) {
        try {
            String json = redisTemplate.opsForValue().get(uuid);
            if (json == null) {
                return ResponseEntity.badRequest().build();
            }

            GameState stored = objectMapper.readValue(json, GameState.class);
            String gameId = stored.getGameId();

            if (gameId == null || gameId.isBlank()) {
                return ResponseEntity.badRequest().build();
            }

            GameState response = dragonsApiClient.solveMessage(gameId, adId);

            response.setUuid(uuid);
            response.setGameId(gameId);
            response.setLevel(stored.getLevel());
            response.setUsername(stored.getUsername());
            response.setTimestamp(Instant.now().toString());

            String value = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(uuid, value);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}

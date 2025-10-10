package ee.bigbank.dragons.api;

import ee.bigbank.dragons.client.GameState;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class GameHistoryController extends APIController {

    @GetMapping("/games-history")
    @Operation(summary = "Fetch all historical games")
    public ResponseEntity<List<GameState>> getGames(
            @RequestParam(name = "limit", required = false) Integer limit
    ) {
        Set<String> keys = redisTemplate.keys("*");
        if (keys.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        final int take = (limit == null || limit <= 0) ? 10 : limit;

        Comparator<GameState> byHighScoreDesc =
                Comparator.comparingInt(GameState::getHighScore).reversed();

        Stream<GameState> stream = keys.stream()
                .map(k -> redisTemplate.opsForValue().get(k))
                .filter(Objects::nonNull)
                .map(v -> {
                    try {
                        return objectMapper.readValue(v, GameState.class);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .sorted(byHighScoreDesc)
                .limit(take);

        List<GameState> result = stream.collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

}

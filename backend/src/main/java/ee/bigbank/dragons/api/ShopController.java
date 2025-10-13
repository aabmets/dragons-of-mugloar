package ee.bigbank.dragons.api;

import ee.bigbank.dragons.client.GameState;
import ee.bigbank.dragons.client.Purchase;
import ee.bigbank.dragons.client.ShopItem;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Instant;

@RestController
@RequestMapping("/api")
public class ShopController extends APIController {

    @GetMapping("/shop")
    @Operation(summary = "Fetch all items available in the shop")
    public ResponseEntity<Flux<ShopItem>> getShopItems(
            @RequestParam(name = "gameId", required = true) String gameId
    ) {
        Flux<ShopItem> items = dragonsApiClient.getShopItems(gameId);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/skip-turn")
    @Operation(summary = "Skip a turn without taking an action, allowing the message board to update")
    public ResponseEntity<Boolean> skipTurn(
            @RequestParam(name = "gameId", required = true) String gameId
    ) {
        Purchase purchase = dragonsApiClient.buyItem(gameId, "skipturn");
        if (purchase.getShoppingSuccess() == false) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/buy-item")
    @Operation(summary = "Buy an item from the shop")
    public ResponseEntity<GameState> buyItem(
            @RequestParam(name = "uuid", required = true) String uuid,
            @RequestParam(name = "itemId", required = true) String itemId
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

            Purchase response = dragonsApiClient.buyItem(gameId, itemId);

            stored.setGold(response.getGold());
            stored.setLives(response.getLives());
            stored.setLevel(response.getLevel());
            stored.setTurn(response.getTurn());
            stored.setSuccess(response.getShoppingSuccess());
            stored.setTimestamp(Instant.now().toString());

            if (response.getShoppingSuccess() == true) {
                stored.setMessage("purchaseSuccess " + itemId);
            } else {
                stored.setMessage("purchaseFailure " + itemId);
            }

            String value = objectMapper.writeValueAsString(stored);
            redisTemplate.opsForValue().set(uuid, value);

            return ResponseEntity.ok(stored);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}

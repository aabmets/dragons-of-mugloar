package ee.bigbank.dragons.api;

import ee.bigbank.dragons.client.Purchase;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SkipTurnController extends APIController {

    @PostMapping("/skip-turn")
    @Operation(summary = "Skip a turn without taking an action, allowing the message board to update")
    public ResponseEntity<Boolean> skipTurn(
            @RequestParam(name = "gameId", required = true) String gameId
    ) {
        Purchase purchase = dragonsApiClient.skipTurn(gameId);
        if (purchase.getShoppingSuccess() == false) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().build();
    }

}

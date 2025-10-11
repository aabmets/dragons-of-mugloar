package ee.bigbank.dragons.api;

import ee.bigbank.dragons.client.Reputation;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class ReputationController extends APIController {

    @GetMapping("/reputation")
    @Operation(summary = "Fetch all messages from the message board")
    public ResponseEntity<Flux<Reputation>> getReputation(
            @RequestParam(name = "gameId", required = true) String gameId
    ) {
        Flux<Reputation> messages = dragonsApiClient.getReputation(gameId);
        return ResponseEntity.ok(messages);
    }

}

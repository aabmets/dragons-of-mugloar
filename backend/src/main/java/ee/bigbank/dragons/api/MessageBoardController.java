package ee.bigbank.dragons.api;

import ee.bigbank.dragons.client.MessageBoard;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class MessageBoardController extends APIController {

    @GetMapping("/messages")
    @Operation(summary = "Fetch all messages from the message board")
    public ResponseEntity<Flux<MessageBoard>> getMessages(
            @RequestParam(name = "gameId", required = true) String gameId
    ) {
        Flux<MessageBoard> messages = dragonsApiClient.getMessageBoard(gameId);
        return ResponseEntity.ok(messages);
    }

}

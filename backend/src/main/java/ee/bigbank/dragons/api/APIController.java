package ee.bigbank.dragons.api;

import ee.bigbank.dragons.client.DragonsApiClient;
import ee.bigbank.dragons.client.GameStartResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Game", description = "Dragons of Mugloar game controller API")
public class APIController {

    @Autowired
    private DragonsApiClient dragonsApiClient;

    @PostMapping("/new-game")
    @Operation(
            summary = "Start a new game",
            description = "Start a new game"
    )
    public ResponseEntity<GameStartResponse> newGame() {
        return ResponseEntity.ok(dragonsApiClient.startGame());
    }

}

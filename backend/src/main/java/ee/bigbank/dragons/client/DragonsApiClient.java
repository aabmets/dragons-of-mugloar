package ee.bigbank.dragons.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class DragonsApiClient {

    private static final String BASE_URL = "https://dragonsofmugloar.com/api/v2";
    private final WebClient webClient;

    public DragonsApiClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Starts a new game.
     * Makes POST to https://dragonsofmugloar.com/api/v2/game/start
     * and returns the parsed response.
     */
    public GameStartResponse startGame() {
        return webClient.post()
                .uri("/game/start")
                .retrieve()
                .bodyToMono(GameStartResponse.class)
                .block();
    }
}

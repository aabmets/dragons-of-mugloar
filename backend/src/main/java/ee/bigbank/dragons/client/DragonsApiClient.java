package ee.bigbank.dragons.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

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
     * Sends a POST request to the "/game/start" endpoint to initiate a new game.
     * @return a {@link GameState} object containing the initial game state information.
     */
    public GameState startGame() {
        return webClient.post()
                .uri("/game/start")
                .retrieve()
                .bodyToMono(GameState.class)
                .block();
    }

    /**
     * Retrieves the message board for a specific game based on the provided game ID.
     * @param gameId the unique identifier of the game for which the message board is being requested.
     * @return a {@link MessageBoard} object containing the messages associated with the game.
     */
    public Flux<MessageBoard> getMessageBoard(String gameId) {
        return webClient.get()
                .uri("/{gameId}/messages", gameId)
                .retrieve()
                .bodyToFlux(MessageBoard.class);
    }

    /**
     * Sends a POST request to solve a specific message in a game based on the provided game ID and advertisement ID.
     * @param gameId the unique identifier of the game where the message is located.
     * @param adId the unique identifier of the advertisement or message to be solved.
     * @return a {@link GameState} object reflecting the changes after solving the message.
     */
    public GameState solveMessage(String gameId, String adId) {
        return webClient.post()
                .uri("/{gameId}/solve/{adId}", gameId, adId)
                .retrieve()
                .bodyToMono(GameState.class)
                .block();
    }

    /**
     * Retrieves the reputation metrics for a specific game based on the provided game ID.
     * @param gameId the unique identifier of the game for which reputation data is being requested.
     * @return a {@link Flux} of a {@link Reputation} object, representing the reputation across factions.
     */
    public Flux<Reputation> getReputation(String gameId) {
        return webClient.post()
                .uri("/{gameId}/investigate/reputation", gameId)
                .retrieve()
                .bodyToFlux(Reputation.class);
    }

    /**
     * Purchase an item from the shop in a specific game.
     * @param gameId the unique identifier of the game where the item is being purchased.
     * @param itemId the unique identifier of the item to be purchased.
     * @return a {@link Purchase} object containing information about the purchase result.
     */
    public Purchase buyItem(String gameId, String itemId) {
        return webClient.post()
                .uri("/{gameId}/shop/buy/{itemId}", gameId, itemId)
                .retrieve()
                .bodyToMono(Purchase.class)
                .block();
    }

    /**
     * Retrieves the list of shop items available for purchase in the specified game.
     * @param gameId the unique identifier of the game for which the shop items are being requested.
     * @return a {@link Flux} of {@link ShopItem} objects representing the available items in the shop.
     */
    public Flux<ShopItem> getShopItems(String gameId) {
        return webClient.get()
                .uri("/{gameId}/shop", gameId)
                .retrieve()
                .bodyToFlux(ShopItem.class);
    }

}

package ee.bigbank.dragons.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameState {

    @JsonProperty("gameId")
    private String gameId;

    @JsonProperty("lives")
    private Long lives;

    @JsonProperty("gold")
    private Long gold;

    @JsonProperty("level")
    private Long level;

    @JsonProperty("score")
    private Long score;

    @JsonProperty("highScore")
    private Long highScore;

    @JsonProperty("turn")
    private Long turn;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("message")
    private String message;

    private String uuid;
    private String timestamp;
    private String username;
}

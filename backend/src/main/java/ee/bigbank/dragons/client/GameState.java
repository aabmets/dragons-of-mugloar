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
    private long lives;

    @JsonProperty("gold")
    private long gold;

    @JsonProperty("level")
    private long level;

    @JsonProperty("score")
    private long score;

    @JsonProperty("highScore")
    private long highScore;

    @JsonProperty("turn")
    private long turn;

    private String uuid;
    private String timestamp;
    private String username;
}

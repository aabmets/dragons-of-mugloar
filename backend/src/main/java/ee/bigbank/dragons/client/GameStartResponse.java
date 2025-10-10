package ee.bigbank.dragons.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameStartResponse {

    @JsonProperty("gameId")
    private String gameId;

    @JsonProperty("lives")
    private int lives;

    @JsonProperty("gold")
    private int gold;

    @JsonProperty("level")
    private int level;

    @JsonProperty("score")
    private int score;

    @JsonProperty("highScore")
    private int highScore;

    @JsonProperty("turn")
    private int turn;

    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }

    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = lives; }

    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getHighScore() { return highScore; }
    public void setHighScore(int highScore) { this.highScore = highScore; }

    public int getTurn() { return turn; }
    public void setTurn(int turn) { this.turn = turn; }
}

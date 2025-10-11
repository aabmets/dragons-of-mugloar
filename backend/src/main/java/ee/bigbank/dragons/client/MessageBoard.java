package ee.bigbank.dragons.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageBoard {
    @JsonProperty("adId")
    private String adId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("reward")
    private int reward;

    @JsonProperty("expiresIn")
    private int expiresIn;

    @JsonProperty("encrypted")
    private Integer encrypted;

    @JsonProperty("probability")
    private String probability;

    @JsonCreator
    @Builder
    public MessageBoard(
            @JsonProperty("adId") String adId,
            @JsonProperty("message") String message,
            @JsonProperty("reward") int reward,
            @JsonProperty("expiresIn") int expiresIn,
            @JsonProperty("encrypted") Integer encrypted,
            @JsonProperty("probability") String probability
    ) {
        boolean shouldDecode = (encrypted != null);

        this.adId        = decodeIf(shouldDecode, adId);
        this.message     = decodeIf(shouldDecode, message);
        this.probability = decodeIf(shouldDecode, probability);

        this.reward   = reward;
        this.expiresIn = expiresIn;
        this.encrypted = shouldDecode ? 0 : null;
    }

    private static String decodeIf(boolean shouldDecode, String value) {
        if (!shouldDecode || value == null) return value;
        try {
            byte[] bytes = Base64.getDecoder().decode(value);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException badBase64) {
            return value;
        }
    }
}

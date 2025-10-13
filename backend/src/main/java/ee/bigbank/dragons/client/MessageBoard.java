package ee.bigbank.dragons.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageBoard {
    @JsonProperty("adId")
    private String adId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("reward")
    private Integer reward;

    @JsonProperty("expiresIn")
    private Integer expiresIn;

    @JsonProperty("encrypted")
    private Integer encrypted;

    @JsonProperty("probability")
    private String probability;

    private static final Set<String> ALLOWED = new HashSet<>(Arrays.asList(
        "Piece of cake",
        "Walk in the park",
        "Sure thing",
        "Quite likely",
        "Gamble",
        "Hmmm....",
        "Risky",
        "Rather detrimental",
        "Playing with fire",
        "Suicide mission"
    ));

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
        this.reward = reward;
        this.expiresIn = expiresIn;
        this.encrypted = encrypted;

        if (isEncryptedFlagSet(encrypted)) {
            String decoded = b64dec(probability);
            if (decoded != null && ALLOWED.contains(decoded)) {
                this.adId = b64dec(adId);
                this.message = b64dec(message);
                this.probability = decoded;
            } else {
                this.adId = rot13dec(adId);
                this.message = rot13dec(message);
                this.probability = rot13dec(probability);
            }
        } else {
            this.adId = adId;
            this.message = message;
            this.probability = probability;
        }

    }

    private boolean isEncryptedFlagSet(Object encrypted) {
        return Boolean.TRUE.equals(encrypted) ||
            (encrypted instanceof Number && ((Number) encrypted).intValue() != 0);
    }

    private static String rot13dec(String str) {
        StringBuilder builder = new StringBuilder(str.length());

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (c >= 'a' && c <= 'z') {
                builder.append((char)('a' + (c - 'a' + 13) % 26));
            } else if (c >= 'A' && c <= 'Z') {
                builder.append((char)('A' + (c - 'A' + 13) % 26));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    private static String b64dec(String str) {
        try {
            return new String(
                Base64.getDecoder().decode(str),
                StandardCharsets.UTF_8
            );
        } catch (IllegalArgumentException bad) {
            return null;
        }
    }

}

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
public class Purchase {
    @JsonProperty("shoppingSuccess")
    private Boolean shoppingSuccess;

    @JsonProperty("lives")
    private Long lives;

    @JsonProperty("gold")
    private Long gold;

    @JsonProperty("level")
    private Long level;

    @JsonProperty("turn")
    private Long turn;

}

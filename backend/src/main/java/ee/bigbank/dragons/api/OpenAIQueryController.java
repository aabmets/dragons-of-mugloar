package ee.bigbank.dragons.api;

import org.springframework.beans.factory.annotation.Value;
import ee.bigbank.dragons.service.AIService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OpenAIQueryController extends APIController {

    private final AIService aiService;

    @Value("${spring.ai.openai.api-key:}")
    private String openAiKey;

    public OpenAIQueryController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping(path = "/query-ai", consumes = "text/plain", produces = "text/plain")
    @Operation(summary = "Query OpenAI with a text prompt and return the model's response")
    public ResponseEntity<String> queryPlain(@RequestBody String prompt) {
        if ("none".equalsIgnoreCase(openAiKey)) {
            return ResponseEntity.ok("[]");
        }
        if (prompt == null || prompt.isBlank()) {
            return ResponseEntity.badRequest().body("Body must contain non-empty text.");
        }
        return ResponseEntity.ok(aiService.generateText(prompt));
    }

}

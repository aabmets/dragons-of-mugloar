package ee.bigbank.dragons.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final ChatClient chat;

    @Value("${spring.ai.openai.api-key:}")
    private String openAiKey;

    public AIService(ChatClient.Builder builder) {
        this.chat = builder.build();
    }

    public String generateText(String prompt) {
        if ("none".equalsIgnoreCase(openAiKey)) {
            throw new IllegalStateException("OpenAI is disabled (api-key == 'none').");
        }
        if (prompt == null || prompt.isBlank()) {
            return "";
        }
        return chat
            .prompt()
            .user(prompt)
            .call()
            .content();
    }
}

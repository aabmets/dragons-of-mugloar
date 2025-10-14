package ee.bigbank.dragons.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final ChatClient chat;

    public AIService(ChatClient.Builder builder) {
        this.chat = builder.build();
    }

    public String generateText(String prompt) {
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

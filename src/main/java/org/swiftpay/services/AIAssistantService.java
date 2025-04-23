package org.swiftpay.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AIAssistantService {

    @Value("${ai.prompt}")
    private String prompt;

    private final ChatClient chatClient;

    public AIAssistantService (ChatClient.Builder chatClient) {

        this.chatClient = chatClient.build();

    }

    public String generateResponse (String message) {

        return chatClient
                .prompt()
                .system(prompt)
                .user(message)
                .call()
                .content();

    }

}

package org.swiftpay.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIAssistantService {

    private final ChatClient chatClient;

    public AIAssistantService (ChatClient.Builder chatClient) {

        this.chatClient = chatClient.build();

    }

}

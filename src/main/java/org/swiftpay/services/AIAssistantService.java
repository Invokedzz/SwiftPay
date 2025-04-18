package org.swiftpay.services;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIAssistantService {

    private final ChatClient chatClient;

    public AIAssistantService (ChatClient.Builder chatClient) {

        this.chatClient = chatClient.build();

    }



}

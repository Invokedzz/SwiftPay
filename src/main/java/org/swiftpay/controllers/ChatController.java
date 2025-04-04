package org.swiftpay.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.swiftpay.services.AIAssistantService;

@RestController
public class ChatController {

    private final AIAssistantService aiAssistantService;

    public ChatController (AIAssistantService aiAssistantService) {

        this.aiAssistantService = aiAssistantService;

    }

}

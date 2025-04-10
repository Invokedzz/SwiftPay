package org.swiftpay.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.swiftpay.services.AIAssistantService;

@RestController
public record ChatController (AIAssistantService aiAssistantService) {



}

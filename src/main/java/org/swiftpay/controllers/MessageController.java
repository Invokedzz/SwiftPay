package org.swiftpay.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.swiftpay.services.MessageService;

@RestController
public record MessageController (MessageService messageService) {
}

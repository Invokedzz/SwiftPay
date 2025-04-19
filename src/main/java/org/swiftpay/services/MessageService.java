package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.AIResponseDTO;
import org.swiftpay.dtos.CreateMessageDTO;
import org.swiftpay.dtos.EditMessageDTO;
import org.swiftpay.model.ChatMessages;
import org.swiftpay.repositories.MessagesRepository;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final AIAssistantService assistantService;

    private final MessagesRepository messagesRepository;

    private final ChatService chatService;

    public AIResponseDTO createMessage (CreateMessageDTO createMessageDTO) {

        var chat = chatService.findChatById(createMessageDTO.chatId());

       var message = new ChatMessages(createMessageDTO.message(), chat);

       var aiResponse = assistantService.generateResponse(createMessageDTO.message());

       messagesRepository.save(message);

       return new AIResponseDTO(aiResponse);

    }

    @Transactional
    public void editMessage (EditMessageDTO editMessageDTO, Long id) {

    }

    @Transactional
    public void deleteMessage (Long id) {

    }

}

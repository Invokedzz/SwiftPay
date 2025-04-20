package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.AIResponseDTO;
import org.swiftpay.dtos.CreateMessageDTO;
import org.swiftpay.dtos.EditMessageDTO;
import org.swiftpay.dtos.MessageDTO;
import org.swiftpay.exceptions.ForbiddenAccessException;
import org.swiftpay.exceptions.MessageNotFoundException;
import org.swiftpay.model.ChatMessages;
import org.swiftpay.repositories.MessagesRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final AIAssistantService assistantService;

    private final MessagesRepository messagesRepository;

    private final TokenAuthService tokenAuthService;

    private final ChatService chatService;

    public AIResponseDTO createMessage (CreateMessageDTO createMessageDTO) {

        var chat = chatService.findChatById(createMessageDTO.chatId());

       var aiResponse = assistantService.generateResponse(createMessageDTO.message());

        var message = new ChatMessages(createMessageDTO.message(), aiResponse, chat);

       messagesRepository.save(message);

       return new AIResponseDTO(aiResponse);

    }

    public List <MessageDTO> findChatMessages (HttpHeaders headers, Long id) {

        var searchForChat = chatService.findChatById(id);

        Long userId = tokenAuthService.findSessionId(headers);

        if (searchForChat.getUser().getId().equals(userId)) {

            var messages = messagesRepository.findByChat(searchForChat);

            return messages
                    .stream()
                    .map(m -> new MessageDTO(m.getMessage(), m.getResponse()))
                    .toList();
        }

        throw new ForbiddenAccessException("You are not authorized to view this chat");

    }

    @Transactional
    public void editMessage (EditMessageDTO editMessageDTO, Long id) {

        var searchForMessage = messagesRepository
                                .findById(id)
                                .orElseThrow(() -> new MessageNotFoundException("Message not found!"));

        searchForMessage.setMessage(editMessageDTO.message());

        messagesRepository.saveAndFlush(searchForMessage);

    }

    @Transactional
    public void deleteMessage (Long id) {

        var searchForMessage = messagesRepository
                .findById(id)
                .orElseThrow(() -> new MessageNotFoundException("Message not found!"));

        messagesRepository.delete(searchForMessage);

    }

}

package org.swiftpay.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.CreateChatDTO;
import org.swiftpay.dtos.EditChatDTO;
import org.swiftpay.dtos.UserChatsDTO;
import org.swiftpay.exceptions.ChatNotFoundException;
import org.swiftpay.model.Chat;
import org.swiftpay.repositories.ChatRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserServices userServices;

    private final ChatRepository chatRepository;

    private final AuthService authService;

    private final TokenAuthService tokenAuthService;

    public void createChat (HttpHeaders headers, CreateChatDTO createChatDTO) {

        authService.compareIdFromTheSessionWithTheIdInTheUrl(headers, createChatDTO.userId());

        var user = userServices.findUserById(createChatDTO.userId());

        chatRepository.save(new Chat(createChatDTO.title(), user));

    }

    public Set <UserChatsDTO> findChatByUserId (HttpHeaders headers) {

        Long id = tokenAuthService.findSessionId(headers);

        authService.compareIdFromTheSessionWithTheIdInTheUrl(headers, id);

        var chats = chatRepository
                .findByUser_Id(id)
                .stream()
                .map(UserChatsDTO::new)
                .collect(Collectors.toSet());

        if (chats.isEmpty()) {

            throw new ChatNotFoundException("Chat not found!");

        }

        return chats;

    }

    public void editChat (HttpHeaders headers, EditChatDTO editChatDTO, Long id) {

        Long userId = tokenAuthService.findSessionId(headers);

        authService.compareIdFromTheSessionWithTheIdInTheUrl(headers, userId);

        var chat = chatRepository
                    .findById(id)
                    .orElseThrow(() -> new ChatNotFoundException("Chat not found!"));

        chat.setTitle(editChatDTO.title());

        chatRepository.save(chat);

    }

    public void deleteChat (Long id) {

        var searchForChat = chatRepository
                            .findById(id)
                            .orElseThrow(() -> new ChatNotFoundException("Chat not found!"));

        chatRepository.delete(searchForChat);

    }

}

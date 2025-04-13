package org.swiftpay.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.swiftpay.repositories.TransferRepository;
import org.swiftpay.repositories.UserRepository;

@Service
public class TransferService {

    @Value("${asaas.api.key}")
    private String asaasKey;

    private final RestTemplate restTemplate;

    private final UserRepository userRepository;

    private final AuthorizationService authorizationService;

    private final TransferRepository transferRepository;

    public TransferService (RestTemplate restTemplate,
                            UserRepository userRepository,
                            AuthorizationService authorizationService,
                            TransferRepository transferRepository) {

        this.restTemplate = restTemplate;

        this.userRepository = userRepository;

        this.authorizationService = authorizationService;

        this.transferRepository = transferRepository;

    }

    @Transactional
    public void transferToSomeone () {

    }

}

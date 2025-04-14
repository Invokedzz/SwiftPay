package org.swiftpay.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.exceptions.APIErrorException;
import org.swiftpay.infrastructure.clients.AuthorizationClient;
import org.swiftpay.infrastructure.policies.TransferAuthorizationPolicy;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AuthorizationClient authorizationClient;

    private final TransferAuthorizationPolicy transferAuthorizationPolicy;

    public boolean validateTransfer () {

        return authorizationClient.validateTransference().data().authorization();

    }

    public void validateTransferBody (TransferDTO transferDTO) {

        transferAuthorizationPolicy.authorize(transferDTO);

    }

}

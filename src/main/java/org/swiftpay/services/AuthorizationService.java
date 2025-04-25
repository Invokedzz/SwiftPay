package org.swiftpay.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.dtos.TransferRequestDTO;
import org.swiftpay.infrastructure.clients.AuthorizationClient;
import org.swiftpay.infrastructure.policies.TransferAuthorizationPolicy;
import org.swiftpay.model.User;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AuthorizationClient authorizationClient;

    private final TransferAuthorizationPolicy transferAuthorizationPolicy;

    public boolean validateTransfer () {

        return authorizationClient
                .validateTransference()
                .data()
                .authorization();

    }

    public void validateTransferBody (TransferRequestDTO transferRequestDTO) {

        transferAuthorizationPolicy.authorizeTransfersToAsaasAccount(transferRequestDTO);

    }

    public void validateTransferBodySandboxVer (TransferDTO transferDTO) {

        transferAuthorizationPolicy.authorizeSandbox(transferDTO);

    }

    public void compareValueAndBalance (BigDecimal balance, BigDecimal value) {

        transferAuthorizationPolicy.validateValueThatIsGoingToBeTransferred(balance, value);

    }

    public void checkIfPayerIsASeller (User payer) {

        transferAuthorizationPolicy.validateUserRolesBeforeTransfer(payer);

    }

}

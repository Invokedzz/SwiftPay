package org.swiftpay.infrastructure.policies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.swiftpay.dtos.BankTransferRequestDTO;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.dtos.TransferRequestDTO;
import org.swiftpay.exceptions.SelfTransferException;
import org.swiftpay.services.TokenAuthService;
import org.swiftpay.services.UserServices;

@Component
@RequiredArgsConstructor
public class NotSelfTransferPolicy implements TransferPolicy {

    private final UserServices userServices;

    @Override
    public void validateSandbox (TransferDTO transferDTO) {

        if (transferDTO.payeeId().equals(transferDTO.payerId())) {

            throw new SelfTransferException("You are not allowed to transfer to yourself!");

        }

    }

    @Override
    public void validateAsaasTransfers (TransferRequestDTO transferRequestDTO) {



    }

    @Override
    public void validateTransferToBankAccounts(BankTransferRequestDTO bankTransferRequestDTO) {

    }

}

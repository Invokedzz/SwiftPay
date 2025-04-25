package org.swiftpay.infrastructure.policies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.dtos.TransferRequestDTO;
import org.swiftpay.exceptions.SelfTransferException;

@Component
@RequiredArgsConstructor
public class NotSelfTransferPolicy implements TransferPolicy {

    @Override
    public void validateSandbox (TransferDTO transferDTO) {

        if (transferDTO.payeeId().equals(transferDTO.payerId())) {

            throw new SelfTransferException("You are not allowed to transfer to yourself!");

        }

    }

    @Override
    public void validateAsaasTransfers (TransferRequestDTO transferRequestDTO) {

        if (transferRequestDTO.walletId().equals(transferRequestDTO.externalReference())) {

            throw new SelfTransferException("You are not allowed to transfer to yourself!");

        }

    }

}

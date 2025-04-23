package org.swiftpay.infrastructure.policies;

import org.springframework.stereotype.Component;
import org.swiftpay.dtos.BankTransferRequestDTO;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.dtos.TransferRequestDTO;
import org.swiftpay.exceptions.SelfTransferException;

@Component
public class NotSelfTransferPolicy implements TransferPolicy {

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

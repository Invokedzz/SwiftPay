package org.swiftpay.infrastructure.policies;

import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.exceptions.SelfTransferException;

public class NotSelfTransferPolicy implements TransferPolicy {

    @Override
    public void validate (TransferDTO transferDTO) {

        if (transferDTO.payeeId().equals(transferDTO.payerId())) {

            throw new SelfTransferException("You are not allowed to transfer to yourself!");

        }

    }

}

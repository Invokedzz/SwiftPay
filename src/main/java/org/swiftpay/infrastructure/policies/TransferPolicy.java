package org.swiftpay.infrastructure.policies;

import org.swiftpay.dtos.TransferDTO;

public interface TransferPolicy {

    void validate (TransferDTO transferDTO);

}

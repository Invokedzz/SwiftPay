package org.swiftpay.infrastructure.policies;

import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.dtos.TransferRequestDTO;

public interface TransferPolicy {

    void validateSandbox (TransferDTO transferDTO);

    void validateAsaasTransfers (TransferRequestDTO transferRequestDTO);

}

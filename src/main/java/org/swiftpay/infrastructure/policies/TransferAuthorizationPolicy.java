package org.swiftpay.infrastructure.policies;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.swiftpay.dtos.TransferDTO;

import java.util.List;

@Component
@AllArgsConstructor
public class TransferAuthorizationPolicy {

    private final List <TransferPolicy> transferPolicies;

    public void authorize (TransferDTO transferDTO) {

        transferPolicies.forEach(policy -> policy.validate(transferDTO));

    }

}

package org.swiftpay.infrastructure.policies;

import org.springframework.stereotype.Component;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.exceptions.InvalidAmountException;

import java.math.BigDecimal;

@Component
public class PositiveAmountPolicy implements TransferPolicy {

    @Override
    public void validate (TransferDTO transferDTO) {

        if (transferDTO.value().compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidAmountException("Invalid amount: " + transferDTO.value());

        }

    }

}

package org.swiftpay.infrastructure.policies;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.exceptions.InvalidAmountException;
import org.swiftpay.exceptions.InvalidTypeOfPayerException;
import org.swiftpay.model.User;

import java.math.BigDecimal;
import java.util.List;

@Component
@AllArgsConstructor
public class TransferAuthorizationPolicy {

    private final List <TransferPolicy> transferPolicies;

    public void authorize (TransferDTO transferDTO) {

        transferPolicies.forEach(policy -> policy.validate(transferDTO));

    }

    public void validateValueThatIsGoingToBeTransferred (BigDecimal balance, BigDecimal value) {

        if (value.compareTo(balance) > 0) {

            throw new InvalidAmountException("The value must be lower than or equal to " + balance);

        }

    }

    public void validateUserRolesBeforeTransfer (User payer) {

        var payerAuthorities = payer.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        if (payerAuthorities.getFirst().equals("ROLE_SELLER")) {

            throw new InvalidTypeOfPayerException("You're not allowed to transfer as a seller!");

        }

    }

}

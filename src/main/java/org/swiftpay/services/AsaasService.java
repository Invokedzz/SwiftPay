package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.PaymentRequestDTO;
import org.swiftpay.dtos.TransferResponseDTO;
import org.swiftpay.infrastructure.clients.AsaasClient;

@Service
@RequiredArgsConstructor
public class AsaasService {

    private final AsaasClient asaasClient;

    @Transactional
    public TransferResponseDTO createPayment (PaymentRequestDTO paymentRequestDTO) {

        return asaasClient.createPayment(paymentRequestDTO);

    }

}

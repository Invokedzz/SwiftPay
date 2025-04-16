package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.*;
import org.swiftpay.infrastructure.clients.AsaasClient;
import org.swiftpay.model.Asaas;
import org.swiftpay.repositories.AsaasRepository;

@Service
@RequiredArgsConstructor
public class AsaasService {

    private final AsaasClient asaasClient;

    private final AsaasRepository asaasRepository;

    public CustomerResponseDTO registerCustomerInAsaas (CustomerRequestDTO customerRequestDTO) {

        return asaasClient.createCustomer(customerRequestDTO);

    }

    public void saveCustomerInTheDB (SaveAsaasCustomerDTO saveAsaasCustomerDTO) {

        asaasRepository.save(new Asaas(saveAsaasCustomerDTO));

    }

    @Transactional
    public TransferResponseDTO createPayment (PaymentRequestDTO paymentRequestDTO) {

        return asaasClient.createPayment(paymentRequestDTO);

    }

}

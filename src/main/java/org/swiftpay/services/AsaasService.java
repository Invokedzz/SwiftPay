package org.swiftpay.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.*;
import org.swiftpay.infrastructure.clients.AsaasAccountsClient;
import org.swiftpay.model.Asaas;
import org.swiftpay.repositories.AsaasRepository;

@Service
@RequiredArgsConstructor
public class AsaasService {

    private final AsaasAccountsClient customerClient;

    private final AsaasRepository asaasRepository;

    public CustomerResponseDTO registerCustomerInAsaas (RegisterDTO registerDTO) {

        return customerClient.createCustomer(registerDTO);

    }

    public void saveCustomerInTheDB (SaveAsaasCustomerDTO saveAsaasCustomerDTO) {

        asaasRepository.save(new Asaas(saveAsaasCustomerDTO));

    }

}

package org.swiftpay.services;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.PIXKeyDTO;
import org.swiftpay.dtos.PIXKeyRequestDTO;
import org.swiftpay.dtos.PIXKeyResponseDTO;
import org.swiftpay.exceptions.KeyGenerationException;
import org.swiftpay.exceptions.KeyNotFoundException;
import org.swiftpay.infrastructure.clients.AsaasAccountsClient;
import org.swiftpay.infrastructure.clients.AsaasPIXClient;
import org.swiftpay.infrastructure.clients.AsaasTransferClient;

@Service
@RequiredArgsConstructor
public class AsaasService {

    private final AsaasPIXClient asaasPIXClient;

    private final AsaasTransferClient asaasTransferClient;

    private final AsaasAccountsClient asaasAccountsClient;

    public PIXKeyResponseDTO askAsaasToGeneratePIXKey (PIXKeyRequestDTO pixKeyRequestDTO) {

        try {

            return asaasPIXClient.generatePIXKey(pixKeyRequestDTO);

        } catch (FeignException.BadRequest ex) {

            throw new KeyGenerationException(ex.getMessage());

        }

    }

    public PIXKeyDTO fetchKeyFromAsaas (String id) {

        try {

            return asaasPIXClient.getIndividualKey(id);

        } catch (FeignException.NotFound ex) {

            throw new KeyNotFoundException(ex.getMessage());

        }

    }

}

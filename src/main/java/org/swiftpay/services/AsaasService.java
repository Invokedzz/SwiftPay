package org.swiftpay.services;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.*;
import org.swiftpay.exceptions.*;
import org.swiftpay.infrastructure.clients.AsaasAccountsClient;
import org.swiftpay.infrastructure.clients.AsaasPIXClient;
import org.swiftpay.infrastructure.clients.AsaasTransferClient;

@Service
@RequiredArgsConstructor
public class AsaasService {

    private final AsaasPIXClient asaasPIXClient;

    private final AsaasTransferClient asaasTransferClient;

    private final AsaasAccountsClient asaasAccountsClient;

    public CustomerResponseDTO registerCustomerInAsaas (RegisterDTO registerDTO) {

        try {

            return asaasAccountsClient.createCustomer(registerDTO);

        } catch (FeignException.BadRequest ex) {

            throw new UsernameAlreadyExistsException(ex.getMessage());

        } catch (FeignException.Unauthorized ex) {

            throw new AsaasUnauthorizedException(ex.getMessage());

        }

    }

    public TransferResponseDTO askAsaasToTransferToACertainAccount (TransferRequestDTO transferRequestDTO) {

        try {

            return asaasTransferClient.transferToAsaasAccount(transferRequestDTO);

        } catch (FeignException.BadRequest ex) {

            throw new InvalidAmountException(ex.getMessage());

        } catch (FeignException.Unauthorized ex) {

            throw new AsaasUnauthorizedException(ex.getMessage());

        }

    }

    public TransferStatusDTO getIndividualTransfer (String id) {

        try {

            return asaasTransferClient.getIndividualTransfer(id);

        } catch (FeignException.NotFound ex) {

            throw new TransferNotFoundException(ex.getMessage());

        } catch (FeignException.Unauthorized ex) {

            throw new AsaasUnauthorizedException(ex.getMessage());

        } catch (FeignException.Forbidden ex) {

            throw new AsaasForbiddenException(ex.getMessage());

        }

    }

    public void cancelTransfer (String id) {

        try {

            asaasTransferClient.cancelTransfer(id);

        } catch (FeignException.NotFound ex) {

            throw new TransferNotFoundException(ex.getMessage());

        } catch (FeignException.Unauthorized ex) {

            throw new AsaasUnauthorizedException(ex.getMessage());

        }

    }

    public PIXKeyResponseDTO askAsaasToGeneratePIXKey (PIXKeyRequestDTO pixKeyRequestDTO) {

        try {

            return asaasPIXClient.generatePIXKey(pixKeyRequestDTO);

        } catch (FeignException.BadRequest ex) {

            throw new KeyGenerationException(ex.getMessage());

        } catch (AsaasUnauthorizedException ex) {

            throw new AsaasUnauthorizedException(ex.getMessage());

        }

    }

    public PIXKeyDTO fetchKeyFromAsaas (String id) {

        try {

            return asaasPIXClient.getIndividualKey(id);

        } catch (FeignException.NotFound ex) {

            throw new KeyNotFoundException(ex.getMessage());

        } catch (FeignException.Unauthorized ex) {

            throw new AsaasUnauthorizedException(ex.getMessage());

        } catch (FeignException.Forbidden ex) {

            throw new AsaasForbiddenException(ex.getMessage());

        }

    }

    public PIXKeyData fetchUserKeysFromAsaas () {

        try {

            return asaasPIXClient.getKeys();

        } catch (FeignException.Unauthorized ex) {

            throw new AsaasUnauthorizedException(ex.getMessage());

        } catch (FeignException.Forbidden ex) {

            throw new AsaasForbiddenException(ex.getMessage());

        }

    }

    public void deletePIXKeyFromAccount (String id) {

        try {

            asaasPIXClient.deletePIXKey(id);

        } catch (FeignException.NotFound ex) {

            throw new KeyNotFoundException(ex.getMessage());

        } catch (FeignException.Unauthorized ex) {

            throw new AsaasUnauthorizedException(ex.getMessage());

        }

    }

}

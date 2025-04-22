package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.*;
import org.swiftpay.infrastructure.clients.AsaasTransferClient;
import org.swiftpay.repositories.TransferRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AsaasTransferClient asaasTransferClient;

    private final TransferRepository transferRepository;

    private final UserServices userServices;

    private final AuthService authService;

    private final AuthorizationService authorizationService;

    @Transactional
    public BankTransferResponseDTO transferToBankAccounts (BankTransferRequestDTO transferRequestDTO) {

        return null;

    }

    @Transactional
    public TransferResponseDTO transferToAsaasAccount (TransferRequestDTO transferRequestDTO) {



        return null;

    }

    public List <TransferStatusDTO> getTransfers (LocalDate createdAt) {

        return null;

    }

    public TransferStatusDTO getIndividualTransfer (String id) {

        return null;

    }

    public void cancelTransfer (String id) {



    }

}

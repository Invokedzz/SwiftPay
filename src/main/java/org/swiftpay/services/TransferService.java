package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.*;
import org.swiftpay.infrastructure.clients.AsaasTransferClient;
import org.swiftpay.model.User;
import org.swiftpay.repositories.TransferRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AsaasTransferClient asaasTransferClient;

    private final TransferRepository transferRepository;

    private final UserServices userServices;

    private final TokenAuthService tokenAuthService;

    private final AuthorizationService authorizationService;

    @Transactional
    public BankTransferResponseDTO transferToBankAccounts (BankTransferRequestDTO transferRequestDTO) {

        return null;

    }

    @Transactional
    public TransferResponseDTO transferToAsaasAccount (HttpHeaders headers, TransferRequestDTO transferRequestDTO) {

        Long payerId = tokenAuthService.findSessionId(headers);

        var payer = userServices.findUserById(payerId);

        var payee = userServices.findUserByWalletId(transferRequestDTO.walletId());

        authorizationService.checkIfPayerIsASeller(payer);

        authorizationService.compareValueAndBalance(payer.getWallet().getBalance(), transferRequestDTO.value());

        transference(payer, payee, transferRequestDTO.value());

        return asaasTransferClient.transferToAsaasAccount(transferRequestDTO);

    }

    public List <TransferStatusDTO> getTransfers (LocalDate createdAt) {

        return null;

    }

    public TransferStatusDTO getIndividualTransfer (String id) {

        return asaasTransferClient.getIndividualTransfer(id);

    }

    public void cancelTransfer (String id) {



    }

    private void transference (User payer, User payee, BigDecimal value) {

        payer.getWallet().setBalance(payer.getWallet().getBalance().subtract(value));

        payee.getWallet().setBalance(payee.getWallet().getBalance().add(value));

    }

}

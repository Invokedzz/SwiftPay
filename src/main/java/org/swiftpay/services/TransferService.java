package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.*;
import org.swiftpay.exceptions.TransferNotFoundException;
import org.swiftpay.infrastructure.clients.AsaasTransferClient;
import org.swiftpay.model.Transfer;
import org.swiftpay.model.User;
import org.swiftpay.model.enums.TransferStatus;
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

    private final WalletService walletService;

    private final AuthorizationService authorizationService;

    @Transactional
    public TransferResponseDTO transferToAsaasAccount (HttpHeaders headers, TransferRequestDTO transferRequestDTO) {

        Long payerId = tokenAuthService.findSessionId(headers);

        var payer = findPayerById(payerId);

        var payee = findPayeeByWalletId(transferRequestDTO.walletId());

        authorizationService.checkIfPayerIsASeller(payer);

        authorizationService.compareValueAndBalance(payer.getWallet().getBalance(), transferRequestDTO.value());

        transference(payer, payee, transferRequestDTO.value());

        var transferResponseDTO = asaasTransferClient.transferToAsaasAccount(transferRequestDTO);

        transferRepository.save(new Transfer(transferResponseDTO, payer, payee));

        return transferResponseDTO;

    }

    public List <TransferStatusDTO> getTransfers (HttpHeaders headers, LocalDate transferDate) {

        Long userId = tokenAuthService.findSessionId(headers);

        return transferRepository
                .findByPayer_Id(userId)
                .stream()
                .filter(transfer -> transfer.transferDate().equals(transferDate))
                .map(TransferStatusDTO::new).toList();

    }

    public TransferStatusDTO getIndividualTransfer (String id) {

        return asaasTransferClient.getIndividualTransfer(id);

    }

    public void confirmTransfer (String id) {

        var transfer = transferRepository
                        .findByTransferId(id)
                        .orElseThrow(() -> new TransferNotFoundException("We weren't able to find a transfer with this id: " + id));

        transfer.setStatus(TransferStatus.DONE);

        transferRepository.save(transfer);

    }

    public TransferStatusDTO cancelTransfer (String id) {

        var transfer = asaasTransferClient.cancelTransfer(id);

        var findTransfer = transferRepository
                            .findByTransferId(id)
                            .orElseThrow(() -> new TransferNotFoundException("We weren't able to find a transfer with id: " + id));

        findTransfer.setStatus(transfer.status());

        transferRepository.save(findTransfer);

        return transfer;

    }

    private User findPayerById (Long id) {

        return userServices.findUserById(id);

    }

    private User findPayeeByWalletId (String walletId) {

        return userServices.findUserByWalletId(walletId);

    }

    private void transference (User payer, User payee, BigDecimal value) {

        payer.getWallet().setBalance(payer.getWallet().getBalance().subtract(value));

        payee.getWallet().setBalance(payee.getWallet().getBalance().add(value));

        walletService.saveWalletAfterTransfer(payer.getWallet());

        walletService.saveWalletAfterTransfer(payee.getWallet());

    }

}

package org.swiftpay.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.swiftpay.infrastructure.clients.AsaasWalletClient;
import org.swiftpay.model.Wallet;
import org.swiftpay.repositories.WalletRepository;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    private final AsaasWalletClient asaasWalletClient;

    public void save (Wallet wallet) {

        var walletId = asaasWalletClient.createWalletIdForUser();

        wallet.setAsaasWalletId(walletId.id());

        walletRepository.save(wallet);

    }

}

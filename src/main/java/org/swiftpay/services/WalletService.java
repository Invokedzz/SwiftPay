package org.swiftpay.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.swiftpay.infrastructure.clients.AsaasWalletClient;
import org.swiftpay.model.User;
import org.swiftpay.model.Wallet;
import org.swiftpay.repositories.WalletRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    private final AsaasWalletClient asaasWalletClient;

    public void save (User user, String walletId) {

        var wallet = new Wallet();

        wallet.setUser(user);

        wallet.setBalance(BigDecimal.valueOf(300.0));

        wallet.setAsaasWalletId(walletId);

        walletRepository.save(wallet);

    }

    public void saveSandboxWallet (Wallet wallet) {

        wallet.setBalance(BigDecimal.valueOf(100.0));

        walletRepository.save(wallet);

    }

}

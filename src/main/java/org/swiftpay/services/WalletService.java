package org.swiftpay.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.swiftpay.model.Wallet;
import org.swiftpay.repositories.WalletRepository;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public void save (Wallet wallet) {

        walletRepository.save(wallet);

    }

}

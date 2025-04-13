package org.swiftpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swiftpay.model.Wallet;

@Repository
public interface WalletRepository extends JpaRepository <Wallet, Long> {}

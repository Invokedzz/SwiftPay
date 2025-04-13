package org.swiftpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swiftpay.model.Transfer;

@Repository
public interface TransferRepository extends JpaRepository <Transfer, Long> {
}

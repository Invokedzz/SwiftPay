package org.swiftpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swiftpay.model.PIX;

@Repository
public interface PIXRepository extends JpaRepository <PIX, Long> {
}

package org.swiftpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swiftpay.dtos.PIXKeyDTO;
import org.swiftpay.model.PIX;
import org.swiftpay.model.User;

import java.util.List;

@Repository
public interface PIXRepository extends JpaRepository <PIX, Long> {
    PIXKeyDTO findByAsaasPixIdAndUser(String asaasPixId, User user);

    List<PIX> findByUser_Id(Long userId);

    List<PIX> findByAsaasPixId(String asaasPixId);
}

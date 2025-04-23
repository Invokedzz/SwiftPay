package org.swiftpay.repositories;

import org.apache.commons.lang3.stream.Streams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swiftpay.dtos.TransferStatusDTO;
import org.swiftpay.model.Transfer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository <Transfer, Long> {

    List <TransferStatusDTO> findByPayer_Id(Long payerId);

    Optional <Transfer> findByTransferId(String transferId);

}

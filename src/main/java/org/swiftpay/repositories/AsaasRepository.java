package org.swiftpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swiftpay.model.Asaas;

@Repository
public interface AsaasRepository extends JpaRepository <Asaas, Long> {}

package org.swiftpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swiftpay.model.DeleteRegister;

@Repository
public interface DeleteRegisterRepository extends JpaRepository <DeleteRegister, Long> {
    void deleteByUser_Id(Long userId);
}

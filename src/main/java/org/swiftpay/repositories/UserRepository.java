package org.swiftpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swiftpay.model.User;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {}

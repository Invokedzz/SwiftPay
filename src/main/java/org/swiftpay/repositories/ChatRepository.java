package org.swiftpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swiftpay.model.Chat;

@Repository
public interface ChatRepository extends JpaRepository <Chat, Long> {
}

package org.swiftpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swiftpay.model.ChatMessages;

@Repository
public interface MessagesRepository extends JpaRepository <ChatMessages, Long> {
}

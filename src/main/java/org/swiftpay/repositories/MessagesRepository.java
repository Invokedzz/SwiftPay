package org.swiftpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swiftpay.model.Chat;
import org.swiftpay.model.ChatMessages;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository <ChatMessages, Long> {

   List <ChatMessages> findByChat (Chat chat);

}

package org.swiftpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swiftpay.dtos.UserChatsDTO;
import org.swiftpay.model.Chat;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository <Chat, Long> {

    List <UserChatsDTO> findByUser_Id (Long userId);

}

package org.swiftpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.swiftpay.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    Optional <User> findByEmail (String email);

    UserDetails findByUsername (String username);

    Optional <User> findByCpfCnpj (String cpfCnpj);

    Optional <User> findByUsernameEqualsIgnoreCase (String username);

    Optional <User> findByWalletAsaasWalletIdEqualsIgnoreCase (String walletId);

}

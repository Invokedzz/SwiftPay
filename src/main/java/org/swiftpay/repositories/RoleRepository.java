package org.swiftpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.swiftpay.model.Role;

@Repository
public interface RoleRepository extends JpaRepository <Role, Long> {

    @Modifying
    @Query(value = "insert into user_roles (user_id, role_id) values (:user_id, :role_id)", nativeQuery = true)
    void insertRole (@Param("user_id") Long userId, @Param("role_id") Long roleId);

}

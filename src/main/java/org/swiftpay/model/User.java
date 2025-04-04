package org.swiftpay.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.swiftpay.dtos.RegisterUserDTO;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String cpfCnpj;

    private String password;

    private Boolean active;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;

    public User (RegisterUserDTO registerUserDTO) {

        this.username = registerUserDTO.username();

        this.email = registerUserDTO.email();

        this.cpfCnpj = registerUserDTO.cpfCnpj();

        this.password = registerUserDTO.password();

        this.active = true;

    }

    public void activate () {

        this.active = true;

    }

    public void deactivate () {

        this.active = false;

    }

}

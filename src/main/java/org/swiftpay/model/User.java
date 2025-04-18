package org.swiftpay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.swiftpay.dtos.CustomerResponseDTO;
import org.swiftpay.dtos.RegisterDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String cpfCnpj;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private BigDecimal incomeValue;

    @Column(unique = true, nullable = false)
    private String postalCode;

    private Boolean active;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns =
    @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List <Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Asaas asaas;

    public User (RegisterDTO registerUserDTO) {

        this.username = registerUserDTO.name();

        this.email = registerUserDTO.email();

        this.cpfCnpj = registerUserDTO.cpfCnpj();

        this.password = registerUserDTO.password();

        this.birthDate = registerUserDTO.birthDate();

        this.incomeValue = registerUserDTO.incomeValue();

        this.postalCode = registerUserDTO.postalCode();

        this.active = false;

    }

    public void activate () {

        this.active = true;

    }

    public void deactivate () {

        this.active = false;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

    }

}

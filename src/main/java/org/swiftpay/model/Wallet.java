package org.swiftpay.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private BigDecimal balance;

    private String asaasWalletId;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

}

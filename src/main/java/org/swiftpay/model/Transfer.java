package org.swiftpay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.dtos.TransferResponseDTO;
import org.swiftpay.model.enums.TransferStatus;
import org.swiftpay.model.enums.TransferType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private BigDecimal value;

    private LocalDate transferDate;

    @Enumerated(EnumType.STRING)
    private TransferType type;

    private String transferId;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    @ManyToOne
    @JoinColumn(name = "payer_id")
    private User payer;

    @ManyToOne
    @JoinColumn(name = "payee_id")
    private User payee;

    public Transfer (TransferResponseDTO transferResponseDTO, User payer, User payee) {

        this.value = transferResponseDTO.value();

        this.transferDate = LocalDate.now();

        this.type = transferResponseDTO.type();

        this.transferId = transferResponseDTO.id();

        this.payer = payer;

        this.payee = payee;

    }

    public Transfer (TransferDTO transferDTO, LocalDate transferDate, User payer, User payee) {

        this.value = transferDTO.value();

        this.transferDate = transferDate;

        this.payer = payer;

        this.payee = payee;

    }

}

package org.swiftpay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.swiftpay.dtos.PIXKeyResponseDTO;
import org.swiftpay.model.enums.PIXStatus;
import org.swiftpay.model.enums.PIXType;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PIX {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String asaasPixId;

    private String key;

    @Enumerated(EnumType.STRING)
    private PIXType type;

    @Enumerated(EnumType.STRING)
    private PIXStatus status;

    private LocalDateTime createdAt;

    private Boolean canBeDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public PIX (PIXKeyResponseDTO pixKeyResponseDTO, User user) {

        this.asaasPixId = pixKeyResponseDTO.id();

        this.key = pixKeyResponseDTO.key();

        this.type = pixKeyResponseDTO.type();

        this.status = pixKeyResponseDTO.status();

        this.createdAt = LocalDateTime.now();

        this.canBeDeleted = pixKeyResponseDTO.canBeDeleted();

        this.user = user;

    }

}

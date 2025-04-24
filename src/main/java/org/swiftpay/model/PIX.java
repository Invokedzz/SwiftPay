package org.swiftpay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.swiftpay.model.enums.PIXStatus;
import org.swiftpay.model.enums.PIXType;

import java.time.LocalDate;

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

    private PIXType type;

    private PIXStatus status;

    private LocalDate dateCreated;

    private Boolean canBeDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

}

package org.swiftpay.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.swiftpay.dtos.SaveAsaasCustomerDTO;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asaas")
public class Asaas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String asaasId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Asaas (SaveAsaasCustomerDTO saveAsaasCustomerDTO) {

        this.asaasId = saveAsaasCustomerDTO.asaasId();

        this.user = saveAsaasCustomerDTO.user();

    }

}

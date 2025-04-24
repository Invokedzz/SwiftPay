package org.swiftpay.dtos;

import org.swiftpay.model.enums.PIXStatus;
import org.swiftpay.model.enums.PIXType;

import java.time.LocalDateTime;

public record PIXKeyResponseDTO (

        String id,

        String key,

  //      PIXType type,

//        PIXStatus status,

        LocalDateTime dateCreated,

        Boolean canBeDeleted

) {}

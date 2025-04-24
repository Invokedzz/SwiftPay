package org.swiftpay.dtos;

import org.swiftpay.model.enums.PIXStatus;
import org.swiftpay.model.enums.PIXType;

public record PIXKeyResponseDTO (

        String id,

        String key,

        PIXType type,

        PIXStatus status,

        Boolean canBeDeleted

) {}

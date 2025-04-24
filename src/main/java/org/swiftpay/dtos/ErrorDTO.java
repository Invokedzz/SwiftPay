package org.swiftpay.dtos;

import java.time.LocalDateTime;

public record ErrorDTO (

        Integer httpStatus,

        String message,

        LocalDateTime timestamp

) {}

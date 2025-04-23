package org.swiftpay.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum TransferStatus {

    DONE,

    PENDING,

    FAILED,

    CANCELLED,

    BANK_PROCESSING

}

package org.swiftpay.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum TransferType {

    PIX,

    TED,

    ASAAS_ACCOUNT

}

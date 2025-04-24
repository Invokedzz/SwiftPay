package org.swiftpay.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum PIXStatus {

    ACTIVE,

    ERROR,

    DELETED,

    AWAITING_ACCOUNT_DELETION,

    AWAITING_DELETION,

    AWAITING_ACTIVATION

}

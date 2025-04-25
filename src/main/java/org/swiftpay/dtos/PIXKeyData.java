package org.swiftpay.dtos;

import java.util.List;

public record PIXKeyData (List <PIXKeyDTO> data) {

    public PIXKeyData (PIXKeyData data) {

        this (data.data());

    }

}

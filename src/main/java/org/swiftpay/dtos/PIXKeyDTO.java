package org.swiftpay.dtos;

public record PIXKeyDTO (String key) {

    public PIXKeyDTO (PIXKeyDTO pixKeyDTO) {

        this (pixKeyDTO.key());

    }

}

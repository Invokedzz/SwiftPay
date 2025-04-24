package org.swiftpay.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.swiftpay.dtos.PIXKeyDTO;
import org.swiftpay.dtos.PIXKeyResponseDTO;
import org.swiftpay.infrastructure.clients.AsaasPIXClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PIXService {

    private final AsaasPIXClient asaasPIXClient;

    public PIXKeyResponseDTO generatePIXKey () {

        return null;

    }

    public PIXKeyDTO getIndividualKey () {

        return null;

    }

    public List <PIXKeyDTO> getKeys () {

        return null;

    }

    public void deletePIXKey () {}

}

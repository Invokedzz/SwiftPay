package org.swiftpay.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.swiftpay.infrastructure.clients.AsaasPIXClient;

@Service
@RequiredArgsConstructor
public class PIXService {

    private final AsaasPIXClient asaasPIXClient;

    public void generatePIXKey () {}

    public void generateQRCode () {}

    public void deletePIXKey () {}

    public void deleteQRCode () {}

}

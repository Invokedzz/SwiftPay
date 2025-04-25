package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.PIXKeyDTO;
import org.swiftpay.dtos.PIXKeyRequestDTO;
import org.swiftpay.dtos.PIXKeyResponseDTO;
import org.swiftpay.exceptions.KeyGenerationException;
import org.swiftpay.exceptions.KeyNotFoundException;
import org.swiftpay.infrastructure.clients.AsaasPIXClient;
import org.swiftpay.model.PIX;
import org.swiftpay.model.enums.PIXStatus;
import org.swiftpay.repositories.PIXRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PIXService {

    private final PIXRepository pixRepository;

    private final AsaasPIXClient asaasPIXClient;

    private final AsaasService asaasService;

    private final TokenAuthService tokenAuthService;

    private final UserServices userServices;

    @Transactional
    public PIXKeyResponseDTO generatePIXKey (HttpHeaders headers, PIXKeyRequestDTO pixKeyRequestDTO) {

        var response = asaasService.askAsaasToGeneratePIXKey(pixKeyRequestDTO);

        if (!PIXStatus.ERROR.equals(response.status())) {

            Long userId = tokenAuthService.findSessionId(headers);

            var user = userServices.findUserById(userId);

            pixRepository.save(new PIX(response, user));

            return response;

        }

        throw new KeyGenerationException("PIX Key generation failed. Please try again later.");

    }

    public PIXKeyDTO getIndividualKey (HttpHeaders headers, String id) {

        var pix = asaasService.fetchKeyFromAsaas(id);

        Long userId = tokenAuthService.findSessionId(headers);

        var pixKeys = pixRepository.findByUser_Id(userId);

        for (var pixKey : pixKeys) {

            if (pixKey.getAsaasPixId().equals(id)) {

                return pix;

            }

        }

        throw new KeyNotFoundException("We weren't able to find a PIX with this key!");

    }

    public List <PIXKeyDTO> getKeys () {

        return asaasPIXClient.getKeys().stream().map(PIXKeyDTO::new).collect(Collectors.toList());

    }

    public void deletePIXKey (String id) {}

}

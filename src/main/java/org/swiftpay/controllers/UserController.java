package org.swiftpay.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.dtos.*;
import org.swiftpay.services.UserServices;

@RestController
public record UserController (UserServices userServices) {

    @PostMapping("/register/client")
    private ResponseEntity <Void> clientRegister (@Valid @RequestBody RegisterDTO registerDTO) {

        userServices.registerAsClient(registerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/register/seller")
    private ResponseEntity <Void> sellerRegister (@Valid @RequestBody RegisterDTO registerDTO) {

        userServices.registerAsSeller(registerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/activate-account")
    private ResponseEntity <Void> activateAccount (@RequestHeader String token) {

        userServices.activateUserAccount(token);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();

    }

    @PostMapping("/login")
    private ResponseEntity <UserTokenDTO> login (@Valid @RequestBody LoginDTO loginDTO) {

        var generatedToken = userServices.login(loginDTO);

        return ResponseEntity.status(HttpStatus.OK).body(new UserTokenDTO(generatedToken));

    }

    @GetMapping("/profile/{id}")
    private ResponseEntity <AccountDTO> userProfile (@RequestHeader HttpHeaders headers, @PathVariable Long id) {

        var users = userServices.getProfileById(headers, id);

        return ResponseEntity.status(HttpStatus.OK).body(new AccountDTO(users));

    }

    @PutMapping("/reactivate-account")
    private ResponseEntity <Void> reactivateUser (@RequestBody ReactivateUserDTO reactivateUserDTO) {

        userServices.reactivateUserAccount(reactivateUserDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @DeleteMapping("/delete-account/{id}")
    private ResponseEntity <Void> disableUser (@RequestHeader HttpHeaders headers, @PathVariable Long id) {

        userServices.disableUserAccount(headers, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}

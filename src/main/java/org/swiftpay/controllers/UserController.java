package org.swiftpay.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.dtos.*;
import org.swiftpay.services.UserServices;

@RestController
@Tag(name = "Users", description = "Endpoints related to user, permissions and more!")
public record UserController (UserServices userServices) {

    @PostMapping("/register/client")
    @Operation(description = "Register as a client. A client is able to receive money, and transfer")
    private ResponseEntity <Void> clientRegister (@Valid @RequestBody RegisterDTO registerDTO) {

        userServices.registerAsClient(registerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/register/seller")
    @Operation(description = "Register as a seller. A seller is able to receive money, and that's it")
    private ResponseEntity <Void> sellerRegister (@Valid @RequestBody RegisterDTO registerDTO) {

        userServices.registerAsSeller(registerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/activate-account")
    @Operation(description = "Use this endpoint in order to activate your account after receiving an email. Get the id sent in the email URL, then use it here")
    private ResponseEntity <Void> activateAccount (@RequestHeader String token) {

        userServices.activateUserAccount(token);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();

    }

    @PostMapping("/login")
    @Operation(description = "After activating your account, do a login. If the request is successful, you're going to receive a JWT. Use it to access other endpoints")
    private ResponseEntity <UserTokenDTO> login (@Valid @RequestBody LoginDTO loginDTO) {

        var generatedToken = userServices.login(loginDTO);

        return ResponseEntity.status(HttpStatus.OK).body(new UserTokenDTO(generatedToken));

    }

    @GetMapping("/profile/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(description = "Endpoint to access your profile, after your account is created")
    private ResponseEntity <AccountDTO> userProfile (@RequestHeader HttpHeaders headers, @PathVariable Long id) {

        var users = userServices.getProfileById(headers, id);

        return ResponseEntity.status(HttpStatus.OK).body(new AccountDTO(users));

    }

    @PutMapping("/reactivate-account")
    @Operation(description = "If you want to reactivate your account, then use this endpoint. You're only able to do so if your account is not activated")
    private ResponseEntity <Void> reactivateUser (@RequestBody ReactivateUserDTO reactivateUserDTO) {

        userServices.reactivateUserAccount(reactivateUserDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @DeleteMapping("/delete-account/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(description = "Endpoint to delete your account from the system. After the request, you're going to receive an email confirming the deletion")
    private ResponseEntity <Void> disableUser (@RequestHeader HttpHeaders headers, @PathVariable Long id) {

        userServices.disableUserAccount(headers, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}

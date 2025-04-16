package org.swiftpay.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(description = "Register as a client. A client is able to receive money, and transfer", responses = {

            @ApiResponse(

                    description = "Created",

                    responseCode = "201"

            ),

            @ApiResponse(

                    description = "Bad Request",

                    responseCode = "400"

            )

    })

    private ResponseEntity <CustomerResponseDTO> clientRegister (@Valid @RequestBody RegisterDTO registerDTO) {

        var user = userServices.registerAsClient(registerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }

    @PostMapping("/register/seller")
    @Operation(description = "Register as a seller. A seller is able to receive money, and that's it", responses = {

            @ApiResponse(

                    description = "Created",

                    responseCode = "201"

            ),

            @ApiResponse(

                    description = "Bad Request",

                    responseCode = "400"

            )

    })

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
    @Operation(description = "After activating your account, do a login. If the request is successful," +
               " you're going to receive a JWT. Use it to access other endpoints", responses = {

            @ApiResponse(

                    description = "Ok",

                    responseCode = "200"

            ),

            @ApiResponse(

                    description = "Bad Request",

                    responseCode = "400"

            )

    })

    private ResponseEntity <UserTokenDTO> login (@Valid @RequestBody LoginDTO loginDTO) {

        var generatedToken = userServices.login(loginDTO);

        return ResponseEntity.status(HttpStatus.OK).body(new UserTokenDTO(generatedToken));

    }

    @GetMapping("/profile/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(description = "Endpoint to access your profile, after your account is created", responses = {

            @ApiResponse(

                    description = "Ok",

                    responseCode = "200"

            ),

            @ApiResponse(

                    description = "Not Found",

                    responseCode = "404"

            )

    })

    @SecurityRequirement(name = "bearerAuth")
    private ResponseEntity <AccountDTO> userProfile (@RequestHeader HttpHeaders headers, @PathVariable Long id) {

        var users = userServices.getProfileById(headers, id);

        return ResponseEntity.status(HttpStatus.OK).body(new AccountDTO(users));

    }

    @Hidden
    @PutMapping("/reactivate-account")
    private ResponseEntity <Void> reactivateUser (@RequestBody ReactivateUserDTO reactivateUserDTO) {

        userServices.reactivateUserAccount(reactivateUserDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @Hidden
    @DeleteMapping("/delete-account/{id}")
    private ResponseEntity <Void> disableUser (@RequestHeader HttpHeaders headers, @PathVariable Long id) {

        userServices.disableUserAccount(headers, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}

package org.swiftpay.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swiftpay.dtos.RegisterUserDTO;
import org.swiftpay.services.UserServices;

@RestController
public class UserController {

    private final UserServices userServices;

    public UserController (UserServices userServices) {

        this.userServices = userServices;

    }

    @PostMapping("/register")
    protected ResponseEntity <Void> register (RegisterUserDTO registerUserDTO) {

        userServices.register(registerUserDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

}

package org.swiftpay.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.dtos.*;
import org.swiftpay.services.UserServices;

import java.util.Set;

@RestController
public class UserController {

    private final UserServices userServices;

    public UserController (UserServices userServices) {

        this.userServices = userServices;

    }

    @PostMapping("/register/client")
    protected ResponseEntity <Void> clientRegister (@Valid @RequestBody RegisterDTO registerDTO) {

        userServices.registerAsClient(registerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/register/seller")
    protected ResponseEntity <Void> sellerRegister (@Valid @RequestBody RegisterDTO registerDTO) {

        userServices.registerAsSeller(registerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/login")
    protected ResponseEntity <UserTokenDTO> login (@Valid @RequestBody LoginDTO loginDTO) {

        var generatedToken = userServices.login(loginDTO);

        return ResponseEntity.status(HttpStatus.OK).body(new UserTokenDTO(generatedToken));

    }

    @GetMapping("/all")
    protected ResponseEntity <Set<ViewAllUsersDTO>> getAllUsers () {

        var users = userServices.getUsers();

        return ResponseEntity.status(HttpStatus.OK).body(users);

    }

    @PutMapping("/reactivate-account")
    protected ResponseEntity <Void> reactivateUser (@RequestBody ReactivateUserDTO reactivateUserDTO) {

        userServices.reactivateUserAccount(reactivateUserDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @DeleteMapping("/delete-account/{id}")
    protected ResponseEntity <Void> disableUser (@PathVariable Long id) {

        userServices.disableUserAccount(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}

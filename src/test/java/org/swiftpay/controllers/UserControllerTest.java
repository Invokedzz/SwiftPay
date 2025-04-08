package org.swiftpay.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.swiftpay.dtos.LoginDTO;
import org.swiftpay.dtos.ReactivateUserDTO;
import org.swiftpay.dtos.RegisterDTO;
import org.swiftpay.services.UserServices;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = UserController.class)
class UserControllerTest {

    /*

    Tests that we need to make: Register, Login, ReactivateAccount, DisableAccount;

    */

    @MockitoBean
    private UserServices userServices;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerClient_ThenReturnCREATED () throws Exception {

        RegisterDTO registerDTO = new RegisterDTO("Athena", "athena@gmail.com",
                                        "76323556855", "123456");

        var mockUserServices = Mockito.mock(UserServices.class);

        mockUserServices.registerAsClient(registerDTO);

        mockMvc.perform(post("/register/client")
                .contentType("application/json").content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isCreated());

        Mockito.verify(mockUserServices, Mockito.times(1)).registerAsClient(registerDTO);

    }

    @Test
    void registerClient_ThenThrowInvalidArgumentException_BecauseNameLengthIsInvalid () throws Exception {

        RegisterDTO registerDTO = new RegisterDTO("", "athena@gmail.com",
                "76323556855", "123456");

        var mockUserServices = Mockito.mock(UserServices.class);

        mockUserServices.registerAsClient(registerDTO);

        mockMvc.perform(post("/register/client")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                        .andExpect(status().isBadRequest());

        Mockito.verify(mockUserServices, Mockito.times(1)).registerAsClient(registerDTO);

    }

    @Test
    void registerSeller_ThenReturnCREATED () throws Exception {

        RegisterDTO registerDTO = new RegisterDTO("Athena", "athena@gmail.com",
                "76323556855", "123456");

        var mockUserServices = Mockito.mock(UserServices.class);

        mockUserServices.registerAsSeller(registerDTO);

        mockMvc.perform(post("/register/seller")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                        .andExpect(status().isBadRequest());

        Mockito.verify(mockUserServices, Mockito.times(1)).registerAsSeller(registerDTO);

    }

    @Test
    void login_ThenReturnOK_AndGenerateToken () throws Exception {

        LoginDTO loginDTO = new LoginDTO("athenas", "123456");

        Mockito.when(userServices.login(loginDTO)).thenReturn(Mockito.any());

         mockMvc.perform(post("/login")
                 .content(new ObjectMapper().writeValueAsString(loginDTO))
                 .contentType("application/json"))
                 .andExpect(status().isOk());

         Mockito.verify(userServices, Mockito.times(1)).login(loginDTO);

    }

    @Test
    void reactivateAccount_ThenReturnNoContent () throws Exception {

        ReactivateUserDTO reactivateUserDTO = new ReactivateUserDTO("athena@gmail.com");

        var mockUserServices = Mockito.mock(UserServices.class);

        mockUserServices.reactivateUserAccount(reactivateUserDTO);

        mockMvc.perform(put("/reactivate-account")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(reactivateUserDTO)))
                .andExpect(status().isNoContent());

        Mockito.verify(mockUserServices, Mockito.times(1)).reactivateUserAccount(reactivateUserDTO);

    }

    @Test
    void deleteAccount_ThenReturnNoContent () throws Exception {

        var mockUserServices = Mockito.mock(UserServices.class);

        mockUserServices.disableUserAccount(1L);

        mockMvc.perform(delete("/delete-account/{id}", 1L)
                        .contentType("application/json"))
                        .andExpect(status().isNoContent());

        Mockito.verify(mockUserServices, Mockito.times(1)).disableUserAccount(1L);

    }


}
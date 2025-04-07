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
import org.swiftpay.dtos.RegisterDTO;
import org.swiftpay.services.UserServices;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    void register_ThenReturnCREATED () throws Exception {

        RegisterDTO registerUserDTO = new RegisterDTO("Athena", "athena@gmail.com",
                                        "76323556855", "123456");

        var mockUserServices = Mockito.mock(UserServices.class);

        mockUserServices.register(registerUserDTO);

        mockMvc.perform(post("/register")
                .contentType("application/json").content(new ObjectMapper().writeValueAsString(registerUserDTO)))
                .andExpect(status().isCreated());

        Mockito.verify(mockUserServices, Mockito.times(1)).register(registerUserDTO);

    }

    @Test
    void register_ThenThrowInvalidArgumentException_BecauseNameLengthIsInvalid () throws Exception {

        RegisterDTO registerUserDTO = new RegisterDTO("", "athena@gmail.com",
                "76323556855", "123456");

        var mockUserServices = Mockito.mock(UserServices.class);

        mockUserServices.register(registerUserDTO);

        mockMvc.perform(post("/register")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(registerUserDTO)))
                        .andExpect(status().isBadRequest());

        Mockito.verify(mockUserServices, Mockito.times(1)).register(registerUserDTO);

    }

}
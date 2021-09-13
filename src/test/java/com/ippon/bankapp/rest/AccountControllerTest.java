package com.ippon.bankapp.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ippon.bankapp.rest.errors.RestErrorHandler;
import com.ippon.bankapp.service.AccountService;
import com.ippon.bankapp.service.dto.AccountDTO;
import com.ippon.bankapp.service.exception.AccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {AccountController.class, RestErrorHandler.class})
class AccountControllerTest {

    @MockBean
    private AccountService accountService;

    @Autowired
    private RestErrorHandler restErrorHandler;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        AccountController subject = new AccountController(accountService);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(subject)
                .setControllerAdvice(restErrorHandler)
                .build();
    }

    @Test
    public void testAccountRetrieval_AccountExists() throws Exception {
        given(accountService.getAccount(1))
                .willReturn(new AccountDTO()
                        .id(1)
                        .lastName("Scott")
                        .firstName("Ben"));

        mockMvc
                .perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ben"))
                .andExpect(jsonPath("$.lastName").value("Scott"));
    }

    @Test
    public void testAccountRetrieval_AccountDoesNotExist() throws Exception {
        given(accountService.getAccount(95))
                .willThrow(new AccountNotFoundException());

        String errorMessage = mockMvc
                .perform(get("/api/accounts/95"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getErrorMessage();

        assertThat(errorMessage, is("Account not found"));
    }

    @Test
    public void testAllAccountsRetrieval() throws Exception {
        AccountDTO firstAccountDTO = new AccountDTO()
                .id(1)
                .firstName("Ben")
                .lastName("Scott");

        AccountDTO secondAccountDTO = new AccountDTO()
                .id(2)
                .firstName("Mike")
                .lastName("Mitchell");

        given(accountService.getAllAccounts())
                .willReturn(new ArrayList<>(Arrays.asList(firstAccountDTO, secondAccountDTO)));

        mockMvc
                .perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].lastName").value("Scott"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].lastName").value("Mitchell"));
    }

    @Test
    public void testCreateAccount_requestValid() throws Exception {
        AccountDTO newAccount = new AccountDTO()
                .firstName("Ben")
                .lastName("Scott");

        given(accountService.createAccount(newAccount))
                .willReturn(new AccountDTO()
                        .id(1)
                        .lastName("Scott")
                        .firstName("Ben")
                        .balance(BigDecimal.ZERO));

        mockMvc
                .perform(post("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ben"))
                .andExpect(jsonPath("$.lastName").value("Scott"))
                .andExpect(jsonPath("$.balance").value(0.0));

    }

    @Test
    public void testCreateAccount_missingFirstName() throws Exception {

        AccountDTO newAccount = new AccountDTO()
                .lastName("Scott");

        ResultActions mvcResult = mockMvc
                .perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAccount_missingLastName() throws Exception {
        AccountDTO newAccount = new AccountDTO()
                .firstName("Ben");

        ResultActions mvcResult = mockMvc
                .perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isBadRequest());
    }

}

package com.ippon.bankapp.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ippon.bankapp.domain.Deposit;
import com.ippon.bankapp.domain.Transfer;
import com.ippon.bankapp.domain.Withdrawal;
import com.ippon.bankapp.rest.errors.RestErrorHandler;
import com.ippon.bankapp.service.AccountService;
import com.ippon.bankapp.service.dto.AccountDTO;
import com.ippon.bankapp.service.exception.AccountLastNameExistsException;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
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
        given(accountService.getAccount("Scott"))
                .willReturn(new AccountDTO()
                        .lastName("Scott")
                        .firstName("Ben"));

        mockMvc
                .perform(get("/api/account/Scott"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ben"))
                .andExpect(jsonPath("$.lastName").value("Scott"));
    }

    @Test
    public void testAccountRetrieval_AccountDoesNotExist() throws Exception {
        given(accountService.getAccount("Scott"))
                .willThrow(new AccountNotFoundException());

        String errorMessage = mockMvc
                .perform(get("/api/account/Scott"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getErrorMessage();

        assertThat(errorMessage, is("Account not found"));
    }

    @Test
    public void testCreateAccount_requestValid() throws Exception {
        AccountDTO newAccount = new AccountDTO()
                .firstName("Ben")
                .lastName("Scott");

        given(accountService.createAccount(newAccount))
                .willReturn(new AccountDTO()
                        .lastName("Scott")
                        .firstName("Ben")
                        .balance(BigDecimal.ZERO)
                        .notificationPreference("email"));

        mockMvc
                .perform(post("/api/account")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Ben"))
                .andExpect(jsonPath("$.lastName").value("Scott"))
                .andExpect(jsonPath("$.balance").value(0.0))
                .andExpect(jsonPath("$.notificationPreference").value("email"));

    }

    @Test
    public void testCreateAccount_missingFirstName() throws Exception {

        AccountDTO newAccount = new AccountDTO()
                .lastName("Scott");

        ResultActions mvcResult = mockMvc
                .perform(post("/api/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAccount_missingLastName() throws Exception {
        AccountDTO newAccount = new AccountDTO()
                .firstName("Ben");

        ResultActions mvcResult = mockMvc
                .perform(post("/api/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAccount_lastNameExists_throwsException() throws Exception {
        AccountDTO newAccount = new AccountDTO()
                .firstName("Ben")
                .lastName("Scott");

        given(accountService.createAccount(newAccount))
                .willThrow(new AccountLastNameExistsException());

        mockMvc
                .perform(post("/api/account/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testDeposit_requestValid() throws Exception {
        Deposit deposit = new Deposit(new BigDecimal("12.55"));

        given(accountService.depositIntoAccount(anyString(), any(Deposit.class)))
                .willReturn(new AccountDTO()
                        .lastName("Scott")
                        .firstName("Ben")
                        .balance(deposit.getAmount())
                        .notificationPreference("email"));

        mockMvc
                .perform(post("/api/account/Scott/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deposit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ben"))
                .andExpect(jsonPath("$.lastName").value("Scott"))
                .andExpect(jsonPath("$.balance").value(deposit.getAmount()))
                .andExpect(jsonPath("$.notificationPreference").value("email"));
    }

    @Test
    public void testWithdraw_requestValid() throws Exception {
        Withdrawal withdrawal = new Withdrawal(new BigDecimal("9.99"));

        given(accountService.withdrawFromAccount(anyString(), any(Withdrawal.class)))
                .willReturn(new AccountDTO()
                        .lastName("Scott")
                        .firstName("Ben")
                        .balance(new BigDecimal("0.01"))
                        .notificationPreference("email"));

        mockMvc
                .perform(post("/api/account/Scott/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ben"))
                .andExpect(jsonPath("$.lastName").value("Scott"))
                .andExpect(jsonPath("$.balance").value("0.01"))
                .andExpect(jsonPath("$.notificationPreference").value("email"));
    }

    @Test
    public void testTransfer_requestValid() throws Exception {
        Transfer withdrawal = new Transfer(new BigDecimal("10.99"), 1);

        given(accountService.transfer(anyString(), any(Transfer.class)))
                .willReturn(new AccountDTO()
                        .lastName("Scott")
                        .firstName("Ben")
                        .balance(withdrawal.getAmount())
                        .notificationPreference("email"));

        mockMvc
                .perform(post("/api/account/Thomas/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ben"))
                .andExpect(jsonPath("$.lastName").value("Scott"))
                .andExpect(jsonPath("$.balance").value(withdrawal.getAmount()))
                .andExpect(jsonPath("$.notificationPreference").value("email"));
    }

}

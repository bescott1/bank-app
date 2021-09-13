package com.ippon.bankapp.cucumber.stepdef;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ippon.bankapp.repository.AccountRepository;
import com.ippon.bankapp.rest.AccountController;
import com.ippon.bankapp.service.dto.AccountDTO;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import com.ippon.bankapp.domain.Deposit;
import com.ippon.bankapp.domain.Transfer;
import com.ippon.bankapp.domain.Withdrawal;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountStepDefinitions {

    private MockMvc mockMvc;

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(accountController)
                .build();
    }

    @After
    public void tearDown() {
        accountRepository.deleteAll();
    }

    @When("A Person {string} {string} creates an account")
    public void thatAPersonIsCreated(String first, String last) throws Exception {
        String results = mockMvc
                .perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"firstName\" : \"" + first + "\"," +
                                "\"lastName\" : \"" + last + "\"" +
                                "}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountDTO accountDTO = new ObjectMapper().readValue(results, AccountDTO.class);

        assertThat(accountDTO.getFirstName(), is(first));
        assertThat(accountDTO.getLastName(), is(last));
        assertThat(accountDTO.getBalance(), is(BigDecimal.ZERO));
    }

    @And("{string} {string} with id {int} deposits {double} into their account")
    public void aDepositIsMade(String first, String last, int id, double depositAmount) throws Exception {
        Deposit deposit = new Deposit(new BigDecimal(depositAmount));

        mockMvc
                .perform(post("/api/accounts/" + id + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(deposit)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @And("{string} {string} with id {int} withdraws {double} from their account")
    public void aWithdrawalIsMade(String first, String last, int id, double withdrawalAmount) throws Exception {
        Withdrawal withdrawal = new Withdrawal(new BigDecimal(withdrawalAmount));

        mockMvc
                .perform(post("/api/accounts/" + id + "/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(withdrawal)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @And("{string} {string} with id {int} transfers {double} into the account of id {int}")
    public void aTransferIsMade(String first, String last, int id, double transferAmount, int destId) throws Exception {
        Transfer transfer = new Transfer(new BigDecimal(transferAmount), destId);

        mockMvc
                .perform(post("/api/accounts/" + id + "/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(transfer)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Then("the {string} {string} account with id {int} has {double} balance")
    public void theAccountHasBalance(String firstName, String lastName, int id, double balance) throws Exception {
        mockMvc
                .perform(get("/api/accounts/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(balance))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName));
    }
}

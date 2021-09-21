package com.ippon.bankapp.cucumber.stepdef;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ippon.bankapp.repository.AccountRepository;
import com.ippon.bankapp.rest.AccountController;
import com.ippon.bankapp.service.dto.AccountDTO;
import com.ippon.bankapp.service.dto.DepositDTO;
import com.ippon.bankapp.service.dto.TransferDTO;
import com.ippon.bankapp.service.dto.WithdrawalDTO;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

    private Map<String, AccountDTO> accountsCreated = new HashMap<>();

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

    private void addAccount(AccountDTO account) {
        // For tests we'll use last name as our look up. Just use a different
        // last name if you encounter this error.
        assert (!accountsCreated.containsKey(account.getLastName()));
        accountsCreated.put(account.getLastName(), account);
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
        addAccount(accountDTO);

        assertThat(accountDTO.getFirstName(), is(first));
        assertThat(accountDTO.getLastName(), is(last));
        assertThat(accountDTO.getBalance(), is(BigDecimal.ZERO));
    }

    @Then("the {string} {string} account has {double} balance")
    public void theAccountHasBalance(String firstName, String lastName, double balance) throws Exception {

        AccountDTO accountDTO = accountsCreated.get(lastName);
        mockMvc
                .perform(get("/api/accounts/" + accountDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(balance))
                .andExpect(jsonPath("$.id").value(accountDTO.getId()))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName));
    }

    @And("{string} {string} deposits {double} into their account")
    public void aDepositIsMade(String firstName, String lastName, double depositAmount) throws Exception {
        DepositDTO depositDTO = new DepositDTO(BigDecimal.valueOf(depositAmount));
        AccountDTO accountDTO = accountsCreated.get(lastName);

        mockMvc
                .perform(post("/api/accounts/" + accountDTO.getId() + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(depositDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountDTO.getId()))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName));
    }

    @And("{string} {string} withdraws {double} from their account")
    public void aWithdrawalIsMade(String firstName, String lastName, double withdrawalAmount) throws Exception {
        WithdrawalDTO withdrawalDTO = new WithdrawalDTO(BigDecimal.valueOf(withdrawalAmount));
        AccountDTO accountDTO = accountsCreated.get(lastName);

        mockMvc
                .perform(post("/api/accounts/" + accountDTO.getId() + "/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(withdrawalDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountDTO.getId()))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName));
    }

    @And("{string} {string} transfers {double} into the account of {string} {string}")
    public void aTransferIsMade(String firstName, String lastName, double transferAmount, String destinationFirst, String destinationLast ) throws Exception {
        AccountDTO destinationAccount = accountsCreated.get(destinationLast);
        TransferDTO transferDTO = new TransferDTO(BigDecimal.valueOf(transferAmount), destinationAccount.getId());
        AccountDTO accountDTO = accountsCreated.get(lastName);

        mockMvc
                .perform(post("/api/accounts/" + accountDTO.getId() + "/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(transferDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountDTO.getId()))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName));
    }

}

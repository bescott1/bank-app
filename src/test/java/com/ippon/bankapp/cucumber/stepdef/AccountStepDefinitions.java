package com.ippon.bankapp.cucumber.stepdef;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ippon.bankapp.rest.AccountController;
import com.ippon.bankapp.service.dto.AccountDTO;
import io.cucumber.java.Before;
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

    private AccountDTO currentAccount;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(accountController)
                .build();
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
        currentAccount = accountDTO;

        assertThat(accountDTO.getFirstName(), is(first));
        assertThat(accountDTO.getLastName(), is(last));
        assertThat(accountDTO.getBalance(), is(BigDecimal.ZERO));
    }

    @Then("the {string} {string} account with id {int} has {double} balance")
    public void theAccountHasBalance(String firstName, String lastName, int id, double balance) throws Exception {
        mockMvc
                .perform(get("/api/accounts/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(balance));

    }

}

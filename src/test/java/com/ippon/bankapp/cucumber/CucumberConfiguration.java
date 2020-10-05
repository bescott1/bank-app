package com.ippon.bankapp.cucumber;

import com.ippon.bankapp.BankappApplication;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
@CucumberContextConfiguration
@ContextConfiguration(classes = BankappApplication.class)
public class CucumberConfiguration {

    @Before
    public void setupCucumberContext() {
        // Dummy method
    }

}

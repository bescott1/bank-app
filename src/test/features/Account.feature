Feature: Create an account

  Scenario: Create an account
    When A Person "Ben" "Scott" creates an account
    Then the account has 0.0 balance

Feature: Create an account

  Scenario: Create an account
    When A Person "Ben" "Scott" creates an account
    Then the "Ben" "Scott" account with id 1 has 0.0 balance

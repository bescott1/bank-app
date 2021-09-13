Feature: Create an account

  Scenario: Create an account
    When A Person "John" "Smith" creates an account
    Then the "John" "Smith" account has 0.0 balance

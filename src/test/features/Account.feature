Feature: Create an account

  Scenario: Create an account
    When A Person "John" "Smith" creates an account
    Then the "John" "Smith" account with id 8 has 0.0 balance

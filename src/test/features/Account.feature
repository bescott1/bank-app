Feature: Create an account

  Scenario: Create an account
    When A Person "John" "Smith" creates an account
    Then the "John" "Smith" account with id 1 has 0.0 balance

  Scenario: Deposit into an account
    When A Person "Jimmy" "Page" creates an account
    And "Jimmy" "Page" with id 2 deposits 10.00 into their account
    Then the "Jimmy" "Page" account with id 2 has 10.0 balance

  Scenario: Withdraw from an account
    When A Person "Lady" "Gaga" creates an account
    And "Lady" "Gaga" with id 3 deposits 10.00 into their account
    And "Lady" "Gaga" with id 3 withdraws 5.00 from their account
    Then the "Lady" "Gaga" account with id 3 has 5.0 balance

  Scenario: Transfer into an account
    When A Person "Timothy" "Jimothy" creates an account
    And "Timothy" "Jimothy" with id 4 deposits 10.00 into their account
    And A Person "Sarah" "Tarkovsky" creates an account
    And "Sarah" "Tarkovsky" with id 5 deposits 5.00 into their account
    And "Timothy" "Jimothy" with id 4 transfers 2.00 into the account of id 5
    Then the "Sarah" "Tarkovsky" account with id 5 has 7.0 balance
    And the "Timothy" "Jimothy" account with id 4 has 8.0 balance

Feature: Create an account

  Scenario: Create an account
    When A Person "John" "Smith" creates an account
    Then the "John" "Smith" account has 0.0 balance

  Scenario: Deposit into an account
    When A Person "Jimmy" "Page" creates an account
    And "Jimmy" "Page" deposits 10.3 into their account
    Then the "Jimmy" "Page" account has 10.3 balance

  Scenario Outline: Deposit multiple times into an account
    When A Person <firstName> <lastName> creates an account
    And <firstName> <lastName> deposits <amount1> into their account
    And <firstName> <lastName> deposits <amount2> into their account
    And <firstName> <lastName> deposits <amount3> into their account
    Then the <firstName> <lastName> account has <balance> balance
    Examples:
      | firstName | lastName   | amount1 | amount2 | amount3 | balance |
      | "Ben"     | "Scott"    | 10.30   | 0.01    | 1500.60 | 1510.91 |
      | "Mike"    | "Mitchell" | 1000.90 | 2500.32 | 100.60  | 3601.82 |


  Scenario: Withdraw from an account
    When A Person "Lady" "Gaga" creates an account
    And "Lady" "Gaga" deposits 10.00 into their account
    And "Lady" "Gaga" withdraws 5.00 from their account
    Then the "Lady" "Gaga" account has 5.0 balance

  Scenario: Transfer into an account
    When A Person "Timothy" "Jimothy" creates an account
    And "Timothy" "Jimothy" deposits 10.80 into their account
    And A Person "Sarah" "Tarkovsky" creates an account
    And "Sarah" "Tarkovsky" deposits 5.00 into their account
    And "Timothy" "Jimothy" transfers 2.50 into the account of "Sarah" "Tarkovsky"
    Then the "Sarah" "Tarkovsky" account has 7.5 balance
    And the "Timothy" "Jimothy" account has 8.3 balance

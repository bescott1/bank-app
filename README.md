# springboot-bank-app

Repository to learn how to do automated testing with Spring Boot.

## Existing features

* [Create an account](src/main/java/com/ippon/sprintbootbankapp/rest/AccountController.java#L22) 
* [Get account](src/main/java/com/ippon/sprintbootbankapp/rest/AccountController.java#L32) 
* [Get all accounts](src/main/java/com/ippon/sprintbootbankapp/rest/AccountController.java#L27)


## Definition of Done:

For each feature implemented there should be unit, integration and acceptance tests. Follow the existing features to see the scope. For example:

* [Unit tests for creating an account](src/test/java/com/ippon/sprintbootbankapp/service/AccountServiceTest.java)
* [Integration tests for the DB](src/test/java/com/ippon/sprintbootbankapp/repository/AccountRepositoryIntegrationTest.java)
* [Integration tests for the API layer](src/test/java/com/ippon/sprintbootbankapp/rest/AccountControllerTest.java)
* [Acceptance test for the create an account feature](src/test/features/Account.feature) with [glue code](src/test/java/com/ippon/sprintbootbankapp/cucumber/stepdef/AccountStepDefinitions.java)

## Todo features

* Deposit into an account
* Withdraw from an account
* Transfer from one account to another
* Get last 10 transaction from an account
* An account may not deposit more than $5000 per day


##

Check branch exercise-complete for a subset of those features completed and tested.

Feature: Registration feature


    # Changed raw_response column type in qahomework from varchar(255)
    # to text in order to have place to save raw response

    # Changed javax validation-api to spring-boot-starter-validation in qahomework
    # because validations were not working; changed @NotEmpty to @NotNull for non-string types;
    # removed @Patter annotations for non-string objects


  @Test
  Scenario: Add funds to newly registered client
    Given Register new client
    And Update personal data
    When Add "5.00" EUR funds to a client's wallet
    Then Check the client's balance is "5.00" EUR
    Then Check client's payments

  @Test
  Scenario: Fail attempt to register user with invalid data
    Given Fail to register invalid user

  @Test
  Scenario: Fail attempt to register user with existing email
    Given Register new client
    Then Fail to register existing client
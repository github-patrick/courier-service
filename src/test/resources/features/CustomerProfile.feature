Feature: As a user
  I want to create a customer profile
  So that I can be in the position to send parcels

  Scenario: A user successfully creates a customer profile
    Given I am a registered "customer"
    And I have a customer profile to create
      | fullName   |
      | John Baker |
    When I create the customer profile
    Then the customer profile should be created in the system
    And associated to the correct user


  Scenario: A customer must provide a full name
    Given I am a registered "customer"
    And I have a customer profile to create
      | fullName |
      |          |
    When I create the customer profile
    Then the customer profile should not be created in the system

  Scenario: Only users with customer type registrations can create a customer profile
    Given I am a registered "driver"
    And I have a customer profile to create
      | fullName   |
      | John Baker |
    When I create the customer profile
    Then the customer profile should not be created in the system


  Scenario Outline: A user can only create one customer profile
    Given I have a "customer" user signed up with the email address of "<email>"
    And I have a customer profile
    And I have a customer profile to create
      | fullName   |
      | John Baker |
    When I create the customer profile
    Then there should be only one customer profile for that user with the email address of "<email>"
    Examples:
      | email                  |
      | john.baker@courier.com |




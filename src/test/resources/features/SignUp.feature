Feature: As a user
  I want to sign up to the app
  So that I can use the courier service

  Scenario Outline: A user successfully signs up as a customer
    Given I am a customer
      | email              | password | type   |
      | random@courier.com | password | <type> |
    When I sign up
    Then I should be successfully registered as a customer
    Examples: Types
      | type     |
      | Customer |
      | Driver   |


  Scenario Outline: A user attempts to sign up without mandatory details
    Given I am a customer
      | email   | password   | type   |
      | <email> | <password> | <type> |
    When I sign up
    Then I should not be successful in registration
    Examples:
      | email              | password | type     |
      |                    | password | Customer |
      | random@courier.com |          | Driver   |

  Scenario Outline: A user's password should be greater than 5 characters
    Given I am a customer
      | email              | password   | type   |
      | random@courier.com | <password> | Driver |
    When I sign up
    Then I should not be successful in registration
    Examples:
      | password |
      | pass     |

  Scenario Outline: A user's email address should be unique in the system
    Given a user is signed up with the email address of "<email>"
    And I am a customer
      | email   | password | type   |
      | <email> | password | Driver |
    When I sign up
    Then I should not be successful in registration as the email is not unique
    Examples:
      | email              |
      | random@courier.com |



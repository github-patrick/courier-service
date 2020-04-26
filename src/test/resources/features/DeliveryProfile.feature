Feature: As a user
  I want to create a driver profile
  So that I can be in the position to deliver parcels


  Scenario: A user successfully creates a driver profile
    Given I am a registered "driver"
    And I have a driver profile to create
      | fullName   | status      |
      | John Baker | UNAVAILABLE |
    When I create the driver profile
    Then the driver profile should be created in the system

  Scenario: Only registered drivers can create a driver profile
    Given I am a registered "customer"
    And I have a driver profile to create
      | fullName   | status      |
      | John Baker | UNAVAILABLE |
    When I create the driver profile
    Then the driver profile should not be created in the system

  Scenario Outline: A registered driver cannot have more than one driver profile
    Given I have a "driver" user signed up with the email address of "<email>"
    And I have a driver profile
    And I have a driver profile to create
      | fullName   | status      |
      | John Baker | UNAVAILABLE |
    When I create the driver profile
    Then there should be only one driver profile for that user with the email address of "<email>"
    Examples:
      | email              |
      | driver@courier.com |

  Scenario: A driver profile must have a full name
    Given I am a registered "driver"
    And I have a driver profile to create
      | fullName | status      |
      |          | UNAVAILABLE |
    When I create the driver profile
    Then the driver profile should not be created in the system

  Scenario Outline: A driver profile's status must be UNAVAILABLE by default on creation
    Given I am a registered "driver"
    And I have a driver profile to create
      | fullName   | status   |
      | John Baker | <status> |
    When I create the driver profile
    Then the driver profile should be created in the system
    And the driver profile status should be "UNAVAILABLE"
    Examples:
      | status      |
      |             |
      | AVAILABLE   |
      | ON_DELIVERY |


  Scenario Outline: A driver can change the status of their driver's profile
    Given I have a "driver" user signed up with the email address of "<email>"
    And I have a driver profile
    When I attempt to change my profile status to "<status>"
    Then my new status should be "<status>"
    Examples:
      | email                   | status    |
      | danny.baker@courier.com | AVAILABLE |







@ParcelAssignment
Feature: Assignment

  Scenario: Parcels of high priority are assigned to an available driver
    Given I have a "customer" user signed up with the email address of "tommy.sword@gmail.com"
    And I have a customer profile
    Given I have a "driver" user signed up with the email address of "driver.one@gmail.com"
    And I have a driver profile
    And I have 1 parcels of priority "HIGH"
    When I attempt to change my profile status to "AVAILABLE"
    And I wait for 10 seconds
    Then the driver should have 1 parcels assigned


  Scenario: No parcels of high priority to be assigned to an available driver
    Given I have a "customer" user signed up with the email address of "tommy.wand@gmail.com"
    And I have a customer profile
    Given I have a "driver" user signed up with the email address of "driver.two@gmail.com"
    And I have a driver profile
    And I have 0 parcels of priority "HIGH"
    When I attempt to change my profile status to "AVAILABLE"
    And I wait for 10 seconds
    Then the driver should have 0 parcels assigned

  Scenario: No available delivery drivers for high priority parcels
    Given I have a "customer" user signed up with the email address of "tommy.cup@gmail.com"
    And I have a customer profile
    Given I have a "driver" user signed up with the email address of "driver.three@gmail.com"
    And I have a driver profile
    And I have 1 parcels of priority "HIGH"
    When I attempt to change my profile status to "UNAVAILABLE"
    And I wait for 10 seconds
    Then the driver should have 0 parcels assigned

  Scenario: No available delivery drivers for high priority parcels
    Given I have a "customer" user signed up with the email address of "tommy.pentacle@gmail.com"
    And I have a customer profile
    Given I have a "driver" user signed up with the email address of "driver.four@gmail.com"
    And I have a driver profile
    And I have 0 parcels of priority "HIGH"
    When I attempt to change my profile status to "UNAVAILABLE"
    And I wait for 10 seconds
    Then the driver should have 0 parcels assigned

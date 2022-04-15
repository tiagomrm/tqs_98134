Feature: Purchase trip

  Scenario: Valid purchase
    Given I am on the Travel Agency home page
    And I choose a flight from 'Portland' to 'Dublin'
    And I click the 'Find Flights' button
    And I choose a flight from the list
    And I fill the purchase form with valid information
    When I click the 'Purchase Flight' button
    Then I should get the confirmation page





Feature: Book Search
  Background: A Library

  Scenario: Search books by publication year
    Given an 'Action' book with the title 'One good book', written by 'Anonymous', published in 14-03-2013
    And another 'Action' book with the title 'Some other book', written by 'Tim Tomson', published in 23-09-2014
    And another 'Action' book with the title 'How to cook a dino', written by 'Fred Flintstone', published in 01-01-2012
    When the customer searches for books published between 2013 and 2014
    Then 2 books should have been found
    And Book 1 should have the title 'Some other book'
    And Book 2 should have the title 'One good book'

  Scenario: Search books by publication author
    Given an 'Action' book with the title 'One good book', written by 'Anonymous', published in 14-03-2013
    And another 'Action' book with the title 'Some other book', written by 'Tim Tomson', published in 23-09-2014
    And another 'Action' book with the title 'How to cook a dino', written by 'Fred Flintstone', published in 01-01-2012
    When the customer searches for books written by 'Tim Tomson'
    Then 1 books should have been found
    And Book 1 should have the title 'Some other book'

  Scenario: Search books by publication category
    Given an 'Action' book with the title 'One good book', written by 'Anonymous', published in 14-03-2013
    And another 'Action' book with the title 'Some other book', written by 'Tim Tomson', published in 23-09-2014
    And another 'Action' book with the title 'How to cook a dino', written by 'Fred Flintstone', published in 01-01-2012
    When the customer searches for books of category 'Action'
    Then 3 books should have been found
    And Book 1 should have the title 'Some other book'
    And Book 2 should have the title 'One good book'
    And Book 3 should have the title 'How to cook a dino'


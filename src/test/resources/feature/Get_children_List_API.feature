
Feature: Validate children with enrollments API response

Background: Given I have a valid ParentToken

  Scenario: Validate status message is "ok"
  	When I send a GET request of children-with-enrollments API
    Then the response status message should be "HTTP/1.1 200 OK"

  Scenario: Validate response schema
    When I send a GET request of children-with-enrollments API
    Then the response body should match the predefined JSON schema "children_with_enrollments_schema.json"
 

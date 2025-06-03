Feature: Login Functionality for Parent and Provider

  Scenario Outline: Successful Login with Valid credentials
    Given The "<user_type>" provides email "<email>" and password "<password>"
    When The "<user_type>" sends a POST request to the login endpoint
    Then The response status code should be 201
    And The response message should be "login_success"
    And The response should contain a valid access token
    Examples:
      | user_type | email                         | password   |
      | Provider  | pankaj.patidar@mindruby.com   | Test@12345 |
      | Parent    | pankaj@yopmail.com  		      | Test@12345 |

  Scenario Outline: Login with Empty/Invalid credentials : <user_type>-<email>-<password>
    Given The "<user_type>" provides email "<email>" and password "<password>"
    When The "<user_type>" sends a POST request to the login endpoint
    Then The response status code should be <statusCode>
    And The response error should be "<error_message>"

    Examples:
      | user_type | email                       | password   | statusCode | error_message                             					  |
      | Provider  |                             | Test@12345 | 422        | Please include a valid email            				      |
      | Provider  | pankaj.patidar@mindruby.com |            | 422        | Password is required                  					      |
  	  | Provider  |                             |            | 422        | Please include a valid email,Password is required             |
      | Provider  | pankaj@yopmail.com          | Test@12345 | 401        | provider_not_found,login                   					  |
      | Provider  | pankajpatidar@mindruby.com  | Test@12345 | 401        | provider_not_found,login              						  |
      | Provider  | pankaj.patidar@mindruby.com | Test@1234  | 401        | credentials_not_match,login            					      |
    
      | Parent 	  |                             | Test@12345 | 422        | Please include a valid email               					  |
      | Parent    | pankaj@yopmail.com          |            | 422        | Password is required                      					  |
  	  | Parent    |                             |            | 422        | Please include a valid email,Password is required 	          |
      | Parent    | pankaj.patidar@mindruby.com | Test@12345 | 401        | credentials_not_match,login                                   |
      | Parent    | pankajmindruby.com          | Test@12345 | 422        | Please include a valid email            					  |
      | Parent    | pankaj@yopmail.com          | Test@1234  | 401        | credentials_not_match,login             				      |
      
      